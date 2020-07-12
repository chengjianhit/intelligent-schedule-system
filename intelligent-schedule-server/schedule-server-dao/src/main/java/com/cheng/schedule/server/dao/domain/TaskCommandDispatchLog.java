package com.cheng.schedule.server.dao.domain;

import lombok.Data;

import java.util.Date;

/**
 *@Description TODO
 *@Author beedoorwei
 *@Date 2019/6/19 20:22
 *@Version 1.0.0
 */
@Data
public class TaskCommandDispatchLog implements java.io.Serializable{
	/**
	 * 指令ID
	 */
	private Long commandId;
	/**
	 * 任务分组ID,取自task_config表
	 */
	private Long groupId;

	/**
	 * 任务类型
	 */
	private String commandType;

	/**
	 * 任务名称
	 */
	private String taskName;
	/**
	 * 任务ID
	 */
	private Long taskId;
	/**
	 * 调度ID
	 */
	private Long scheduleId;

	/**
	 * 待调度任务状态
	 */
	private String scheduleState;

	/**
	 * 当前调度发起host
	 */
	private String scheduleHost;
	/**
	 * 任务调度目标机器
	 */
	private String targetHost;
	/**
	 * 跟踪ID
	 */
	private String traceId;

	/**
	 * 调度发起时间
	 */
	private Date publishTime;

	/**
	 * 实际发起时间
	 */
	private Date startTime;
	/**
	 * 实际结束时间
	 */
	private Date endTime;

	/**
	 * 任务优先级(1-10)
	 */
	private Byte priority;

	/**
	 * 总条数
	 */
	private Long total;

	/**
	 * 成功条数
	 */
	private Long success;

	/**
	 * 失败条数
	 */
	private Long fail;

}
