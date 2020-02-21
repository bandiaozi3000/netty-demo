package com.atguigu.netty.demo.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * @ClassName:NettyByteBuf01
 * @Description:Unpool测试类
 * @Author:lm.sun
 * @Date:2020/1/17 17:36
 */
public class NettyByteBuf02 {

    public static void main(String[] args) {

        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world!", Charset.forName("utf-8"));

        if(byteBuf.hasArray()){
            byte[] content = byteBuf.array();
            System.out.println(new String(content,Charset.forName("utf-8")));
            System.out.println("byteBuf="+byteBuf);

            System.out.println(byteBuf.arrayOffset()); // 0
            System.out.println(byteBuf.readerIndex()); // 0
            System.out.println(byteBuf.writerIndex()); // 0
            System.out.println(byteBuf.capacity()); // 36
            System.out.println(byteBuf.readByte());
            System.out.println(byteBuf.getByte(0));

            int len = byteBuf.readableBytes();  //可读取的字节数
            System.out.println("len="+len);

            //使用for取出各个字节
            for(int i=0;i<len;i++){
                System.out.println((char)byteBuf.getByte(i));
            }

            //第一个参数为截取起始下标,第二个为截取的长度
            System.out.println(byteBuf.getCharSequence(0,4,Charset.forName("utf-8")));
            System.out.println(byteBuf.getCharSequence(4,6,Charset.forName("utf-8")));

        }
    }
}
