package com.cheng.schedule.server.dao.mapper;

import com.cheng.schedule.server.dao.domain.TaskLog;
import com.cheng.schedule.server.dao.domain.TaskLogExample;
import org.springframework.stereotype.Repository;

/**
 * TaskLogDao继承基类
 */
@Repository
public interface TaskLogDao extends MyBatisBaseDao<TaskLog, Long, TaskLogExample> {
}