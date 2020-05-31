package com.cheng.core.netty;

import com.cheng.core.client.ConnectionInstance;
import com.cheng.core.codec.MessageCodec;
import com.cheng.core.codec.ServerCodecService;
import com.cheng.core.enums.TransportType;
import com.cheng.core.transport.ServerTransport;
import com.cheng.schedule.config.ScheduleServerConfig;
import com.cheng.schedule.config.transport.Netty4Config;
import org.springframework.beans.factory.annotation.Autowired;

public class Netty4Transport implements ServerTransport {

    /**
     * Intelligent智能调度平台 Server端信息
     */
    @Autowired
    private ScheduleServerConfig scheduleServerConfig;

    @Autowired
    private ServerCodecService serverCodecService;


    Netty4Client netty4Client;

    Netty4Server netty4Server;

    @Override
    public boolean startServer() throws Exception {
        Netty4Config netty4Config = scheduleServerConfig.getTransport().getConfig();
        MessageCodec codec = serverCodecService.getCodec(scheduleServerConfig.getCodec().getCode());
        Netty4Server netty4Server = Netty4Server.defaultServer()
                .configNetty4Config(netty4Config)
                .configCodec(codec);
        netty4Server.buildServer();
        return true;
    }

    /**
     * 初始化客户端
     * @throws Exception
     */
    @Override
    public void transportInit() throws Exception {
        Netty4Config netty4Config = scheduleServerConfig.getTransport().getConfig();
        MessageCodec messageCodec = serverCodecService.getCodec(scheduleServerConfig.getCodec().getCode());
        Netty4Client netty4Client = Netty4Client.getInstance()
                .configNetty4Config(netty4Config)
                .configCodec(messageCodec);
        netty4Client.init();

    }

    @Override
    public void cleanTransport() throws Exception {
        if (netty4Server != null){
            netty4Server.shutdown();
        }
    }

    @Override
    public ConnectionInstance connectServer(String remoteServer, int port) {
        return netty4Client.connect(remoteServer, port);
    }

    @Override
    public int transportPort() {
        return scheduleServerConfig.getTransport().getConfig().getServerPort();
    }

    @Override
    public String transportType() {
        return TransportType.NETTY4.getCode();    }
}
