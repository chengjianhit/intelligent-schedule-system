package com.cheng.schedule.server.dao.mapper;

import com.cheng.schedule.server.dao.domain.TaskCommand;
import com.cheng.schedule.server.dao.domain.TaskCommandDispatchLog;
import com.cheng.schedule.server.dao.domain.TaskCommandExample;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * TaskCommandDao继承基类
 */
@Repository
public interface TaskCommandDao extends MyBatisBaseDao<TaskCommand, Long, TaskCommandExample> {

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	int lockTaskCommand(Map<String, Object> paramsMap);

	List<TaskCommand> getWaitForCommand(Map<String, Object> paramsMap);

	void resetLockInfo(Map<String, Object> paramsMap);


	/**
	 * 查询调度进度记录
	 * @param param
	 * @return
	 */
	List<TaskCommandDispatchLog> queryCommandDispatchState(Map<String, Object> param);

	/**
	 *
	 * @param param
	 */
	Long queryTaskScheduleLogCount(Map<String, Object> param);
}