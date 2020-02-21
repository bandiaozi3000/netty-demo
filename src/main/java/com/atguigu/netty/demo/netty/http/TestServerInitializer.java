package com.atguigu.netty.demo.netty.http;

import io.netty.channel.ChannelInitializer;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @ClassName:TestServerInitializer
 * @Description:
 * @Author:lm.sun
 * @Date:2020/1/13 14:10
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        /**
         * 加入一个netty提供的httpServerCodec codec->[coder-decoder]
         * HttpServerCodec 说明
         * 1.HttpServerCodec是netty提供的处理http的编码解码器
         */
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        pipeline.addLast("MyTestHttpServerHandler",new TestHttpServerHandler());

    }
}
