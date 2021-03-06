package io.netty.example.study.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.example.study.server.codec.OrderFrameDecoder;
import io.netty.example.study.server.codec.OrderFrameEncoder;
import io.netty.example.study.server.codec.OrderProtocolDecoder;
import io.netty.example.study.server.codec.OrderProtocolEncoder;
import io.netty.example.study.server.codec.handler.MetricHandler;
import io.netty.example.study.server.codec.handler.OrderServerProcessHandler;
import io.netty.example.study.server.codec.handler.ServerIdleCheckHandler;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.IpSubnetFilterRule;
import io.netty.handler.ipfilter.RuleBasedIpFilter;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;

import java.util.concurrent.ExecutionException;

public class Server {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
        NioEventLoopGroup boss = new NioEventLoopGroup(0, new DefaultThreadFactory("boss"));
        NioEventLoopGroup worker = new NioEventLoopGroup(0, new DefaultThreadFactory("worker"));

        serverBootstrap.group(boss, worker);

//        禁止Nagle算法
        serverBootstrap.childOption(NioChannelOption.TCP_NODELAY, true);
//        设置TCP连接等待数量
        serverBootstrap.option(NioChannelOption.SO_BACKLOG, 1024);

        MetricHandler metricHandler = new MetricHandler();

        UnorderedThreadPoolEventExecutor business = new UnorderedThreadPoolEventExecutor(10,
                new DefaultThreadFactory("business"));

//        GlobalTrafficShapingHandler tsHandler = new GlobalTrafficShapingHandler(new NioEventLoopGroup(),
//                100 * 1024 * 1024, 100 * 1024 * 1024);


//        禁止本机客户端连接[黑名单]
        IpSubnetFilterRule ipSubnetFilterRule = new IpSubnetFilterRule(
                "127.0.0.1", 8, IpFilterRuleType.REJECT
        );

//        禁止127.0xxx的IP连接[黑名单,但是127.0.0.1可以通过]
//        IpSubnetFilterRule ipSubnetFilterRule = new IpSubnetFilterRule(
//                "127.0.0.1", 16, IpFilterRuleType.REJECT
//        );

        RuleBasedIpFilter ruleBasedIpFilter = new RuleBasedIpFilter(ipSubnetFilterRule);

        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
//                pipeline.addLast("loggingHandler debug",new LoggingHandler(LogLevel.DEBUG));

//                黑名单设置
                pipeline.addLast("ipFilter", ruleBasedIpFilter);

//                添加流量整形Handler
//                pipeline.addLast("TSHandler", tsHandler);
                pipeline.addLast("idleCheck", new ServerIdleCheckHandler());

                pipeline.addLast("frameDecoder", new OrderFrameDecoder());
                pipeline.addLast("orderFrameEncoder", new OrderFrameEncoder());
                pipeline.addLast("orderProtocolEncoder", new OrderProtocolEncoder());
                pipeline.addLast("orderProtocolDecoder", new OrderProtocolDecoder());
                pipeline.addLast("metricHandler", metricHandler);
                pipeline.addLast("loggingHandler", new LoggingHandler(LogLevel.INFO));

//                添加增强写Handler
//                pipeline.addLast("flushEnhance", new FlushConsolidationHandler(5,
//                        true));

                pipeline.addLast(business,
                        "orderServerProcessHandler", new OrderServerProcessHandler());
            }
        });

        ChannelFuture channelFuture = serverBootstrap.bind(8090).sync();

        channelFuture.channel().closeFuture().get();

    }

}
