package com.atguigu.netty.demo.netty.http;

/**
 * @ClassName:TestHttpServerHandler
 * @Description:
 * @Author:lm.sun
 * @Date:2020/1/13 14:10
 */


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * 说明
 * 1.SimpleChannelInboundHandler是ChannelInboundHandlerAdapter
 * 2.HttpObject客户端和服务器端相互通讯的数据被封装成HttpObject
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        //判断httpObject是不是httpRequest请求
        if(httpObject instanceof HttpRequest){

            System.out.println("httpObject 类型 = "+httpObject.getClass());
            System.out.println("客户端地址: " +channelHandlerContext.channel().remoteAddress());

            //每个新连接之间不共享Handler
            //根据HashCode可以看出每访问一次接口pipeLine 和 handler 的hashcode都不一样
            System.out.println("pipeLine hashcode :" + channelHandlerContext.pipeline().hashCode()+" handler hashcode :" +channelHandlerContext.handler().hashCode());

            //过滤指定访问
            HttpRequest httpRequest = (HttpRequest) httpObject;
            URI uri = new URI(httpRequest.uri());

            //浏览器图标
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了 favicon.ico ,系统不做响应");
                return;
            }

            //回复信息给浏览器
            ByteBuf content = Unpooled.copiedBuffer("hello,客户端~(>^ω^<)喵",CharsetUtil.UTF_16);

            //构造一个http的响应,即httpResponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            //将构建好的response返回
            channelHandlerContext.writeAndFlush(response);
        }
    }
}
