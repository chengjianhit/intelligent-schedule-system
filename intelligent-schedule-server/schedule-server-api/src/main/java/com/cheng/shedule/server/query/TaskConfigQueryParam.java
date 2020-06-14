package com.cheng.shedule.server.query;

import lombok.Data;


@Data
public class TaskConfigQueryParam extends PageQuery {
	/**
	 * 任务所属分组ID
	 */
	private Long groupId;

	/**
	 * 任务编码
	 */
	private String taskName;

	/**
	 * 任务状态
	 */
	private String taskState;

	/**
	 * 调度时间类型
	 */
	private String scheduleType;

	/**
	 * 任务优先级(1-10)
	 */
	private Byte priority;

	/**
	 * 是否允许并发执行
	 */
	private Byte parallel;
}
