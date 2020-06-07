package com.cheng.core.register;

import lombok.Data;


@Data
public class ServerInstance {
	public String server;
	public int port;


	public static ServerInstance defaultInstance() {
		return new ServerInstance();
	}

	public ServerInstance withServer(String server) {
		this.server = server;
		return this;
	}

	public ServerInstance withPort(int port ) {
		this.port = port;
		return this;
	}
}
