package com.atguigu.netty.demo.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @ClassName:GroupChatServer
 * @Description:Netty群聊服务端
 * @Author:lm.sun
 * @Date:2020/1/19 14:00
 */
public class GroupChatServer {

    private int port;

    public GroupChatServer(int port) {
        this.port = port;
    }

    //编写run方法,处理客户端的请求
    public void run() throws InterruptedException {

        //创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            //获取到pipeline
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            //向pipeline加入解码器
                            channelPipeline.addLast("decoder", new StringDecoder());
                            //向pipeline加入编码器
                            channelPipeline.addLast("encoder", new StringEncoder());
                            //向pipeline加入自己的业务处理handler
                            channelPipeline.addLast(new GroupChatServerHandler());
                        }
                    });
            System.out.println("****** netty 服务器启动 ******");
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            //监听关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new GroupChatServer(6666).run();
    }
}
