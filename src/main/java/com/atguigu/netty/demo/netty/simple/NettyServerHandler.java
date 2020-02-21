package com.atguigu.netty.demo.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName:NettyServerHandler
 * @Description:Netty服务端处理器
 * @Author:lm.sun
 * @Date:2020/1/10 10:48
 */

/**
 * 说明
 * 1.我们自定义一个Handler,需要继承netty绑定好的某个HandlerAdapter(规范)
 * 2.这时我们自定义一个Handler,才能称为一个Handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据实现(这里我们可以读取客户端发送的消息)
     * 1.ChannelHandlerContext ctx:上下文对象,含有管道pipeline,通道channel,地址
     * 2.Object msg:就是客户端发送的数据,默认Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器读取线程" +Thread.currentThread().getName());
        System.out.println("server ctx =" +ctx);
        System.out.println("看看Channel和pipeline的关系");
        Channel channel = ctx.channel();
        //channel和channelPipeline是相互包含
        ChannelPipeline channelPipeline = ctx.pipeline();  //是一个双向链表
        /**
         * 比如这里我们有一个非常耗时长的业务->异步执行->提交该channel对应的NIOEventLoop的taskQueue中
         * 解决方案1:用户程序自定义的普通任务
         * 注意:例如下面代码有两个耗时的任务,那么总共消费的时间为两个任务的时间之和,原因:执行任务放到同一个线程的taskQueue中,任务要顺序执行
         * taskQueue:ctx->channel->eventLoop->taskQueue
         *
         */
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(10*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~(>^ω^<)喵2",CharsetUtil.UTF_8));
                }catch (Exception e){
                    System.out.println("发生异常"+e.getMessage());
                }
            }
        });
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(20*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~(>^ω^<)喵3",CharsetUtil.UTF_8));
                }catch (Exception e){
                    System.out.println("发生异常"+e.getMessage());
                }
            }
        });
        //用户自定义定时任务->该任务提交到scheduleTaskQueue中
        //scheduleTaskQueue:ctx->channel->eventLoop->scheduleTaskQueue
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~(>^ω^<)喵4", CharsetUtil.UTF_8));
                } catch (Exception e) {
                    System.out.println("发生异常" + e.getMessage());
                }
            }
        },5, TimeUnit.SECONDS);
        /**
         * 将msg转成一个ByteBuf
         * ByteBuf是netty提供的,不是NIO的ByteBuffer
         */
        ByteBuf byteBuf = (ByteBuf)msg;
        System.out.println("客户端发送的消息是:"+byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址:"+ctx.channel().remoteAddress());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /**
         * writeAndFlush是write+flush
         * 将数据写入到缓存,并刷新
         * 一般讲,我们对这个发送的数据进行编码
         */
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~(>^ω^<)喵1",CharsetUtil.UTF_8));
    }

    //处理异常,一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
