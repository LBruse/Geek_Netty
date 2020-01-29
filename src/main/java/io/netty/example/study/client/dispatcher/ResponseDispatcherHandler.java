package io.netty.example.study.client.dispatcher;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.example.study.common.ResponseMessage;

public class ResponseDispatcherHandler extends SimpleChannelInboundHandler<ResponseMessage> {

    private RequestPendingCenter requestPendingCenter;

    /**
     * 构造函数
     *
     * @param requestPendingCenter
     */
    public ResponseDispatcherHandler(RequestPendingCenter requestPendingCenter) {
        this.requestPendingCenter = requestPendingCenter;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ResponseMessage responseMessage)
            throws Exception {
        requestPendingCenter.set(responseMessage.getMessageHeader().getStreamId(),
                responseMessage.getMessageBody());
    }

}
