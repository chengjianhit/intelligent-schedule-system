package com.cheng.schedule.config.register;

import lombok.Data;


@Data
public class RegisterConfig {
	/**
	 * 注册中心编码
	 */
	String code="nacos";

	/**
	 * 注册中心环境
	 */
	String registerEnv="DEVELOP";

	NacosRegister config;
}
