package com.cheng.core.netty;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.core.client.ConnectionInstance;
import com.cheng.core.entity.ScheduleTaskCommand;
import com.cheng.core.enums.ConnectionState;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Data;
import org.slf4j.Logger;


@Data
public class Netty4ConnectionInstance implements ConnectionInstance {

    private Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE", Netty4ConnectionInstance.class);

    private String remoteServer;

    private int port;

    private Channel channel;

    private ConnectionState connectionState = ConnectionState.INIT;

    @Override
    public boolean sendData(ScheduleTaskCommand data) throws Exception {
        logger.debug("channel all status [{}] [{}] [{}] [{}] ", channel.isActive(), channel.isOpen(), channel.isRegistered(), channel.isWritable());
        if (!channel.isOpen() || !channel.isActive()){
            logger.error("server status is not correct ");
            return false;
        }
        ChannelFuture sync = channel.writeAndFlush(data).sync();
        return sync.isSuccess();
    }

    @Override
    public boolean sendCommand(ScheduleTaskCommand scheduleTaskMsg) throws Exception {
        logger.debug("channel all status [{}] [{}] [{}] [{}] ", channel.isActive(), channel.isOpen(), channel.isRegistered(), channel.isWritable());
        if (!channel.isOpen() || !channel.isActive()){
            logger.error("server status is not correct ");
            return false;
        }
        ChannelFuture sync = channel.writeAndFlush(scheduleTaskMsg).sync();
        return sync.isSuccess();
    }

    @Override
    public boolean disConnection() {
        try {
            ChannelFuture sync = channel.disconnect().sync();
            logger.info("disconnect from server [{}] result is [{}]", channel.remoteAddress(), sync.isSuccess());
            return sync.isSuccess();
        } catch (Throwable e) {
            logger.error(String.format("disconnect fail for server [%s] port [%s]", remoteServer, port), e);
        }
        return false;
    }

    @Override
    public ConnectionState getConnectionState() {
        return this.connectionState;
    }

    @Override
    public void stateTrans(ConnectionState newState) {
        if (connectionState != ConnectionState.REMOVED) {
            this.connectionState = newState;
        }
    }

    @Override
    public String getRemoteServer() {
        return remoteServer+"_"+port;
    }
}
