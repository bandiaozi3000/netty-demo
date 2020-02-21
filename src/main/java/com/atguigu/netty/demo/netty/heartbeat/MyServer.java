package com.atguigu.netty.demo.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName:MyServer
 * @Description:netty心跳机制
 * @Author:lm.sun
 * @Date:2020/1/19 15:23
 */
public class MyServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler((LogLevel.WARN)))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            //获取到pipeline
                            ChannelPipeline channelPipeline = socketChannel.pipeline();

                            /**
                             * 加入一个netty提供的IdleStateHandler
                             * 说明:
                             * 1.IdleStateHandler是netty提供的处理空闲状态的处理器
                             * 2.Long readerIdleTime:表示多长时间没有读,就会发送一个心跳检测包检测是否连接
                             * 3.Long writerIdleTime:表示多长时间没有写,就会发送一个心跳检测包检测是否连接
                             * 4.Long allIdleTime:标识多长时间没有读写,就会发送一个心跳检测包检测是否连接
                             * 5.文档说明:* Triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
                             * read, write, or both operation for a while.
                             * 6.当IdleStateEvent触发后,就会传递给管道的下一个handler去处理,通过调用(触发)下一个handler的userEventTrigged
                             * 在该方法中去处理IdleStateEvent(读空闲,写空闲,读写空闲)
                             */
                            channelPipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                            //加入一个对空闲检测进一步处理的handler(自定义)
                            channelPipeline.addLast(new MyServerHandler());
                        }
                    });
            System.out.println("****** netty 服务器启动 ******");
            ChannelFuture channelFuture = serverBootstrap.bind(6666).sync();

            //监听关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
