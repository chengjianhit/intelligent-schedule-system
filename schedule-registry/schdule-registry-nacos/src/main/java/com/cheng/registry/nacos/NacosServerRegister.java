package com.cheng.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.client.naming.utils.StringUtils;
import com.cheng.core.register.ServerInstance;
import com.cheng.core.register.ServerRegister;
import com.cheng.core.register.listener.ServerStateChangeListener;
import com.cheng.core.transport.ServerTransport;
import com.cheng.core.transport.ServerTransportService;
import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.config.ScheduleServerConfig;
import com.cheng.schedule.config.register.NacosRegister;
import com.cheng.util.InetUtils;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class NacosServerRegister implements ServerRegister {

    private Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE",NacosServerRegister.class);

    private static String serviceNamePrefix = "com.cheng.schedule_server_group:%s";

    @Autowired
    private ScheduleServerConfig scheduleServerConfig;

    @Autowired
    private ServerTransportService serverTransportService;

    private NamingService namingService;

    @PostConstruct
    public void init() throws Exception{
        NacosRegister nacosConfig = scheduleServerConfig.getRegister().getConfig();
        Properties properties = new Properties();
        properties.setProperty("serverAddr", StringUtils.join(nacosConfig.getServerList(), ","));
        properties.setProperty("namespace", nacosConfig.getNamespace());

        namingService = NamingFactory.createNamingService(properties);
    }

    @Override
    public boolean register() throws Exception {
        ServerTransport serverTransport = serverTransportService.getServerTransport(scheduleServerConfig.getTransport().getCode());
        String selfIp = InetUtils.getSelfIp();
        AtomicBoolean result = new AtomicBoolean(true);
        scheduleServerConfig.getTask().getGroupList().stream()
                .forEach(group -> {
                    if (StringUtils.isNotEmpty(group)){
                        String serviceName= String.format(serviceNamePrefix,group);
                        try {
                            namingService.registerInstance(serviceName,
                                    scheduleServerConfig.getRegister().getRegisterEnv(), selfIp, serverTransport.transportPort());
                        } catch (NacosException e) {
                            logger.error("nacos registerInstance error is {} ", e);
                            result.set(false);
                        }
                        logger.info("server register success [{}] ", serviceName);

                    }
                });

        return result.get();
    }

    @Override
    public boolean unRegister() throws Exception {
        return false;
    }

    @Override
    public List<ServerInstance> getAllServer(String groupId) throws Exception {
        return null;
    }

    @Override
    public void addServerChangeListener(String groupId, ServerStateChangeListener serverStateChangeListener) throws Exception {

    }

    @Override
    public String registerCode() {
        return null;
    }
}
