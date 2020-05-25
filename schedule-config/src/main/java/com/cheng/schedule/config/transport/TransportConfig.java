package com.cheng.schedule.config.transport;

import lombok.Data;


@Data
public class TransportConfig {
	String code="netty4";
	Netty4Config config;
}
