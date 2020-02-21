package com.atguigu.netty.demo.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @ClassName:MyServerHandler
 * @Description:netty心跳检测自定义Handler
 * @Author:lm.sun
 * @Date:2020/1/19 15:58
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     *
     * @param ctx 上下文
     * @param evt 事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()){
                case READER_IDLE:eventType = "读空闲";break;
                case WRITER_IDLE:eventType = "写空闲";break;
                case ALL_IDLE:eventType = "读写空闲";break;
            }
            System.out.println(ctx.channel().remoteAddress()+"--- 超时时间 ---" +eventType);
            System.out.println("服务器做相应处理");
        }
    }
}
