package com.cheng.core.client;

import com.cheng.core.entity.ScheduleTaskCommand;
import com.cheng.core.enums.ConnectionState;

/**
 * ConnectionInstance的代理
 */
public class ConnectionProxy implements ConnectionInstance{

    private ConnectionInstance instance;

    Long lastVisit = 0L;
    Long markTime = 0L;

    public Long getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Long lastVisit) {
        this.lastVisit = lastVisit;
    }

    public Long getMarkTime() {
        return markTime;
    }

    public void setMarkTime(Long markTime) {
        this.markTime = markTime;
    }

    public static ConnectionProxy proxy(ConnectionInstance connectionInstance){
        ConnectionProxy proxy = new ConnectionProxy();
        proxy.instance = connectionInstance;
        return proxy;
    }

    @Override
    public boolean sendData(ScheduleTaskCommand data) throws Exception {
        return false;
    }

    @Override
    public boolean sendCommand(ScheduleTaskCommand scheduleTaskMsg) throws Exception {
        return false;
    }

    @Override
    public boolean disConnection() {
        return false;
    }

    @Override
    public ConnectionState getConnectionState() {
        return null;
    }

    @Override
    public void stateTrans(ConnectionState newState) {

    }

    @Override
    public String getRemoteServer() {
        return null;
    }
}
