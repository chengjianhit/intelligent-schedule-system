package com.cheng.shedule.server.api;


import com.cheng.shedule.server.common.AiResponse;

public interface TaskCommandRemoteService {

	/**
	 * 强制终止某个任务
	 * @param commandId
	 * @return
	 */
	AiResponse<Boolean> abortCommand(String userId, Long commandId);
	/**
	 * 强制终止某个任务
	 * @param commandId
	 * @return
	 */
	AiResponse<Boolean> copyCommand(String userId, Long commandId);
}
