package com.cheng.core.service;


import com.alibaba.fastjson.JSON;
import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.core.entity.ScheduleTaskCommand;
import com.cheng.core.entity.TaskQueue;
import com.cheng.core.enums.Command;
import com.cheng.core.processor.JobProcessor;
import com.cheng.core.processor.JobProcessorService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 第三方引入client依赖时，手动bean注入，此处不需要加注解注入
 */
public class MessageProcessService {

    private Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE", MessageProcessService.class);

    @Autowired
    private JobProcessorService jobProcessorService;

    /**
     * 需要处理的消息队列
     */
    private TaskQueue queue;

    public void setQueue(TaskQueue queue) {
        this.queue = queue;
    }


    /**
     * 处理TaskQueue中的任务
     */
    public void process(ScheduleTaskCommand scheduleTaskCommand) {
        if (scheduleTaskCommand.getScheduleTraceId() == null || scheduleTaskCommand.getCommandId() == null
                || scheduleTaskCommand.getTaskId() == null) {
            logger.warn("the taskId,scheduleTraceId,commandId must not null [{}] ", JSON.toJSONString(scheduleTaskCommand));
            return;
        }

        try {
            JobProcessor jobProcessor = jobProcessorService.getProcessor(scheduleTaskCommand.getProcessor());
            if (jobProcessor == null) {
                logger.error("[{}] processor not found [{}] ", scheduleTaskCommand.getScheduleTraceId(), scheduleTaskCommand.getProcessor());
                return;
            }
            Command command = scheduleTaskCommand.getCommand();
            switch (command) {
                case SCHEDULE:
                case RUN:
                    queue.put(scheduleTaskCommand);
                    break;
                /**
                 * 对于停止指令，要优先执行
                 */
                case STOP:
                case SCHEDULE_STOP:
                    queue.putFirst(scheduleTaskCommand);
                    break;
                /**
                 * PING 指令为无效指令
                 */
                default:
                    logger.info("[{}] command not found [{}] ", scheduleTaskCommand.getScheduleTraceId(), scheduleTaskCommand.getCommand());


            }
        } catch (Exception e) {
            logger.error(" message process error " + JSON.toJSONString(scheduleTaskCommand), e);
        }
    }
}


