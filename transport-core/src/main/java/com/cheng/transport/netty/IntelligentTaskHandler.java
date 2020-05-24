package com.cheng.transport.netty;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.transport.entity.ScheduleTaskCommand;
import com.cheng.transport.enums.Command;
import com.cheng.transport.service.MessageProcessService;
import com.cheng.transport.util.ScheduleContextUtils;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;

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
        super.channelRead(ctx, msg);
    }
}
