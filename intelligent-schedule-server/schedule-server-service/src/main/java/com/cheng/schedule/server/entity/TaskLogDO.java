package com.cheng.schedule.server.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * task_log
 * @author
 */
@Data
public class TaskLogDO implements Serializable {
	private Long id;

	/**
	 * 任务配置ID
	 */
	private Long taskId;

	/**
	 * 指令ID
	 */
	private Long commandId;

	/**
	 * 任务分组号
	 */
	private String groupCode;

	private Long total;

	private Long success;

	private Long fail;

	/**
	 * 任务全局追踪ID
	 */
	private String traceId;

	/**
	 * 当前步骤编码（预留给多级任务使用）
	 */
	private String currentStepCode;

	/**
	 * 如果是多级任务，表示父任务ID（预留给多级任务使用）
	 */
	private Long parentTaskId;

	/**
	 * 调度中心ip
	 */
	private String scheduleServer;

	/**
	 * 当前步骤是否结束1是，0否
	 */
	private Byte isFinished;

	/**
	 * 任务开始时间
	 */
	private Date startTime;

	/**
	 * 结束时间
	 */
	private Date endTime;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;
}

