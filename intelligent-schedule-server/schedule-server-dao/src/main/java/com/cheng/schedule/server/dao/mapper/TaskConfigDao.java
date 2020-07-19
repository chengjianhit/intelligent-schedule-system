package com.cheng.schedule.server.dao.mapper;

import com.cheng.schedule.server.dao.domain.TaskConfig;
import com.cheng.schedule.server.dao.domain.TaskConfigExample;
import org.springframework.stereotype.Repository;

/**
 * TaskConfigDao继承基类
 */
@Repository
public interface TaskConfigDao extends MyBatisBaseDao<TaskConfig, Long, TaskConfigExample> {
}