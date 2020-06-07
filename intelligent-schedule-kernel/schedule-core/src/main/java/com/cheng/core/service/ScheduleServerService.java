package com.cheng.core.service;

import com.cheng.core.register.ServerRegister;
import com.cheng.core.register.ServerRegisterService;
import com.cheng.core.transport.ServerTransport;
import com.cheng.core.transport.ServerTransportService;
import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.config.ScheduleServerConfig;
import com.cheng.schedule.config.register.RegisterConfig;
import com.cheng.schedule.config.transport.TransportConfig;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class ScheduleServerService {
    private Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE",ScheduleServerService.class);

    @Autowired
    private ScheduleServerConfig scheduleServerConfig;

    @Autowired
    ServerRegisterService serverRegisterService;

    @Autowired
    ServerTransportService serverTransportService;

    public void startServer() throws Exception{
        //start server
        TransportConfig transport = scheduleServerConfig.getTransport();

        ServerTransport serverTransport = serverTransportService.getServerTransport(transport.getCode());

        serverTransport.startServer();

        //register it self
        RegisterConfig register = scheduleServerConfig.getRegister();
        String registerType = register.getCode();
        ServerRegister serverRegister = serverRegisterService.getServerRegisterByCode(registerType);
        if (serverRegister == null) {
            logger.error("not found the server ");
            throw new RuntimeException("the register [" + registerType + "] cannot found ");
        }
        serverRegister.register();

        /**
         * 关闭应用时， 解除注册
         */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                serverTransport.cleanTransport();
                serverRegister.unRegister();
            } catch (Exception e) {
                logger.error("unRegister exception ", e);
            }
        }));

    }

}
