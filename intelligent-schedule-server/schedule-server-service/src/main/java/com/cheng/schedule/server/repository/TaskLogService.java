package com.cheng.schedule.server.repository;

import com.cheng.schedule.server.dao.domain.TaskLog;
import com.cheng.schedule.server.dao.mapper.TaskLogDao;
import com.cheng.schedule.server.entity.TaskLogDO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Author beedoorwei
 * @Date 2019/6/13 2:02
 * @Version 1.0.0
 */
@Component
public class TaskLogService {
    @Autowired
    private TaskLogDao taskLogDao;

    public boolean addTaskLog(TaskLogDO taskLogDO) {
        TaskLog taskLog = new TaskLog();
        BeanUtils.copyProperties(taskLogDO, taskLog);
        int i = taskLogDao.insertSelective(taskLog);
        taskLogDO.setId(taskLog.getId());
        return i > 0;
    }
}
