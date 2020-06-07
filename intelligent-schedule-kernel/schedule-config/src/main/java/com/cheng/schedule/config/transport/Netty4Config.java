package com.cheng.schedule.config.transport;

import lombok.Data;


@Data
public class Netty4Config {
	int serverPort=9006 ;
	int idleTime=10;
	int connectionTimeOut=5000;
	//kill时，等待多久关闭
	int closeWait=5;
}
