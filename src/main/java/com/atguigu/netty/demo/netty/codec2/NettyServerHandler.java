package com.atguigu.netty.demo.netty.codec2;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

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
//public class NettyServerHandler extends ChannelInboundHandlerAdapter {
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {


//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//       StudentPOJO.Student student = (StudentPOJO.Student)msg;
//        System.out.println("客户端发送的数据 id = "+student.getId()+"名字 = "+student.getName());
//    }

    /**
     * 继承ChannelInboundHandlerAdapter,不用再做类型转换
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        //根据dataType来显示不同的信息
        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if(dataType==MyDataInfo.MyMessage.DataType.StudentType){
            MyDataInfo.Student student = msg.getStudent();
            System.out.println("学生id = "+student.getId()+"学生名字 = "+student.getName());
        }else if(dataType==MyDataInfo.MyMessage.DataType.WorkerType){
            MyDataInfo.Worker worker = msg.getWorker();
            System.out.println("工人年龄 = "+worker.getAge()+"工人名字 = "+worker.getName());
        }else{
            System.out.println("传输的类型不正确");
        }
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
