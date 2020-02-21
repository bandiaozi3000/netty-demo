package com.atguigu.netty.demo.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @ClassName:MyServer
 * @Description:websocket服务端
 * @Author:lm.sun
 * @Date:2020/1/20 13:21
 */
public class MyServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler((LogLevel.WARN)))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {


                            //获取到pipeline
                            ChannelPipeline channelPipeline = socketChannel.pipeline();

                            //因为基于http协议，使用http的编码和解码器
                            channelPipeline.addLast(new HttpServerCodec());
                            //是以块方式写，添加ChunkedWriteHandler处理器
                            channelPipeline.addLast(new ChunkedWriteHandler());

                            /**
                             * 说明:
                             * 1.http数据在传输过程中是分段,HttpObjectAggregator就是可以将多个段聚合
                             * 2.这就是为什么当浏览器发送大量数据时,就会发出多次http请求
                             */
                            channelPipeline.addLast(new HttpObjectAggregator(8192));
                            /**
                             * 说明:
                             * 1.对应websocket,它的数据时以帧(frame)形式传播
                             * 2.可以看到webSocketFrame下面有6个子类
                             * 3.浏览器请求时:ws://localhost:6666/hello表示请求的uri
                             * 4.WebSocketServerProtocolHandler:核心功能是将Http协议升级为wc协议,保持长连接
                             * 5.是通过一个状态码101
                             */
                            channelPipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            //自定义的handler,处理业务了逻辑
                            channelPipeline.addLast(new MyServerHandler());
                        }
                    });
            System.out.println("****** netty 服务器启动 ******");
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();

            //监听关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
