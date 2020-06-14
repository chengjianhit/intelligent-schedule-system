package com.cheng.shedule.server.enums;


public enum TaskCommandState {
	/**
	 * 初始化
	 */
	INIT((byte) 1, "NORMAL"),

	/**
	 * 待下发
	 */
	PUBLISHING((byte)2,"PUBLISHING"),

	/**
	 *下发失败
	 */
	PUBLISH_FAIL((byte)3,"PUBLISH_FAIL"),
	/**
	 * 运行中
	 */
	RUN((byte) 4, "RUN"),
	/**
	 * 暂停
	 */
	PAUSE((byte)5, "PAUSE"),
	/**
	 * 终止中
	 */
	STOPING((byte) 6, "STOPING"),
	/**
	 * 暴力终止，即所有任务都终止掉
	 */
	ABORT((byte)7, "ABORT"),
	/**
	 * 完成
	 */
	FINISH((byte)8, "FINISH"),

	;

	byte state;
	String desc;

	private TaskCommandState(byte state, String desc) {
		this.state = state;
		this.desc = desc;
	}
}
