package com.cheng.transport.netty;

import com.cheng.transport.entity.ScheduleTaskCommand;
import com.cheng.transport.serialization.MessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ServerChannelHandler extends ChannelInitializer<SocketChannel> {

    private MessageCodec messageCodec;

    private int idleTime;

    public ServerChannelHandler(MessageCodec messageCodec, int idleTime) {
        this.messageCodec = messageCodec;
        this.idleTime = idleTime;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, 0, 4))
                .addLast(new LengthFieldPrepender(4))
                .addLast(buildDecoder())
                .addLast(buildEncoder())
                .addLast(new IdleStateHandler(idleTime,0,0, TimeUnit.SECONDS))
                .addLast(new IntelligentTaskHandler());
    }


    private ChannelHandler buildEncoder() {
        MessageToByteEncoder messageToByteEncoder = new MessageToByteEncoder<ScheduleTaskCommand>(){
            @Override
            protected void encode(ChannelHandlerContext channelHandlerContext, ScheduleTaskCommand scheduleTaskMsg, ByteBuf byteBuf)
                    throws Exception {
                byte[] write =messageCodec.serializable(scheduleTaskMsg);
                byteBuf.writeBytes(write);
            }
        };
        return messageToByteEncoder;
    }

    private ChannelHandler buildDecoder() {
        ByteToMessageDecoder decoder = new ByteToMessageDecoder(){
            @Override
            protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)
                    throws Exception {
                final int len = byteBuf.readableBytes();
                byte[] bytes = new byte[len];
                byteBuf.getBytes(byteBuf.readerIndex(), bytes, 0, len);
                Object decode = messageCodec.deSerializable(bytes, Object.class);
                byteBuf.skipBytes(len);
                list.add(decode);
            }
        };
        return decoder;
    }
}
