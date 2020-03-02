package com.atguigu.netty.demo.netty.protocoltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline.addLast(new MyMessageEncoder());
        channelPipeline.addLast(new MyMessageDecoder());
        channelPipeline.addLast(new MyClientHandler());
    }
}
