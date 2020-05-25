package com.cheng.core.handler;

import com.cheng.core.entity.ScheduleTaskCommand;
import com.cheng.core.codec.MessageCodec;
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

public class ClientChannelHandler extends ChannelInitializer<SocketChannel> {

    private MessageCodec messageCodec;
    private int heartInterval;

    public ClientChannelHandler(MessageCodec messageCodec, int heartInterval) {
        this.messageCodec = messageCodec;
        this.heartInterval = heartInterval;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024*1024,0,4,0,4));
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(buildDecoder());
        pipeline.addLast(buildEncoder());

        pipeline.addLast(new IdleStateHandler(0, heartInterval>1?heartInterval-1:0,0, TimeUnit.SECONDS));
        pipeline.addLast(new ClientHeartBeatHandler());

    }

    private ChannelHandler buildDecoder() {
        ByteToMessageDecoder decoder = new ByteToMessageDecoder() {
            @Override
            protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
                final int len = in.readableBytes();
                byte[] bytes = new byte[len];
                in.getBytes(in.readerIndex(), bytes, 0 , len);
                Object object = messageCodec.deSerializable(bytes, Object.class);
                in.skipBytes(len);
                out.add(object);
            }
        };

        return decoder;
    }

    private ChannelHandler buildEncoder() {
        MessageToByteEncoder encoder = new MessageToByteEncoder<ScheduleTaskCommand>() {
            @Override
            protected void encode(ChannelHandlerContext ctx, ScheduleTaskCommand msg, ByteBuf out) throws Exception {
                byte[] write = messageCodec.serializable(msg);
                out.writeBytes(write);
            }
        };

        return encoder;

    }
}
