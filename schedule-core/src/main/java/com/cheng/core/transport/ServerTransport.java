package com.cheng.core.transport;

import com.cheng.core.client.ConnectionInstance;

/**
 * Transport方式，目前只支持 Netty4，后续可支持其他方式
 */
public interface ServerTransport {

    /**
     * do register user
     * @return
     * @throws Exception
     */
    boolean startServer() throws Exception;

    void transportInit() throws Exception;

    /**
     * clean the connection
     * @throws Exception
     */
    public void cleanTransport()throws Exception;


    /**
     * do connect the server, Get ConnectInstance
     * @param remoteServer
     * @param port
     * @return
     */
    public ConnectionInstance connectServer(String remoteServer, int port);

    /**
     * return the transport port
     * @return
     */
    public int transportPort();
    /**
     * get the transport name
     * @return
     */
    public String transportType();
}
