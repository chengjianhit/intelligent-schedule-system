package com.cheng.schedule.server.dao.mapper;

import com.cheng.schedule.server.dao.domain.TaskGroup;
import com.cheng.schedule.server.dao.domain.TaskGroupExample;
import org.springframework.stereotype.Repository;

/**
 * TaskGroupDao继承基类
 */
@Repository
public interface TaskGroupDao extends MyBatisBaseDao<TaskGroup, Long, TaskGroupExample> {

	public String getLastestGroupCode();
}