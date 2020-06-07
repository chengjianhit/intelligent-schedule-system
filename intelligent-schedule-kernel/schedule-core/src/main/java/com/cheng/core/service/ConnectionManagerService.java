package com.cheng.core.service;

import com.alibaba.fastjson.JSON;
import com.cheng.core.client.ConnectionInstance;
import com.cheng.core.client.ConnectionProxy;
import com.cheng.core.enums.ConnectionState;
import com.cheng.core.register.ServerInstance;
import com.cheng.core.register.ServerRegister;
import com.cheng.core.register.ServerRegisterService;
import com.cheng.core.register.listener.ServerStateChangeListener;
import com.cheng.core.transport.ServerTransport;
import com.cheng.core.transport.ServerTransportService;
import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.config.ScheduleServerConfig;
import com.cheng.schedule.config.task.TaskConfig;
import com.cheng.schedule.config.transport.TransportConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ConnectionManagerService {

    private Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE", ConnectionManagerService.class);

    @Autowired
    private ScheduleServerConfig scheduleServerConfig;
    @Autowired
    ServerRegisterService serverRegisterService;

    @Autowired
    ServerTransportService serverTransportService;

    private static ReentrantLock reentrantLock = new ReentrantLock(true);

    /**
     * 是否允许迟延创建连接
     */
    private boolean lazyInitConnect = true;

    public ConnectionManagerService(boolean lazyInitConnect) {
        this.lazyInitConnect = lazyInitConnect;
    }

    /**
     * data module
     * group:
     * serverIp:instance
     */
    Map<String, List<String>> groupConnectionKeyMap = Maps.newConcurrentMap();

    Map<String, ConnectionProxy> connectionInstanceMap = Maps.newConcurrentMap();


    public void init() throws Exception{
        addCleanConnection();
        clientInit();
        registerConnection();
        logger.info("connection init suc lazyInit {}", lazyInitConnect);

    }

    /**
     * core logic is that ,first mark if there is not access for this connection
     * when the mark time past 4 minutes but there is no invoke ,clean it
     * clean connection
     */
    private void addCleanConnection() {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern("connection-clean-%d").priority(1).daemon(true).build());
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(() -> {
            Iterator<String> iterator = connectionInstanceMap.keySet().iterator();
            while (iterator.hasNext()) {
                String serverKey = iterator.next();
                ConnectionProxy connectionInstance = connectionInstanceMap.get(serverKey);
                long l = System.currentTimeMillis() - connectionInstance.getLastVisit();
                if (l > 1000 * 60 * 5) {
                    if (connectionInstance.getMarkTime() == 0) {
                        connectionInstance.setMarkTime(connectionInstance.getLastVisit());
                    } else {
                        long l1 = connectionInstance.getLastVisit() - connectionInstance.getMarkTime();
                        if (l1 > 1000 * 60 * 4) {
                            //清理连接
                            logger.info("there is no invoke on this connection {} ,remove it ", serverKey);
                            groupConnectionKeyMap.keySet().stream().forEach(s -> {
                                if (StringUtils.isNotEmpty(s)) {
                                    List<String> strings = groupConnectionKeyMap.get(s);
                                    if (strings != null && strings.contains(serverKey)) {
                                        doRemoveConnection(serverKey);
                                        strings.remove(serverKey);
                                    }
                                }
                            });
                        }
                    }
                } else {
                    connectionInstance.setMarkTime(0L);
                }
            }
        }, 0, 5, TimeUnit.MINUTES);
    }

    /**
     * init all connection
     */
    private void registerConnection() {
        TaskConfig taskConfig = scheduleServerConfig.getTask();
        Set<String> groupSet = Sets.newHashSet();
        if (CollectionUtils.isEmpty(taskConfig.getGroupList())) {
            return;
        }
        taskConfig.getGroupList().stream().forEach(group -> {
            if (StringUtils.isNotEmpty(group)) {
                groupSet.add(group);
            }
        });
        //init connect
        for (String group : groupSet) {
            initGroup(group);
            logger.info("group {} init suc", group);
        }
    }

    /**
     * client init
     */
    public void clientInit() throws Exception {
        TransportConfig transport = scheduleServerConfig.getTransport();
        ServerTransport serverTransport = serverTransportService.getServerTransport(transport.getCode());
        serverTransport.transportInit();
    }



    /**
     * get one connection instance
     *
     * @param group
     * @return
     */
    public ConnectionInstance selectInstance(String group) {
        return selectInstance(group, null);
    }

    /**
     * get one connection instance
     *
     * @param group
     * @return
     */
    public ConnectionInstance selectInstance(String group, String target) {
        if (!groupConnectionKeyMap.containsKey(group)) {
            initGroup(group);
        }
        List<String> connectionKeyList = groupConnectionKeyMap.get(group);
        if (CollectionUtils.isEmpty(connectionKeyList)) {
            return null;
        }
        String connectionKey = target;
        if (StringUtils.isEmpty(connectionKey)) {
            int selectIdx = RandomUtils.nextInt(0, connectionKeyList.size());
            connectionKey = connectionKeyList.get(selectIdx);
        }
        if (!connectionInstanceMap.containsKey(connectionKey)) {
            initConnection(connectionKey);
        }
        logger.info("select server {}", connectionKey);
        return connectionInstanceMap.get(connectionKey);
    }

    /**
     * get all connection of specific group
     * @param group
     * @return
     */
    public List<ConnectionInstance> selectAllInstance(String group) {
        if (!groupConnectionKeyMap.containsKey(group)) {
            initGroup(group);
        }
        List<String> connectionKey = groupConnectionKeyMap.get(group);
        if (CollectionUtils.isEmpty(connectionKey)) {
            return Lists.newArrayList();
        }
        List<ConnectionInstance> result = Lists.newArrayList();
        connectionKey.stream().forEach(s -> {
            ConnectionProxy connectionProxy = connectionInstanceMap.get(s);
            if (connectionProxy != null) {
                result.add(connectionProxy);
            }
        });
        return result;
    }


    /**
     * init one single group
     */
    public void initGroup(String group){
        reentrantLock.lock();
        try{
            if (groupConnectionKeyMap.containsKey(group)){
                return;
            }

            String registryCode = scheduleServerConfig.getRegister().getCode();
            ServerRegister serverRegister = serverRegisterService.getServerRegisterByCode(registryCode);
            List<ServerInstance> allServer = serverRegister.getAllServer(group);
            if (CollectionUtils.isEmpty(allServer)){
                logger.warn("there is no active server in group [{}] ", group);
                return;
            }
            List<String> serverKeyList = Lists.newArrayList();

            List<String> ipPortList = allServer.stream().map(server ->
                    getServerKey(server)).collect(Collectors.toList());

            //与group对应的每个客户端服务器创建连接
            ipPortList.stream().forEach(ipPort -> {
                if (!serverKeyList.contains(ipPort)){
                    serverKeyList.add(ipPort);
                }
                //如果没有延迟加载，直接初始化
                if (!lazyInitConnect){
                    initConnection(ipPort);
                }
            });

            logger.info("group [{}] has [{}] server instance ", group, JSON.toJSONString(serverKeyList));
            groupConnectionKeyMap.put(group, serverKeyList);
            serverRegister.addServerChangeListener(group, new ServerStateChangeListener() {

                @Override
                public void serverRemoved(ServerInstance serverInstance) {
                    logger.info("group server {} instance removed ,add new instance [{}] ", group, JSON.toJSONString(serverInstance));
                    groupConnectionKeyMap.get(group).remove(getServerKey(serverInstance));
                    doRemoveConnection(getServerKey(serverInstance));
                }

                @Override
                public void serverAdd(ServerInstance serverInstance) {
                    logger.info("group server {} instance add ,remove instance [{}] ", group, JSON.toJSONString(serverInstance));
                    addServerInstance(group, getServerKey(serverInstance));
                }

                @Override
                public void refreshAll(List<ServerInstance> serverInstanceList) {
                    logger.info("group server {} instance refresh,all server instance [{}] ", group, JSON.toJSONString(serverInstanceList));
                    doRefreshInstance(group, serverInstanceList);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }


    }

    /**
     * add new ServerInstance Connection
     * @param group
     * @param serverKey
     */
    private void addServerInstance(String group, String serverKey) {
       reentrantLock.lock();
       try {
           if (!groupConnectionKeyMap.containsKey(group)) {
               groupConnectionKeyMap.put(group, Lists.newArrayList());
           }
           groupConnectionKeyMap.get(group).add(serverKey);
           if (!lazyInitConnect) {
               initConnection(serverKey);
           }

       }finally {
           reentrantLock.unlock();
       }
    }


    /**
     * init connection
     * @param serverKey
     * @return
     */
    private ConnectionInstance initConnection(String serverKey){
        reentrantLock.lock();
        try {
            ServerTransport serverTransport = serverTransportService.getServerTransport(
                    scheduleServerConfig.getTransport().getCode());
            if (connectionInstanceMap.containsKey(serverKey)){
                return connectionInstanceMap.get(serverKey);
            }
            String[] s = serverKey.split("_");
            ConnectionInstance connectionInstance = serverTransport.connectServer(s[0], Integer.parseInt(s[1]));
            logger.info("init connection to server [{}] result [{}]", serverKey, connectionInstance != null);
            if (connectionInstance != null){
                connectionInstanceMap.put(serverKey, ConnectionProxy.proxy(connectionInstance));
            }
            return connectionInstanceMap.get(serverKey);
        } catch (Throwable e) {
            logger.info("init connection to server [" + serverKey + "] exception", e);
            return null;
        }finally {
            reentrantLock.unlock();
        }
    }

    /**
     * refresh all the connection instance
     * @param groupId
     * @param serverInstanceList
     */
    private void doRefreshInstance(String groupId, List<ServerInstance> serverInstanceList) {
        reentrantLock.lock();
        try {
            //将最新的serverInstanceList全部更新到connectionInstanceMap中
            Map<String, ServerInstance> newServerInstanceMap = Maps.newHashMap();
            if (CollectionUtils.isNotEmpty(serverInstanceList)){
                serverInstanceList.stream().forEach(instance ->
                        newServerInstanceMap.put(getServerKey(instance), instance));
            }
            //1、移除connectionInstanceMap中的instance
            List<String> connectionList = groupConnectionKeyMap.get(groupId);
            Iterator<String> iterator = connectionList.iterator();
            while (iterator.hasNext()){
                String serverKey = iterator.next();
                if (!newServerInstanceMap.containsKey(serverKey)){
                    doRemoveConnection(serverKey);
                    iterator.remove();
                }
            }
            //2、将newServerInstanceMap中的instance添加到groupConnectionKeyMap
            newServerInstanceMap.keySet().stream().forEach(server ->{
                if (!connectionList.contains(server)){
                    connectionList.add(server);
                    if(!lazyInitConnect){
                        initConnection(server);
                    }
                }
            });
        }catch (Exception e){
            logger.error("refresh instance exception " + groupId, e);
        }
        finally {
            reentrantLock.unlock();
        }
        logger.info("group [{}] refresh instance remain [{}]", groupId, connectionInstanceMap.size());

    }

    /**
     * 关闭对应服务器的连接
     * @param serverKey
     */
    private void doRemoveConnection(String serverKey){
        reentrantLock.lock();
        try{
            ConnectionInstance connectionInstance = connectionInstanceMap.get(serverKey);
            if (connectionInstance != null){
                connectionInstance.stateTrans(ConnectionState.REMOVED);
                boolean b = connectionInstance.disConnection();
                connectionInstanceMap.remove(serverKey);
                logger.info("remove instance [{}] result [{}]", serverKey, b);
            }
        }catch (Throwable e){
            logger.error("remove connection exception " + serverKey, e);

        }
        finally {
            reentrantLock.unlock();
        }
    }
    /**
     * get server key
     *
     * @param serverInstance
     * @return
     */
    private String getServerKey(ServerInstance serverInstance) {
        return String.format("%s_%s", serverInstance.getServer(), serverInstance.getPort());
    }

}
