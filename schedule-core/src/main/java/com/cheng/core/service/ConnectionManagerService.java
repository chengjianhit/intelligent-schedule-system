package com.cheng.core.service;

import com.cheng.core.register.ServerRegisterService;
import com.cheng.core.transport.ServerTransportService;
import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.config.ScheduleServerConfig;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.locks.ReentrantLock;

public class ConnectionManagerService {

    private Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE", ConnectionManagerService.class);

    @Autowired
    private ScheduleServerConfig scheduleServerConfig;
    @Autowired
    ServerRegisterService serverRegisterService;

    @Autowired
    ServerTransportService serverTransportService;

    private static ReentrantLock reentrantLock = new ReentrantLock(true);

}
