package com.cheng.shedule.server.dto;

import lombok.Data;

import java.util.Date;


@Data
public class TaskGroupDTO implements java.io.Serializable{
	private Long id;
	/**
	 * 分组编码
	 */
	private String groupCode;

	/**
	 * 分组名称
	 */
	private String groupName;

	/**
	 * 描述
	 */
	private String desc;

	/**
	 * 归属应用信息{appId:xxx,appName:xxx}
	 */
	private String appInfo;

	/**
	 * 创建时间
	 */
	private Date createTime;

}
