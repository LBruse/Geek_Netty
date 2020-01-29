package io.netty.example.study.client.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.study.common.RequestMessage;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 一次编码器
 */
public class OrderProtocolEncoder extends MessageToMessageEncoder<RequestMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RequestMessage requestMessage,
                          List<Object> out) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer();
        requestMessage.encode(byteBuf);

//        一定要把编码后的结果添加到outList!!!
        out.add(byteBuf);
    }

}
