package com.atguigu.netty.demo.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * @ClassName:MyServerHandler
 * @Description:websocket自定义handler
 * @Author:lm.sun
 * @Date:2020/1/20 13:28
 */

//这里TextWebSocketFrame类型,表示是一个文本帧
public class MyServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器收到消息:"+msg.text());

        //回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间"+ LocalDateTime.now()+msg.text()));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用"+ctx.channel().id().asLongText());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        //id 标识唯一的值,LongText 是唯一的,ShortText不是唯一的
        System.out.println("handlerRemoved 被调用"+ctx.channel().id().asLongText());
        System.out.println("handlerRemoved 被调用"+ctx.channel().id().asShortText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生"+cause.getMessage());
        ctx.close();
    }
}
