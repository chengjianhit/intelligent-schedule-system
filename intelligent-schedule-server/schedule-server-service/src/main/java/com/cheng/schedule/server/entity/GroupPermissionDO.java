package com.cheng.schedule.server.entity;

import lombok.Data;

import java.util.Date;


@Data
public class GroupPermissionDO {
	private Long id;

	/**
	 * 分组id
	 */
	private Long groupId;

	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 用户名称
	 */
	private String userName;

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
	 * 是否管理员
	 */
	private Boolean isAdmin;
}
