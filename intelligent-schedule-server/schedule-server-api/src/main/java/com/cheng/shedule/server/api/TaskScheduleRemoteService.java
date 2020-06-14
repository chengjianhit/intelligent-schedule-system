package com.cheng.shedule.server.api;


import com.cheng.shedule.server.common.AiPageResponse;
import com.cheng.shedule.server.common.AiResponse;
import com.cheng.shedule.server.dto.TaskScheduleDTO;
import com.cheng.shedule.server.query.TaskScheduleQueryParam;

public interface TaskScheduleRemoteService {

	/**
	 * 暂停调度
	 * @param userId
	 * @param taskId
	 * @return
	 */
	AiResponse<Boolean> pauseSchedule(String userId, Long taskId);
	/**
	 * 恢复调度
	 * @param userId
	 * @param taskId
	 * @return
	 */
	AiResponse<Boolean> resumeSchedule(String userId, Long taskId);
	/**
	 * query the task schedule list
	 * @param taskScheduleQueryParam
	 * @return
	 */
	AiPageResponse<TaskScheduleDTO> queryTaskSchedule(String userId, TaskScheduleQueryParam taskScheduleQueryParam);

	/**
	 * query one item
	 * @param userId
	 * @param commandId
	 * @return
	 */
	AiResponse<TaskScheduleDTO> queryTaskProcess(String userId, Long commandId);

	/**
	 * clean the parallel lock for no parallel task
	 * @param userId
	 * @param taskId
	 * @return
	 */
	AiResponse<Boolean> cleanRunningLock(String userId, Long taskId);
}
