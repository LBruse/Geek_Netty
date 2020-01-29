package io.netty.example.study.client.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.study.common.ResponseMessage;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 二次解码器
 */
public class OrderProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out)
            throws Exception {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.decode(byteBuf);

//        将结果存放到outList中,一定要放!!!否则Netty以为没有解析出数据
        out.add(responseMessage);
    }

}
