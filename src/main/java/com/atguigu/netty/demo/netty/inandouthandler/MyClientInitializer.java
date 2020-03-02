package com.atguigu.netty.demo.netty.inandouthandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        //加入一个出站的handler 对数据进行一个编码
        channelPipeline.addLast(new MyLongToByteEncoder());

        //加入一个入站的解码器(入站handler )
        channelPipeline.addLast(new MyByteToLongDecoder());
        //加入一个自定义的handler,处理业务
        channelPipeline.addLast(new MyClientHandler());
    }
}
