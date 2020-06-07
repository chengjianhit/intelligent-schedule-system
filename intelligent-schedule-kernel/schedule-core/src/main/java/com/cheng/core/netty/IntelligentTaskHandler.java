package com.cheng.core.netty;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.core.entity.ScheduleTaskCommand;
import com.cheng.core.enums.Command;
import com.cheng.core.service.MessageProcessService;
import com.cheng.core.util.ScheduleContextUtils;
import com.cheng.util.InetUtils;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.net.InetSocketAddress;

public class IntelligentTaskHandler extends ChannelDuplexHandler {

    private Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE", IntelligentTaskHandler.class);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ScheduleTaskCommand scheduleTaskCommand = (ScheduleTaskCommand) msg;
        if (scheduleTaskCommand.getCommand() == Command.PING){
            //pin消息不做任务处理
            logger.debug("receive ping msg from channel [{}] server [{}]", ctx.channel().id(), ctx.channel().remoteAddress());
            return;
        }
        logger.info("receive cid [{}] cmd [{}] node [{}] stid [{}] stepId [{}]", scheduleTaskCommand.getCommandId(), scheduleTaskCommand.getCommand(), scheduleTaskCommand.getCurrentNodeName(), scheduleTaskCommand.getScheduleTraceId(), scheduleTaskCommand.getStepTraceId());
        MessageProcessService messageProcessService = ScheduleContextUtils.getBean(MessageProcessService.class);
        messageProcessService.process(scheduleTaskCommand);
        super.channelRead(ctx, msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            String remoteAddress = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
            String localAddress = ((InetSocketAddress) ctx.channel().localAddress()).getAddress().getHostAddress();
            if (StringUtils.equals(remoteAddress, localAddress)
                    || StringUtils.equals(remoteAddress, InetUtils.getSelfIp())) {
                //TODO 自己不给自己发送心跳消息,一台机器部署多个实例的先不考虑
                return;
            }
            //空闲时间检测
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE){
                logger.warn("there is no msg from channel [{}] server [{}],close it", ctx.channel().id(), ctx.channel().remoteAddress());
                //没有收到客户端的消息
                ctx.channel().close();
            }
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(String.format("client connection [%s] ,channelId [%s] instance exception,close it ", ctx.channel().remoteAddress(), ctx.channel().id()), cause);
        ctx.channel().close();
        super.exceptionCaught(ctx, cause);
    }
}
