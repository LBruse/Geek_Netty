package io.netty.example.study.client.dispatcher;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.study.common.RequestMessage;
import io.netty.example.study.common.keepalive.KeepaliveOperation;
import io.netty.example.study.util.IdUtil;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * keepalive handler
 */
@ChannelHandler.Sharable
@Slf4j
public class KeepaliveHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        判断是否FIRST_WRITER_IDLE_STATE_EVENT
        if (evt == IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT) {
            log.info("write idle happen. so need to send keepalive to keep connection " +
                    "not closed by server.");
//            构建keepalive 消息
            KeepaliveOperation keepaliveOperation = new KeepaliveOperation();
            RequestMessage message = new RequestMessage(IdUtil.nextId(), keepaliveOperation);
//            发送keepalive 消息
            ctx.writeAndFlush(message);
        }
        super.userEventTriggered(ctx, evt);
    }

}
