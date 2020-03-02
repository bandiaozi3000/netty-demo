package com.atguigu.netty.demo.netty.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class MyServerHandler extends SimpleChannelInboundHandler<byte[]> {

    private int count;

//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
//       byte[] buffer = new byte[msg.readableBytes()];
//       msg.readBytes(buffer);
//
//       //将buffer转成字符串
//        String message = new String(buffer, Charset.forName("utf-8"));
//        System.out.println("服务器接收到的数据:"+message);
//        System.out.println("服务器接收到的消息量="+(++this.count));
//
//        //服务器回送数据到客户端,回送一个随机Id
//        ByteBuf responseByteBuf = Unpooled.copiedBuffer(UUID.randomUUID().toString()+"\n",Charset.forName("utf-8"));
//        ctx.writeAndFlush(responseByteBuf);
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       cause.printStackTrace();
       ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
        String message = new String(msg, Charset.forName("utf-8"));
        System.out.println("服务器接收到的数据:"+message);
        System.out.println("服务器接收到的消息量="+(++this.count));
    }
}
