package com.cheng.schedule.server.trigger.cron;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.entity.TaskScheduleDO;
import com.cheng.schedule.server.trigger.Trigger;
import com.cheng.shedule.server.enums.ScheduleType;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class CronTrigger implements Trigger {
    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER",CronTrigger.class);

    LRUMap<String, CronExpression> lruMap = new LRUMap<>(1000);
    String lruKeyFormat = "%s_%s_%s";
    Lock lock = new ReentrantLock(true);

    @Override
    public Date getNextFireTime(TaskScheduleDO taskScheduleDO) {
        String key = String.format(lruKeyFormat, taskScheduleDO.getId(),
                taskScheduleDO.getScheduleType(), taskScheduleDO.getScheduleExpress());

        if (!lruMap.containsKey(key)){
            lock.lock();
            try{
               CronExpression cronExpression = new CronExpression(taskScheduleDO.getScheduleExpress());
                if (StringUtils.isNoneBlank(taskScheduleDO.getTimeZone())) {
                    cronExpression.setTimeZone(TimeZone.getTimeZone(taskScheduleDO.getTimeZone()));
                }
                lruMap.put(key, cronExpression);
            }catch (Throwable e){
                logger.error("init cron express exception ", e);
            }finally {
                lock.unlock();
            }
        }

        CronExpression cronExpression = lruMap.get(key);
        Date nextFireTime = taskScheduleDO.getNextFireTime();
        if (nextFireTime == null){
            nextFireTime = new Date(System.currentTimeMillis());
        }
        Date nextValidTimeAfter = cronExpression.getNextValidTimeAfter(nextFireTime);
        return nextValidTimeAfter;
    }

    @Override
    public String getTrigerName() {
        return ScheduleType.CRON.name();
    }
}
