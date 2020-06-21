package com.cheng.schedule.server.service;

import com.cheng.schedule.server.entity.TaskScheduleDO;

import java.util.concurrent.BlockingQueue;

public class CommandGenerateService {

    /**
     * 外部传入并发阻塞队列，将需要调度的任务put到队列里面
     * @param scheduleQueue
     */
    public void lockAndGetTask(BlockingQueue<TaskScheduleDO> scheduleQueue) {
    }

    public void process(BlockingQueue<TaskScheduleDO> scheduleQueue) {
    }
}
