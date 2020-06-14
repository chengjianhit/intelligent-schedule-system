package com.cheng.shedule.server.api;


import com.cheng.shedule.server.common.AiPageResponse;
import com.cheng.shedule.server.common.AiResponse;
import com.cheng.shedule.server.dto.TaskConfigDTO;
import com.cheng.shedule.server.query.TaskConfigQueryParam;

public interface TaskConfigRemoteService {

	/**
	 * add new task config
     * @param userId the operator
	 * @param taskConfigDTO
	 * @return
	 */
	AiResponse<Boolean> addTask(String userId, TaskConfigDTO taskConfigDTO);

	/**
	 * 查询任务列表
	 * @param userId the operator
	 * @param taskConfigQueryParam
	 * @return
	 */
	AiPageResponse<TaskConfigDTO> queryTask(String userId, TaskConfigQueryParam taskConfigQueryParam);

	/**
	 * 修改任务
	 * @param userId the operator
	 * @param taskConfigDTO
	 * @return
	 */
	AiResponse<Boolean> modifyTask(String userId, TaskConfigDTO taskConfigDTO);

	/**
	 * @param taskId
	 * @param userId the operator
	 * @return
	 */
	AiResponse<Boolean> delTask(String userId, Long taskId);


}
