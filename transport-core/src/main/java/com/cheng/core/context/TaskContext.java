package com.cheng.core.context;

import com.cheng.logger.BusinessLoggerFactory;
import lombok.Data;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReentrantLock;

@Data
public class TaskContext {
    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE", TaskContext.class);

    private ConcurrentLinkedDeque<Long> commandIdQueue = new ConcurrentLinkedDeque();

    private ReentrantLock reentrantLock = new ReentrantLock(true);

    /**
     * 保存每个任务的状态
     */
    private Map<Long, TaskState> taskStateMap = new ConcurrentHashMap<>();

    public boolean validateRepeat(Long commandId, String stepTraceId){
        reentrantLock.lock();
        try {
            TaskState taskState = taskStateMap.get(stepTraceId);
            if (taskState != null){
                boolean isRepeat = taskState.isRepeat(stepTraceId);
                //如果没有重复，则将 stepTraceId植入
                if (!isRepeat){
                    taskState.addTrace(stepTraceId);
                }
                return isRepeat;
            }

            return false;
        } finally {
            reentrantLock.unlock();
        }

    }

    public void stop(Long commandId){
        reentrantLock.lock();
        try {
            if (!taskStateMap.containsKey(commandId)){
                taskStateMap.put(commandId, new TaskState());
            }
            taskStateMap.get(commandId).stop();
            logger.info("stop result {} ",taskStateMap.get(commandId).isStoped());

        }finally {
            reentrantLock.unlock();
        }
    }

    /**
     * init task context
     * @param commandId
     */
    public void init(Long commandId){
        if (taskStateMap.containsKey(commandId)){
            return;
        }

        reentrantLock.lock();
        try{
            if (taskStateMap.containsKey(commandId)){
                return;
            }

            taskStateMap.put(commandId, new TaskState());
            commandIdQueue.add(commandId);
            if (commandIdQueue.size()> 50){
                Long poll = commandIdQueue.poll();
                if (poll != null){
                    taskStateMap.remove(poll);
                }
            }
        }finally {
            reentrantLock.unlock();
        }
    }

    /**
     * 判断任务是否能运行
     * @param commandId
     * @return
     */
    public boolean stateNormal(Long commandId){
        TaskState taskState = taskStateMap.get(commandId);
        if (taskState != null){
            return !taskState.isStoped();
        }
        return true;
    }
}
