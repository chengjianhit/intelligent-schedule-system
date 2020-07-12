package com.cheng.schedule.server.service;

import com.alibaba.fastjson.JSON;
import com.cheng.core.enums.Command;
import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.entity.TaskCommandDO;
import com.cheng.schedule.server.entity.TaskScheduleDO;
import com.cheng.schedule.server.repository.TaskCommandService;
import com.cheng.schedule.server.repository.TaskScheduleService;
import com.cheng.schedule.server.trigger.Trigger;
import com.cheng.schedule.server.util.RunState;
import com.cheng.shedule.server.enums.TaskCommandState;
import com.cheng.util.InetUtils;
import com.cheng.util.TimeUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

    @Autowired
    private TaskCommandService taskCommandService;


    @Autowired
    private TaskTriggerService taskTriggerService;


    /**
     * 外部传入并发阻塞队列，将需要调度的任务put到队列里面
     *
     * @param scheduleQueue
     */
    public void lockAndGetTask(BlockingQueue<TaskScheduleDO> scheduleQueue) {
        logger.info("generate schedule task run");
        List<TaskScheduleDO> taskScheduleDOList = taskScheduleService.lockGetForSchedule();
        AtomicInteger cnt = new AtomicInteger(0);
        while (CollectionUtils.isNotEmpty(taskScheduleDOList)) {
            if (!RunState.canRun()) {
                logger.warn("the run state can't run ");
                return;
            }

            taskScheduleDOList.stream().forEach(taskScheduleDO -> {
                //需要调度的任务，本地缓存保存一份
                boolean b = scheduleCacheService.addGenerateQueue(taskScheduleDO.getId());
                if (b) {
                    cnt.incrementAndGet();
                    try {
                        scheduleQueue.put(taskScheduleDO);
                    } catch (InterruptedException e) {
                        logger.error("put data to the schedule queue exception ", e);
                    }
                }
            });
            //循环获取需要执行的任务，sleep 1 毫秒，让出CPU
            taskScheduleDOList = taskScheduleService.lockGetForSchedule();
            TimeUtils.sleep(1);
        }

        logger.info("this batch run finish ,all generate is [{}]", cnt);

    }

    /**
     * process the blocking queue
     *
     * @param scheduleQueue
     */
    public void process(BlockingQueue<TaskScheduleDO> scheduleQueue) throws InterruptedException {
        TaskScheduleDO taskScheduleDO = null;
        while ((taskScheduleDO = scheduleQueue.take()) != null) {
            logger.debug("process one task to schedule {} ", taskScheduleDO.getId());
            realProcessSchedule(taskScheduleDO);
        }
    }

    /**
     * real process a taskSchedule item
     *
     * @param taskScheduleDO
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    private void realProcessSchedule(TaskScheduleDO taskScheduleDO) {
        Trigger trigger = taskTriggerService.getTrigger(taskScheduleDO.getScheduleType());
        Date date = new Date(System.currentTimeMillis());
        Date nextFireTime = trigger.getNextFireTime(taskScheduleDO);
        //如果下一次要调度的时间，比当前时间早，则现在就执行
        if (nextFireTime.before(date)) {
            nextFireTime = date;
        }
        //先对数据库记录进行更新，schedule_host进行重置
        boolean b = taskScheduleService.updateScheduleTime(taskScheduleDO.getId(), nextFireTime);
        if (!b) {
            logger.error("update task schedule info error ");
            return;
        }
        //将生成的TaskSchedule转换成 TaskCommadnDO,插入task_command表中，进行dispatch调度
        TaskCommandDO taskCommandDO = buildTaskCommand(taskScheduleDO, nextFireTime);
        boolean addSuccess = taskCommandService.addCommand(taskCommandDO);
        logger.info("add task command result [{}] context [{}]", addSuccess, JSON.toJSONString(taskCommandDO));

    }

    /**
     * TaskScheduleDO ——》 TaskCommandDO
     * @param taskScheduleDO
     * @param nextFireTime
     * @return
     */
    private TaskCommandDO buildTaskCommand(TaskScheduleDO taskScheduleDO, Date nextFireTime) {
        TaskCommandDO taskCommandDO = new TaskCommandDO();
        taskCommandDO.setCommand(Command.SCHEDULE.name());
        taskCommandDO.setContext(taskScheduleDO.getTaskContext());
        taskCommandDO.setGroupId(taskScheduleDO.getGroupId());
        taskCommandDO.setScheduleHost(InetUtils.getSelfIp());
        taskCommandDO.setScheduleId(taskScheduleDO.getId());
        taskCommandDO.setIsDeleted(false);
        taskCommandDO.setPublishTime(nextFireTime);
        taskCommandDO.setCreateTime(new Date(System.currentTimeMillis()));
        taskCommandDO.setTaskId(taskScheduleDO.getTaskId());
        taskCommandDO.setProcessor(taskScheduleDO.getProcessor());
        taskCommandDO.setParallel(taskScheduleDO.getParallel() != null ? taskScheduleDO.getParallel().byteValue() : 0);
        taskCommandDO.setPriority(taskScheduleDO.getPriority() != null ? taskScheduleDO.getPriority().byteValue() : 0);
        taskCommandDO.setState(TaskCommandState.INIT.name());
        return taskCommandDO;
    }




}
