package com.cheng.shedule.server.api;


import com.cheng.shedule.server.common.AiPageResponse;
import com.cheng.shedule.server.common.AiResponse;
import com.cheng.shedule.server.dto.PermissionDTO;
import com.cheng.shedule.server.dto.TaskGroupDTO;
import com.cheng.shedule.server.query.TaskGroupQueryParam;

public interface TaskGroupRemoteService {

	/**
	 * add new group
	 * @param taskGroupDTO
	 * @return
	 */
	AiResponse<Boolean> addGroup(String userId, TaskGroupDTO taskGroupDTO);

	/**
	 * 查询分组列表
	 * @param taskGroupQueryParam
	 * @return
	 */
	AiPageResponse<TaskGroupDTO> queryGroup(String userId, TaskGroupQueryParam taskGroupQueryParam);

	/**
	 * 修改分组
	 * @param taskGroupDTO
	 * @return
	 */
	AiResponse<Boolean> modifyGroup(String userId, TaskGroupDTO taskGroupDTO);

	/**
	 * @param taskId
	 * @return
	 */
	AiResponse<Boolean> delGroup(String userId, Long taskId);


	/**
	 * 查询分组所有权限用户
	 * @param groupId
	 * @return
	 */
	AiPageResponse<String> queryGroupUser(String userId, Long groupId);

	/**
	 * 添加权限
	 * @param permissionDTO
	 * @return
	 */
	AiResponse<Boolean> addGroupUser(String userId, PermissionDTO permissionDTO);

	/**
	 * 删除用户权限
	 * @param userId
	 * @param permissionDTO
	 * @return
	 */
	AiResponse<Boolean> delGroupUser(String userId, PermissionDTO permissionDTO);
}
