package io.netty.example.study.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.example.study.client.codec.*;
import io.netty.example.study.common.Operation;
import io.netty.example.study.common.order.OrderOperation;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ExecutionException;

public class ClientV1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(new NioEventLoopGroup());

        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast(new OrderFrameDecoder());
                pipeline.addLast(new OrderFrameEncoder());
                pipeline.addLast(new OrderProtocolEncoder());
                pipeline.addLast(new OrderProtocolDecoder());
                pipeline.addLast(new OperationToRequestMessageEncoder());
                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
            }
        });

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090);

        channelFuture.sync();
//        发送请求
        Operation operation = new OrderOperation(1001, "土豆");
        channelFuture.channel().writeAndFlush(operation);

        channelFuture.channel().closeFuture().get();

    }

}
