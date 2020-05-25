package com.cheng.schedule.config.register;

import lombok.Data;

import java.util.List;


@Data
public class NacosRegister {
	private String namespace;
	private List<String> serverList;
}
