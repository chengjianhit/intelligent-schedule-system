package com.cheng.core.enums;


public enum TransportType {
	NETTY4("netty4");
	String code;

	TransportType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
