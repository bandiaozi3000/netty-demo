package com.atguigu.netty.demo.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName:GroupChatServerHandler
 * @Description:netty群聊服务端自定义handler
 * @Author:lm.sun
 * @Date:2020/1/19 14:10
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    //定义一个channel组,管理所有的channel
    //GlobalEventExecutor.INSTANCE:是全局的事件执行器,是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //handlerAdded,表示连接建立,一旦连接,第一个被执行
    //将当前channel加入到channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        Channel channel = ctx.channel();

        //将该客户加入聊天的信息推送给其他在线的客户端
        /**
         * 该方法会将 channelGroup 中所有的channel遍历,并发送消息
         * 我们不需要自己遍历
         */
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "加入聊天" + simpleDateFormat.format(new Date()) + "\n");
        channelGroup.add(channel);
    }

    //断开连接,将XX客户离开信息推送给当前在线的客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "离开了\n");
        System.out.println("channelGroup size" + channelGroup.size());
    }

    //表示channel处于活动状态,提示XX上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线了~");
    }

    //表示channel处于不活动状态,提示XX离线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 离线了~");
    }

    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

        //获取到当前的通道
        Channel channel = channelHandlerContext.channel();

        //遍历channelGroup,根据不同的情况返回不同的消息
        channelGroup.forEach(ch -> {
            if (channel != ch) {
                //不是当前的channel返回消息
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + ":" + s + "\n");
            } else {
                //回显自己发送的消息给自己
                ch.writeAndFlush("[自己]:" + s + "\n");
            }
        });
    }

    //异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭通道
        ctx.close();
    }
}
