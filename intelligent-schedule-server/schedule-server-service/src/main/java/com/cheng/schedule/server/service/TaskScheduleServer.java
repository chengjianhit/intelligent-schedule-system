package com.cheng.schedule.server.service;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.entity.TaskCommandDO;
import com.cheng.schedule.server.entity.TaskScheduleDO;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

@Component
public class TaskScheduleServer {

    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER", TaskScheduleServer.class);

    @Autowired
    public CommandGenerateService commandGenerateService;
    @Autowired
    public CommandPublishService commandPublishService;




    ScheduledExecutorService scheduledService = null;
    ScheduledExecutorService workerService = null;

    @Value("${task.schedule.generateTask}")
    private int generateTask = 10;

    @Value("${task.schedule.dispatchTask}")
    private int dispatchTask = 20;

    BlockingQueue<TaskScheduleDO> scheduleQueue = new ArrayBlockingQueue(1024);
    BlockingQueue<TaskCommandDO> dispatchQueue = new LinkedBlockingQueue(1024);

    //TODO ScheduleServer服务端启动入口
    @PostConstruct
    public void startSchedule() throws Exception{
        scheduledService = new ScheduledThreadPoolExecutor(2,
                new BasicThreadFactory.Builder().namingPattern("scheduler-server-%d").priority(10).daemon(true).build());

        workerService = new ScheduledThreadPoolExecutor(generateTask+dispatchTask,
                new BasicThreadFactory.Builder().namingPattern("scheduler-worker-%d").priority(10).daemon(true).build());

        //每3秒执行一次 调度命令生成任务，将生成的任务Task填充scheduleQueue中
        scheduledService.scheduleWithFixedDelay(() -> commandGenerateService.lockAndGetTask(scheduleQueue), 0, 3, TimeUnit.SECONDS);

        //每3秒执行一次 分发Dispatch任务生成方法，将生成的Dispatch任务填充到DispatchQueue队列中
        scheduledService.scheduleWithFixedDelay(() -> commandPublishService.dispatchTask(dispatchQueue), 0, 3, TimeUnit.SECONDS);
        for (int i = 0; i < generateTask; i++) {
            workerService.submit(() -> {
                try {
                    commandGenerateService.process(scheduleQueue);
                } catch (Throwable e) {
                    logger.error("generateService process task error ", e);
                }
            });
        }

        for (int i = 0; i < dispatchTask; i++) {
            workerService.submit(() -> {
                try {
                    commandPublishService.process(dispatchQueue);
                } catch (Throwable e) {
                    logger.error("publishService process task error ", e);
                }
            });
        }


        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            //关闭相关线程池
            scheduledService.shutdown();
            workerService.shutdown();
        }));
    }


}
