package com.cheng.schedule.server.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.client.utils.StringUtils;
import com.cheng.core.entity.ScheduleTaskCommand;
import com.cheng.core.enums.Command;
import com.cheng.core.service.ScheduleCommandService;
import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.entity.TaskCommandDO;
import com.cheng.schedule.server.entity.TaskLogDO;
import com.cheng.schedule.server.repository.TaskCommandService;
import com.cheng.schedule.server.repository.TaskGroupService;
import com.cheng.schedule.server.repository.TaskLogService;
import com.cheng.schedule.server.repository.TaskScheduleService;
import com.cheng.schedule.server.util.RunState;
import com.cheng.util.InetUtils;
import com.cheng.util.TimeUtils;
import com.cheng.util.UUIDUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CommandPublishService {

    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER", CommandPublishService.class);

    @Autowired
    private TaskScheduleService taskScheduleService;

    @Autowired
    private ScheduleCacheService scheduleCacheService;

    @Autowired
    private TaskCommandService taskCommandService;

    @Autowired
    private ScheduleCommandService scheduleCommandService;

    @Autowired
    private TaskGroupService taskGroupService;


    @Autowired
    private TaskLogService taskLogService;

    /**
     * 从task_command表中获取指令记录，存放到 queue中
     * @param dispatchQueue
     */
    public void dispatchTask(BlockingQueue<TaskCommandDO> dispatchQueue) {
        logger.info("dispatch schedule task run");
        List<TaskCommandDO> taskCommandDOS = taskCommandService.lockGetTaskCommand();
        AtomicInteger cnt = new AtomicInteger(0);
        while (CollectionUtils.isNotEmpty(taskCommandDOS)){
            if (!RunState.canRun()){
                return;
            }
            taskCommandDOS.stream().forEach(command ->{
                if (scheduleCacheService.addDispatchQueue(command.getId())){
                    cnt.incrementAndGet();
                    try {
                        dispatchQueue.put(command);
                    } catch (InterruptedException e) {
                        logger.error("put data to the dispatch queue exception ", e);
                    }
                }
            });
            TimeUtils.sleep(1);
            taskCommandDOS = taskCommandService.lockGetTaskCommand();

        }
        logger.info("this batch dispatch finish ,all task is [{}]", cnt);
    }

    /**
     * command任务处理逻辑
     * @param dispatchQueue
     */
    public void process(BlockingQueue<TaskCommandDO> dispatchQueue) throws Exception{
        TaskCommandDO taskCommandDO = null;
        while (true){
            taskCommandDO = dispatchQueue.take();
            doDispatch(taskCommandDO);
        }

    }

    /**
     * 调度实际逻辑，主要进行远程命令发送
     * @param taskCommandDO
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    private void doDispatch(TaskCommandDO taskCommandDO) throws Exception{
        //先看该任务是否允许并发执行。
        // 如果允许，则直接执行；否则不执行
        if(!verify(taskCommandDO)){
            return;
        }
       //判断多少毫秒后要执行调度命令
       int diff =  (int)(taskCommandDO.getPublishTime().getTime()-System.currentTimeMillis());
       if(diff > 0){
           TimeUtils.sleep(diff);
       }
       //构造执行日志
        TaskLogDO taskLogDO = initTaskLog(taskCommandDO);
        boolean addLog = taskLogService.addTaskLog(taskLogDO);
        if (!addLog){
            throw new Exception("init task log exception ");
        }
        //构造ScheduleTaskCommand,server服务器下发到指定客户端机器
        ScheduleTaskCommand<Object> scheduleTaskMsg = buildPublishMsg(taskCommandDO, taskLogDO);

        String targetHost = getRemoteServer(taskCommandDO);
        Pair<Boolean, String> commandResult =  scheduleCommandService.command(scheduleTaskMsg, targetHost);
        //如果命令发送成功，更新task_command状态
        if (commandResult.getLeft()){
            taskCommandService.dispatchSuccess(taskCommandDO, commandResult.getRight());
        }
        logger.info("dispatch command {} to server[{}] result is {} ", JSON.toJSONString(taskCommandDO), commandResult.getRight(), commandResult.getLeft());

    }

    /**
     *
     * @param taskCommandDO
     * @return
     */
    private String getRemoteServer(TaskCommandDO taskCommandDO) {
        return null;
    }

    /**
     * 构造调度日志
     * @param taskCommandDO
     * @return
     */
    private TaskLogDO initTaskLog(TaskCommandDO taskCommandDO) {
        TaskLogDO taskLog = new TaskLogDO();
        taskLog.setScheduleServer(InetUtils.getSelfIp());
        taskLog.setTaskId(taskCommandDO.getTaskId());
        taskLog.setTraceId(UUIDUtils.next());
        taskLog.setCommandId(taskCommandDO.getId());
        taskLog.setGroupCode(taskGroupService.getById(taskCommandDO.getGroupId()).getGroupCode());
        taskLog.setCurrentStepCode("ROOT");
        taskLog.setParentTaskId(-1L);
        taskLog.setStartTime(new Date(System.currentTimeMillis()));
        return taskLog;
    }

    /**
     * 构造 ScheduleTaskCommand
     * @param taskCommandDO
     * @param taskLogDO
     * @return
     */
    private ScheduleTaskCommand<Object> buildPublishMsg(TaskCommandDO taskCommandDO, TaskLogDO taskLogDO) {
        ScheduleTaskCommand<Object> scheduleTaskMsg = new ScheduleTaskCommand<>();
        scheduleTaskMsg.setGroupCode(taskLogDO.getGroupCode());
        scheduleTaskMsg.setProcessor(taskCommandDO.getProcessor());
        scheduleTaskMsg.setTaskContext(taskCommandDO.getContext());
        scheduleTaskMsg.setCommand(Command.valueOf(taskCommandDO.getCommand()));
        scheduleTaskMsg.setCommandId(taskCommandDO.getId());
        scheduleTaskMsg.setScheduleTraceId(taskLogDO.getTraceId());
        scheduleTaskMsg.setStartHost(InetUtils.getSelfIp());
        scheduleTaskMsg.setTaskId(taskCommandDO.getTaskId());
        scheduleTaskMsg.setCurrentNodeName(taskLogDO.getCurrentStepCode());
        scheduleTaskMsg.setStartDate(new Date(System.currentTimeMillis()));
        scheduleTaskMsg.setParentCommandId(taskCommandDO.getRefCommandId());
        return scheduleTaskMsg;
    }

    /**
     * 判断任务是否运行执行
     * 1、如果当前command任务为非并发任务，且状态为执行中，则不允许执行；
     * 2、如果是页面下发的立即停止的任务，直接执行
     * @param taskCommandDO
     * @return
     */
    private boolean verify(TaskCommandDO taskCommandDO) {
        //如果收到了调度停止的命令，直接执行
        if (StringUtils.equals(taskCommandDO.getState(), Command.SCHEDULE_STOP.name())) {
            return true;
        }
        //非并发任务
        if (taskCommandDO.getParallel() == 0){
            boolean b = taskScheduleService.lockRunningState(taskCommandDO.getScheduleId(), taskCommandDO.getId());
            if (!b){
                logger.info("there is another dispatch run the work {} ", JSON.toJSONString(taskCommandDO));
                //锁定失败，重置task_command状态为INIT
                taskCommandService.resetLockInfo(taskCommandDO);
                return false;
            }
        }

        return true;
    }
}
