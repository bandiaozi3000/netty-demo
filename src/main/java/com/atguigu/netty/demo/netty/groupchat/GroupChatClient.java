package com.atguigu.netty.demo.netty.groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @ClassName:GroupChatClient
 * @Description:netty群聊客户端
 * @Author:lm.sun
 * @Date:2020/1/19 14:32
 */
public class GroupChatClient {

    private final String host;
    private final int port;

    public GroupChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            //得到pipeline
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            //加入相关的handler
                            channelPipeline.addLast("decoder", new StringDecoder());
                            channelPipeline.addLast("encoder", new StringEncoder());
                            //加入自定义handler
                            channelPipeline.addLast(new GroupChatCilentHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host,port).sync();
            Channel channel = channelFuture.channel();
            System.out.println("****** "+channel.localAddress()+" ******");
            //客户端需要输入信息,创建一个扫描器
            Scanner scanner = new Scanner(System.in);
            while(scanner.hasNext()){
                String msg = scanner.nextLine();
                //通过channel发送到服务端
                channel.writeAndFlush(msg+"\r\n");
            }
            } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new GroupChatClient("127.0.0.1",6666).run();
    }
}
