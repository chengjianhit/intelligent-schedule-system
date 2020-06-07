package com.cheng.client.autoconfig;

import com.cheng.core.codec.ServerCodecService;
import com.cheng.core.processor.JobProcessorService;
import com.cheng.core.register.ServerRegisterService;
import com.cheng.core.service.ConnectionManagerService;
import com.cheng.core.service.MessageProcessService;
import com.cheng.core.service.ScheduleServerService;
import com.cheng.core.service.TaskProcessorService;
import com.cheng.core.store.SchedulePersistent;
import com.cheng.core.store.TaskExecRecordService;
import com.cheng.core.transport.ServerTransportService;
import com.cheng.core.util.ScheduleContextUtils;
import com.cheng.schedule.config.ScheduleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

@EnableConfigurationProperties(ScheduleClientAutoConfiguration.class)
public class ScheduleClientAutoConfiguration {

    @Autowired
    ScheduleServerConfig scheduleServerConfig;

    @Bean(initMethod = "buildDataSource")
    @ConditionalOnMissingBean
    public SchedulePersistent schedulePersistent() throws Exception{
        return new SchedulePersistent();
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskExecRecordService taskExecRecordService() {
        return new TaskExecRecordService();
    }

    /**
     * 使用方也会启动Server监听端口
     * @return
     */
    @Bean(initMethod = "startServer")
    @ConditionalOnMissingBean
    public ScheduleServerService scheduleServerService(){
        ScheduleServerService scheduleServerService = new ScheduleServerService();
        return scheduleServerService;
    }

    @Bean
    @ConditionalOnMissingBean
    public MessageProcessService messageProcessService() {
        return new MessageProcessService();
    }

    @Bean(initMethod = "init")
    @ConditionalOnMissingBean
    @DependsOn("messageProcessService")
    public TaskProcessorService taskProcessorService() {
        return new TaskProcessorService();
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

    @Bean
    @ConditionalOnMissingBean
    public JobProcessorService jobProcessorService() {
        JobProcessorService jobProcessorService = new JobProcessorService();
        return jobProcessorService;
    }

    @Bean(initMethod = "init")
    @ConditionalOnMissingBean
    @DependsOn({"serverCodecService", "serverTransportService", "serverRegisterService"})
    public ConnectionManagerService connectionManagerService() {
        return new ConnectionManagerService(false);
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerRegisterService serverRegisterService() {
        return new ServerRegisterService();
    }

    @Bean
    @ConditionalOnMissingBean
    public ScheduleContextUtils applicationContextUtils() {
        return new ScheduleContextUtils();
    }
}
