package com.cheng.schedule.server.entity;

import lombok.Data;

import java.util.Date;


@Data
public class TaskCommandDO {

	private Long id;

	/**
	 * 待下发指令，调度、暂停、恢复、优雅停止、强制停止
	 */
	private String command;

	/**
	 * 发起指令下发HOST
	 */
	private String publishHost;

	/**
	 * 待下发时间
	 */
	private Date publishTime;

	/**
	 * 发起调度IP
	 */
	private String scheduleHost;

	/**
	 * 任务执行器
	 */
	private String processor;

	/**
	 * 分组ID
	 */
	private Long groupId;

	/**
	 * 任务ID
	 */
	private Long taskId;

	/**
	 * 调度ID
	 */
	private Long scheduleId;

	/**
	 * 状态,待下发，已取消、下发中、执行中、执行完成、执行失败
	 */
	private String state;

	/**
	 * 下发目标HOST
	 */
	private String targetHost;

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
	private Byte priority;

	/**
	 * 是否允许并发执行
	 */
	private Byte parallel;

	/**
	 * 下发指令上下文
	 */
	private String context;

    /**
     * 原引用的commandId，任务终止，暂停，恢复需要指向原任务
     */
    private Long refCommandId;
}
