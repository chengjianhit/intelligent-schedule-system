package com.cheng.schedule.server.trigger;

import com.cheng.schedule.server.entity.TaskScheduleDO;

import java.util.Date;

public interface Trigger {

    Date getNextFireTime(TaskScheduleDO taskScheduleDO);

    String getTrigerName();
}
