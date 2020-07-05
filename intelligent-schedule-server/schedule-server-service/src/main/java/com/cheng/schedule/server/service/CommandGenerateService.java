package com.cheng.schedule.server.service;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.entity.TaskScheduleDO;
import com.cheng.schedule.server.repository.TaskScheduleService;
import com.cheng.schedule.server.util.RunState;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CommandGenerateService {

    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER", CommandGenerateService.class);

    @Autowired
    private TaskScheduleService taskScheduleService;

    @Autowired
    private ScheduleCacheService scheduleCacheService;


    /**
     * 外部传入并发阻塞队列，将需要调度的任务put到队列里面
     * @param scheduleQueue
     */
    public void lockAndGetTask(BlockingQueue<TaskScheduleDO> scheduleQueue) {
        logger.info("generate schedule task run");
        List<TaskScheduleDO> taskScheduleDOList = taskScheduleService.lockGetForSchedule();
        AtomicInteger cnt = new AtomicInteger(0);
        while (CollectionUtils.isNotEmpty(taskScheduleDOList)){
            if (!RunState.canRun()){
                logger.warn("the run state can't run ");
                return;
            }

            taskScheduleDOList.stream().forEach(taskScheduleDO -> {
                //需要调度的任务，本地缓存保存一份
                boolean b = scheduleCacheService.addGenerateQueue(taskScheduleDO.getId());
                if (b){
                    cnt.incrementAndGet();
                    try {
                        scheduleQueue.put(taskScheduleDO);
                    } catch (InterruptedException e) {
                        logger.error("put data to the schedule queue exception ", e);
                    }
                }
            } );
            //循环获取需要执行的任务，sleep 1 毫秒，让出CPU
            taskScheduleDOList = taskScheduleService.lockGetForSchedule();
            sleep(1);
        }

        logger.info("this batch run finish ,all generate is [{}]", cnt);

    }

    public void process(BlockingQueue<TaskScheduleDO> scheduleQueue) {
    }
}

    private void sleep(int time) {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (Throwable e) {
            logger.error("sleep time error when task generate", e);
        }
    }
