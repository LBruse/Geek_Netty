package io.netty.example.study.server.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 一次解码器
 */
public class OrderFrameDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * 构造函数
     */
    public OrderFrameDecoder() {
        super(Integer.MAX_VALUE, 0, 2, 0, 2);
    }

}
