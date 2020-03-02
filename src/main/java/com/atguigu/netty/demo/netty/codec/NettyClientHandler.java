package com.atguigu.netty.demo.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @ClassName:NettyClientHandler
 * @Description:Netty客户端处理器
 * @Author:lm.sun
 * @Date:2020/1/10 15:05
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter{

    /**
     * 当通道就绪就会触发该方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //发送一个Student对象到服务器
        StudentPOJO.Student student = StudentPOJO.Student.newBuilder().setId(4).setName("豹子头 林冲").build();
        ctx.writeAndFlush(student);
    }

    /**
     * 当通道有读取事件时触发
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        System.out.println("服务器回复的消息:"+byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址:"+ctx.channel().remoteAddress());
    }

    /**
     * 异常捕捉处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
