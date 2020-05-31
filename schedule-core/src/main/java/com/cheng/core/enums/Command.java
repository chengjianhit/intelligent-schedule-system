package com.cheng.core.enums;


public enum Command {

	/**
	 * 心跳消息
	 */
	PING,
	/**
	 * 第一次调度
	 */
	SCHEDULE,
	/**
	 * 正常调度后取值
	 */
	RUN,
	/**
	 * 服务端下发任务终止指令
	 */
	SCHEDULE_STOP,
	/**
	 * 任务终止
	 */
	STOP;
}
