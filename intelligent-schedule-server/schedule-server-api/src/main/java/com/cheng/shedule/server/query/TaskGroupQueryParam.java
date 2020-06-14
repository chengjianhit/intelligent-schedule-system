package com.cheng.shedule.server.query;

import lombok.Data;


@Data
public class TaskGroupQueryParam extends PageQuery {
	/**
	 * 分组编码
	 */
	private String groupCode;

	/**
	 * 分组名称
	 */
	private String groupName;
}
