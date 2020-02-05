package io.netty.example.study.server.codec.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * read idle check
 * 服务器10s接受不到channel的请求就断开连接
 * 保护自己、瘦身(及时清理空闲的连接)
 */
@Slf4j
public class ServerIdleCheckHandler extends IdleStateHandler {

    /**
     * 构造函数
     */
    public ServerIdleCheckHandler() {
        super(10, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
//        是否第一次READER_IDLE_STATE_EVENT触发
        if (evt == IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT) {
//            断开连接
            log.info("idle check happen, so close the connection");
            ctx.close();
            return;
        }
        super.channelIdle(ctx, evt);
    }

}
