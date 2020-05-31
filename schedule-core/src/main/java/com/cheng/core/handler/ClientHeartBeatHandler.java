package com.cheng.core.handler;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.core.entity.ScheduleTaskCommand;
import com.cheng.core.enums.Command;
import com.cheng.util.InetUtils;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.net.InetSocketAddress;

public class ClientHeartBeatHandler extends ChannelDuplexHandler {
    private Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE",ClientHeartBeatHandler.class);


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleState){
            String remoteAddress = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
            String localAddress = ((InetSocketAddress) ctx.channel().localAddress()).getAddress().getHostAddress();
            if(StringUtils.equals(remoteAddress,localAddress)
                    || StringUtils.equals(remoteAddress, InetUtils.getSelfIp())) {
                //TODO 自己不给自己发送心跳消息,一台机器部署多个实例的先不考虑
                return;
            }
        }

        ScheduleTaskCommand<Object> pingMsg = new ScheduleTaskCommand<>();
        pingMsg.setStartHost(InetUtils.getSelfIp());
        pingMsg.setCommand(Command.PING);
        ctx.channel().writeAndFlush(pingMsg);
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("heart beat exception ",cause);
        super.exceptionCaught(ctx, cause);
    }
}
