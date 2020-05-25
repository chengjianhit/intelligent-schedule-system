package com.cheng.core.client;

import com.cheng.core.entity.ScheduleTaskCommand;
import com.cheng.core.enums.ConnectionState;

public interface ConnectionInstance {

    /**
     *
     * @param data
     * @return
     * @throws Exception
     */
    boolean sendData(ScheduleTaskCommand data) throws Exception;

    /**
     * dispath task command to client
     * @param scheduleTaskMsg
     * @return
     */
    boolean sendCommand(ScheduleTaskCommand scheduleTaskMsg)throws Exception;

    /**
     *disconnectin from server
     * @return
     */
    boolean disConnection();

    /**
     * get the connection state
     * @return
     */
    ConnectionState getConnectionState();

    /**
     * trans to new state,if the old state is removed,then it will hold the REMOVED state for ever
     * @param newState
     */
    void stateTrans(ConnectionState newState);
    /**
     * get the remote server
     * @return
     */
    String getRemoteServer();

}
