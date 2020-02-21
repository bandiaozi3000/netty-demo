package com.atguigu.netty.demo.netty.groupchat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @ClassName:GroupChatCilentHandler
 * @Description:netty群聊客户端自定义handler
 * @Author:lm.sun
 * @Date:2020/1/19 14:40
 */
public class GroupChatCilentHandler  extends SimpleChannelInboundHandler<String>{

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println(s.trim());
    }
}
