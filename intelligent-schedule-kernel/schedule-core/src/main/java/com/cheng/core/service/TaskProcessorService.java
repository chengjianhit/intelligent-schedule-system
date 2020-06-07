package com.cheng.core.service;

import com.alibaba.fastjson.JSON;
import com.cheng.core.client.ConnectionInstance;
import com.cheng.core.context.TaskContextUtils;
import com.cheng.core.entity.ScheduleTaskCommand;
import com.cheng.core.entity.TaskQueue;
import com.cheng.core.enums.Command;
import com.cheng.core.processor.JobProcessor;
import com.cheng.core.processor.JobProcessorService;
import com.cheng.core.store.TaskExecRecordService;
import com.cheng.core.util.ScheduleContextUtils;
import com.cheng.core.util.TaskNodeUtil;
import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.util.UUIDUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class TaskProcessorService {
    private Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE", TaskProcessorService.class);

    @Autowired
    private JobProcessorService jobProcessorService;

    @Autowired
    private TaskExecRecordService taskExecLogService;

    @Autowired
    private MessageProcessService messageProcessService;

    private TaskQueue<ScheduleTaskCommand> queue;

    private int workThread = 10;
    private int queueSize = 100;

    /**
     * init初始化任务处理器
     */
    public void init(){
        /**
         * 初始化任务处理队列，messageProcessService会设置 queue
         */
        queue = new TaskQueue<>(queueSize);
        messageProcessService.setQueue(queue);

        ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(workThread,
                new BasicThreadFactory.Builder().namingPattern("schedule-processor-%d").priority(10).daemon(true).build());

        for (int i=0; i<workThread; i++){
            threadPoolExecutor.submit((Runnable) () -> {
               while (true){
                   try {
                       ScheduleTaskCommand waitingDealCommand = queue.take();
                       processTask(waitingDealCommand);
                   } catch (Exception e) {
                       logger.error("process msg error ", e);
                   }
               }
            });
        }
    }

    /**
     * 处理从任务队列Queue里面的命令
     * @param waitingDealCommand
     */
    private void processTask(ScheduleTaskCommand waitingDealCommand) {
        try {
            JobProcessor jobProcessor = jobProcessorService.getProcessor(waitingDealCommand.getProcessor());
            Command command = waitingDealCommand.getCommand();
            //根据不同类型command进行处理
            switch (command){
                case SCHEDULE:
                    TaskContextUtils.initContext(waitingDealCommand);
                    fetchData(jobProcessor, waitingDealCommand);
                    break;
                case RUN:
                    processData(jobProcessor, waitingDealCommand);
                    break;
                case SCHEDULE_STOP:
                    dispatchStop(waitingDealCommand);
                    taskExecLogService.updateStopState(waitingDealCommand);
                    break;
                case STOP:
                    TaskContextUtils.stop(waitingDealCommand.getTaskId(), waitingDealCommand.getCommandId());
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送任务终止指令
     * @param scheduleTaskMsg
     */
    private void dispatchStop(ScheduleTaskCommand scheduleTaskMsg) {
        ConnectionManagerService connectionManagerService = ScheduleContextUtils.getBean(ConnectionManagerService.class);
        List<ConnectionInstance> connectionInstances = connectionManagerService.selectAllInstance(scheduleTaskMsg.getGroupCode());
        if (CollectionUtils.isNotEmpty(connectionInstances)){
            connectionInstances.stream().forEach(instance -> {
                //给所有的客户端发送任务终止指令
                ScheduleTaskCommand<Object> stopMsg = new ScheduleTaskCommand<>();
                BeanUtils.copyProperties(scheduleTaskMsg, stopMsg);
                stopMsg.setCommand(Command.STOP);
                stopMsg.setCurrentNodeName(TaskNodeUtil.nextLevel(scheduleTaskMsg.getCurrentNodeName()));
                stopMsg.setStepTraceId(UUIDUtils.next());
                //retry 3 times to one server ,if the it fail
                for (int i=0; i<3; i++){
                    try {
                        boolean b = instance.sendCommand(stopMsg);
                        logger.info("send command [{}] to client [{}],result is [{}]", JSON.toJSONString(stopMsg), instance.getRemoteServer(), b);
                        if (b){
                            break;
                        }
                    } catch (Exception e) {
                        logger.error("[NOTIFY] send stop command to " + instance.getRemoteServer() + " exception", e);
                    }
                }
            });
        }
    }

    /**
     * record the task run result
     * @param jobProcessor
     * @param scheduleTaskMsg
     */
    private void processData(JobProcessor jobProcessor, ScheduleTaskCommand scheduleTaskMsg) throws Exception {
        boolean repeatRequest = TaskContextUtils.validateAndInit(scheduleTaskMsg);
        if (repeatRequest) {
            logger.error("repeat request [{}] ", JSON.toJSONString(scheduleTaskMsg));
            return;
        }
        Integer fail = CollectionUtils.size(scheduleTaskMsg.getTaskDataList()), suc = 0;
        try {
            if (CollectionUtils.isNotEmpty(scheduleTaskMsg.getTaskDataList())) {
                suc = jobProcessor.processData(scheduleTaskMsg);
                fail = fail - suc;
            }
        } catch (Exception e) {
            logger.error("[NOTIFY] process data error [{}] " + JSON.toJSONString(scheduleTaskMsg), e);
        }
        logger.info("add batch [{}] [{}] [{}] [{}] [{}]", scheduleTaskMsg.getCommandId(), scheduleTaskMsg.getStepTraceId(), CollectionUtils.size(scheduleTaskMsg.getTaskDataList()), suc, fail);
        if (TaskNodeUtil.isFirstLevel(scheduleTaskMsg.getCurrentNodeName())) {
            taskExecLogService.updateRunResult(scheduleTaskMsg.getCommandId(), suc, fail);
        }
    }

    /**
     * get the task data
     * @param jobProcessor
     * @param scheduleTaskMsg
     */
    private void fetchData(JobProcessor jobProcessor, ScheduleTaskCommand scheduleTaskMsg) {
        try {
            Long totalData = jobProcessor.fetchData(scheduleTaskMsg);
            if (totalData <= 0){
                logger.error("commandId [%s] fetch size zero ,task state may not correct ", scheduleTaskMsg.getCommandId());
            }

            logger.info("task [{}] all data [{}]  ", scheduleTaskMsg.getCommandId(), totalData);
            boolean b = taskExecLogService.updateFetchResult(scheduleTaskMsg.getCommandId(), totalData);
            if (!b) {
                logger.warn("task [{}] dispatch and fetch [{}] no equal", scheduleTaskMsg.getCommandId(), totalData);
            } else {
                taskExecLogService.cleanRunState(scheduleTaskMsg.getCommandId());
            }
        } catch (Exception e) {
            logger.error("[NOTIFY] fetch data error  " + JSON.toJSONString(scheduleTaskMsg), e);
        } finally {
        }


    }

}
