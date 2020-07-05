package com.cheng.schedule.server.dao.mapper;

import com.cheng.schedule.server.dao.domain.TaskSchedule;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TaskScheduleDao {

    /**
     * lock task for schedule
     * @param paramsMap
     * @return
     */
    int lockWaitForSchedule(Map<String, Object> paramsMap);

    /**
     * get task for schedule
     * @param paramsMap
     * @return
     */
    List<Map<String,Object>> getWaitForSchedule(Map<String, Object> paramsMap);

    /**
     * update scheduleTime
     * @param taskSchedule
     * @return
     */
    int updateScheduleTime(TaskSchedule taskSchedule);


    /**
     * 非并发任务，锁定状态
     * @param scheduleId
     * @return
     */
    int lockRunningState(Map<String, Long> scheduleId);

    /**
     * clean task schedule lock
     */
    int cleanRunningLock(Long taskId);
}
