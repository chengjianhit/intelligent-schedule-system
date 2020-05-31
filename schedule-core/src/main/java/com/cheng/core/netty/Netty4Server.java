package com.cheng.core.netty;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.config.transport.Netty4Config;
import com.cheng.core.handler.ServerChannelHandler;
import com.cheng.core.codec.MessageCodec;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;

public class Netty4Server {
    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE",Netty4Server.class);

    private MessageCodec messageCodec;

    private Netty4Config netty4Config;

    NioEventLoopGroup bossGroup;
    NioEventLoopGroup workerGroup;

    public Netty4Server() {
    }

    /**
     * @return
     */
    public static Netty4Server defaultServer() {
        return new Netty4Server();
    }

    public Netty4Server configCodec(MessageCodec messageCodec){
        this.messageCodec = messageCodec;
        return this;
    }

    public Netty4Server configNetty4Config(Netty4Config netty4Config){
        this.netty4Config = netty4Config;
        return this;
    }

    public void buildServer() throws Exception{
        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("scheduleServerBoss", true));
        workerGroup = new NioEventLoopGroup(10, new DefaultThreadFactory("scheduleServerWorker", true));
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(NioChannelOption.SO_REUSEADDR, true)
                    .option(NioChannelOption.TCP_NODELAY, true)
                    .option(NioChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .option(NioChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ServerChannelHandler(messageCodec, netty4Config.getIdleTime()));
            Channel channel = serverBootstrap.bind(netty4Config.getServerPort()).sync().channel();
            logger.info("start schedule service success at port [{}]", netty4Config.getServerPort());
            addShutdownHolder();
        }catch (Throwable e){
            logger.error("build server error ", e);
            throw e;
        }
    }

    private void addShutdownHolder() {
        Runtime.getRuntime().addShutdownHook(
                new Thread(()->{
                    shutdown();
                })
        );
    }

    public void shutdown(){
        if (bossGroup != null){
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null){
            workerGroup.shutdownGracefully();
        }
    }
}
