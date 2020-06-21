package com.cheng.schedule.server.entity;

import lombok.Data;

import java.util.Date;


@Data
public class TaskScheduleDO {
	private Long id;

	/**
	 * 任务分组ID,取自task_config表
	 */
	private Long groupId;

	/**
	 * 任务ID
	 */
	private Long taskId;

	/**
	 * 任务执行器
	 */
	private String processor;

	/**
	 * 待调度任务状态
	 */
	private String scheduleState;

	/**
	 * 调度时间类型
	 */
	private String scheduleType;

	/**
	 * 时区
	 */
	private String timeZone;

	/**
	 * 调度时间表达
	 */
	private String scheduleExpress;

	/**
	 * 当前调度发起host，目前作为排它锁用，任务调度发起前先更新此字段为本机host，调度完毕后再更新回去
	 */
	private String scheduleHost;

	/**
	 * 上一次调度发起时间
	 */
	private Date preFireTime;

	/**
	 * 上一次调度任务执行时间
	 */
	private Integer preUseTime;

	/**
	 * 下次调度发起时间
	 */
	private Date nextFireTime;

	/**
	 * 是否删除
	 */
	private Boolean isDeleted;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 任务优先级(1-10)
	 */
	private Integer priority;

	/**
	 * 是否允许并发执行
	 */
	private Integer parallel;

	/**
	 * 正在运行的任务ID
	 */
	public Long runningCmdId;

	/**
	 * 任务上下文
	 */
	private String taskContext;
}
