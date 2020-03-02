package com.atguigu.netty.demo.netty.inandouthandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


public class MyServerInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        //从入站的handler进行解码 MyByteToLongDecoder
        channelPipeline.addLast(new MyByteToLongDecoder());
        //出站的handler进行编码
        channelPipeline.addLast(new MyLongToByteEncoder());
        channelPipeline.addLast(new MyServerHandler());

    }
}
