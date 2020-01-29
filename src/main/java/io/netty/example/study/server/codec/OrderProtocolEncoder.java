package io.netty.example.study.server.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.study.common.ResponseMessage;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 一次编码器
 */
public class OrderProtocolEncoder extends MessageToMessageEncoder<ResponseMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseMessage responseMessage,
                          List<Object> out) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer();
        responseMessage.encode(byteBuf);

//        一定要把编码后的结果添加到outList!!!
        out.add(byteBuf);
    }

}
