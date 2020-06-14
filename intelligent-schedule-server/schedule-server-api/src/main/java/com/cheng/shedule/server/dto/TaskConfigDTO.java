package com.cheng.shedule.server.dto;


import lombok.Data;

import java.util.Date;


@Data
public class TaskConfigDTO implements java.io.Serializable{
	private Long id;

	/**
	 * 任务所属分组ID
	 */
	private Long groupId;

	/**
	 * 任务编码
	 */
	private String groupCode;

	/**
	 * 任务编码
	 */
	private String taskName;

	/**
	 * 任务上下文
	 */
	private String taskContext;

	/**
	 * 任务状态
	 */
	private String taskState;

	/**
	 * 任务执行器
	 */
	private String processor;

	/**
	 * 调度时间类型
	 */
	private String scheduleType;

	/**
	 * 调度时区
	 */
	private String timeZone;

	/**
	 * 任务调度表达式
	 */
	private String scheduleExpress;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 任务优先级(1-10)
	 */
	private Byte priority;

	/**
	 * 是否允许并发执行
	 */
	private Byte parallel;

	/**
	 *  running cmd id
	 */
	private Long runCmdId;

}
