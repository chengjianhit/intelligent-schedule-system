package com.cheng.server.autoconfig;

import com.cheng.core.codec.ServerCodecService;
import com.cheng.core.register.ServerRegisterService;
import com.cheng.core.service.ConnectionManagerService;
import com.cheng.core.service.ScheduleCommandService;
import com.cheng.core.transport.ServerTransportService;
import com.cheng.core.util.ScheduleContextUtils;
import com.cheng.schedule.config.ScheduleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

@EnableConfigurationProperties(ScheduleServerAutoConfiguration.class)
public class ScheduleServerAutoConfiguration {

    @Autowired
    ScheduleServerConfig scheduleServerConfig;

    @Bean
    @ConditionalOnMissingBean
    public ServerRegisterService serverRegisterService() {
        return new ServerRegisterService();
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerTransportService serverTransportService() {
        return new ServerTransportService();
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerCodecService serverCodecService() {
        return new ServerCodecService();
    }

    @Bean(initMethod = "init")
    @ConditionalOnMissingBean
    @DependsOn({"serverCodecService", "serverTransportService", "serverRegisterService"})
    public ConnectionManagerService connectionManagerService() {
        return new ConnectionManagerService(true);
    }

    @Bean
    @ConditionalOnMissingBean
    public ScheduleCommandService scheduleCommandService() {
        return new ScheduleCommandService();
    }

    @Bean
    @ConditionalOnMissingBean
    public ScheduleContextUtils applicationContextUtils() {
        return new ScheduleContextUtils();
    }
}
