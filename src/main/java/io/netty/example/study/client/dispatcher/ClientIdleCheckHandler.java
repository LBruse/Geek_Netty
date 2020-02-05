package io.netty.example.study.client.dispatcher;

import io.netty.handler.timeout.IdleStateHandler;

/**
 * 客户端 write idle check
 * write idle check + keepalive - 客户端5秒不发送数据就发一个keepalive
 * 避免连接被断,启用不频繁的keepalive
 */
public class ClientIdleCheckHandler extends IdleStateHandler {

    /**
     * 构造函数
     */
    public ClientIdleCheckHandler() {
        super(0, 5, 0);
    }



}
