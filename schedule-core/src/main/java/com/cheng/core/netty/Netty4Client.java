package com.cheng.core.netty;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.config.transport.Netty4Config;
import com.cheng.core.client.ConnectionInstance;
import com.cheng.core.enums.ConnectionState;
import com.cheng.core.handler.ClientChannelHandler;
import com.cheng.core.codec.MessageCodec;
import com.cheng.core.util.Netty4Cleaner;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

public class Netty4Client {

    private Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE",Netty4Client.class);

    private MessageCodec messageCodec;

    private Netty4Config netty4Config;

    Bootstrap bootstrap = null;

    EventLoopGroup eventExecutors;

    public static Netty4Client getInstance(){
        return new Netty4Client();
    }

    public Netty4Client configCodec(MessageCodec messageCodec){
        this.messageCodec = messageCodec;
        return this;
    }

    public Netty4Client configNetty4Config(Netty4Config netty4Config){
        this.netty4Config = netty4Config;
        return this;
    }

    /**
     * get a connected instance
     * @param remoteServer
     * @param port
     * @return
     */
    public ConnectionInstance connect(String remoteServer, int port){
        Netty4ConnectionInstance connectionInstance = new Netty4ConnectionInstance();
        connectionInstance.setRemoteServer(remoteServer);
        connectionInstance.setPort(port);

        /**
         * build connect instance
         */
        try {
            connectionInstance.stateTrans(ConnectionState.CONNECTING);
            Channel channel = doConnect(remoteServer, port, new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        logger.info("the server has been removed ,didn't try to reconnect [{}] [{}] ", remoteServer, port);
                        future.channel().close();
                    }

                    logger.warn("reconnect ");
                    connectionInstance.stateTrans(ConnectionState.RECONNECTING);
                    EventLoop eventExecutors = future.channel().eventLoop();
                    eventExecutors.schedule(() -> {
                        try {
                            Channel newChannel = Netty4Client.this.doConnect(remoteServer, port, this);
                            connectionInstance.stateTrans(ConnectionState.SUCCESS);
                            connectionInstance.setChannel(newChannel);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }, 1, TimeUnit.SECONDS);
                }
            });
            connectionInstance.setChannel(channel);
            return connectionInstance;
        }catch (Exception e){
            connectionInstance.stateTrans(ConnectionState.FAIL);
            logger.error(String.format("connect to server [%s],port [%s] exception", remoteServer, port), e);

        }

        return connectionInstance;
    }

    /**
     * get a real connect
     * @param remoteServer
     * @param port
     * @param connectionListener
     * @return
     */
    private Channel doConnect(String remoteServer, int port, ChannelFutureListener connectionListener) throws InterruptedException {
        Channel channel = bootstrap.connect(remoteServer, port).addListener(connectionListener).sync().channel();
        return channel;
    }


    /**
     * init a connection
     */
    public void init(){
        bootstrap = new Bootstrap();
        eventExecutors = new NioEventLoopGroup(10,
                new DefaultThreadFactory("scheduleClientWorker", true));
        bootstrap.group(eventExecutors).channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, netty4Config.getConnectionTimeOut())
                .handler(new ClientChannelHandler(messageCodec, netty4Config.getIdleTime()));

        addShutdownHolder();
    }

    /**
     * 添加回调函数
     */
    private void addShutdownHolder(){
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    logger.info("shutdown client ");
                    if (eventExecutors != null){
                     eventExecutors.shutdownGracefully();
                    }

                    Netty4Cleaner.clean(netty4Config.getCloseWait());
                })
        );
    }

}
