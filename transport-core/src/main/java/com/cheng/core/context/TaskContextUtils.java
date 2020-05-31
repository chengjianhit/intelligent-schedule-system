package com.cheng.core.context;

import com.cheng.core.entity.ScheduleTaskCommand;
import com.cheng.core.enums.Command;
import com.cheng.logger.BusinessLoggerFactory;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;

import java.util.Map;

/**
 * we suggest that one task the current validate traceId info may not exceed to 1000 at the same time,
 * <p>
 * data model is {
 * taskId:{
 * commandIdQueue,
 * commandId:{
 * state,
 * traceQueue
 * }
 * }
 * }
 * when task schedule or run ,then init the command info
 *

 */
public class TaskContextUtils {
    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE", TaskContextUtils.class);

    static Map<Long, TaskContext> taskContextMap = Maps.newConcurrentMap();

    public static boolean stateNormal(ScheduleTaskCommand scheduleTaskMsg) {
        //如果是多级任务，则保持一致性，执行完毕再说
        int level = 0;
        String[] s = StringUtils.split(scheduleTaskMsg.getCurrentNodeName(), "_");
        if (s != null && s.length == 2) {
            level = NumberUtils.toInt(s[1], 0);
        }
        if (scheduleTaskMsg.getCommand() == Command.RUN && level > 1) {
            return true;
        }
        Long taskId = scheduleTaskMsg.getTaskId();
        if (taskContextMap.containsKey(taskId)) {
            TaskContext taskContext = taskContextMap.get(taskId);
            boolean b = taskContext.stateNormal(scheduleTaskMsg.getCommandId());
            logger.info("stop validate result [{}] is {} {}", scheduleTaskMsg.getStepTraceId(),scheduleTaskMsg.getCommandId(),b);
            return b;

        }
        return true;
    }

    public static void stop(Long taskId,Long commandId) {
        if (taskContextMap.containsKey(taskId)) {
            TaskContext taskContext = taskContextMap.get(taskId);
            taskContext.stop(commandId);
            logger.info("stop result is {} {}", commandId,taskContext.stateNormal(commandId));
        }
    }

    public static void initContext(ScheduleTaskCommand scheduleTaskMsg) {
        Long taskId = scheduleTaskMsg.getTaskId();
        if (!taskContextMap.containsKey(taskId)) {
            synchronized (TaskContextUtils.class) {
                if (!taskContextMap.containsKey(taskId)) {
                    TaskContext taskContext = new TaskContext();
                    taskContext.init(scheduleTaskMsg.getCommandId());
                    taskContextMap.put(taskId, taskContext);
                }
            }
        }
    }


    public static boolean validateAndInit(ScheduleTaskCommand scheduleTaskMsg) {
        initContext(scheduleTaskMsg);
        TaskContext taskContext = taskContextMap.get(scheduleTaskMsg.getTaskId());
        return taskContext.validateRepeat(scheduleTaskMsg.getCommandId(), scheduleTaskMsg.getStepTraceId());
    }
}
