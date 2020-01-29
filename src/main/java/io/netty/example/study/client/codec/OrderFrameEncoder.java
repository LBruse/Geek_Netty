package io.netty.example.study.client.codec;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * 二次编码器
 */
public class OrderFrameEncoder extends LengthFieldPrepender {

    /**
     * 构造函数
     */
    public OrderFrameEncoder() {
        super(2);
    }

}
