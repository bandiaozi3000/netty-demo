package com.atguigu.netty.demo.netty.codec2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

/**
 * @ClassName:NettyServer
 * @Description:Netty服务端
 * @Author:lm.sun
 * @Date:2020/1/10 10:35
 */
public class NettyServer {

    public static void main(String[] args) throws Exception {
        /**
         * 创建BossGroup和WorkerGroup
         * 说明
         * 1.创建两个线程组bossGroup和workerGroup
         * 2.bossGroup只是处理连接请求,真正的和客户端业务处理,会交给workerGroup完成
         * 3.两个都是无限循环
         * 4.bossGroup和workerGroup含有的子线程(NioEventLoop)的个数默认为cpu核数*2
         *   CPU核数:NettyRuntime.availableProcessors(),可通过源代码看出
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);

        //创建服务器端的启动对象,配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();

        //使用链式编程来进行设置
        bootstrap.group(bossGroup, workerGroup) //设置两个线程组
                .channel(NioServerSocketChannel.class) //使用NioServerSocketChannel作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列得到连接个数
                .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道测试对象(匿名对象)
                    //给pipeline设置处理器
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //在pipeline加入ProtoBufDecoder
                        //指定对那种对象进行解码
                        socketChannel.pipeline().addLast("decoder",new ProtobufDecoder(MyDataInfo.MyMessage.getDefaultInstance()));
                        socketChannel.pipeline().addLast(new NettyServerHandler());
                    }
                }); //给我们的workerGroup的EventLoop对应的管道设置处理器
        System.out.println(".....服务器 is ready .....");

        //绑定一个端口并且同步,生成了一个ChannelFuture对象
        //启动服务器(并绑定端口)
        ChannelFuture channelFuture = bootstrap.bind(6666).sync();

        //channelFuture注册监听器,监控我们关心的事件
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
               if(channelFuture.isSuccess()){
                   System.out.println("服务器监听端口6666成功");
               }else{
                   System.out.println("服务器监听端口6666失败");
               }
            }
        });

        //对关闭通道进行监听
        channelFuture.channel().closeFuture().sync();
    }

}
