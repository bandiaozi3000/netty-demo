package com.atguigu.netty.demo.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @ClassName:NettyByteBuf01
 * @Description:Unpool测试类
 * @Author:lm.sun
 * @Date:2020/1/17 17:36
 */
public class NettyByteBuf01 {
    public static void main(String[] args) {

        /**
         * 创建一个ByteBuf
         * 说明
         * 1.创建对象,该对象包含一个数组arr,是一个byte[10]
         * 2.在netty的buffer中,不需要使用flip进行反转,底层维护了一个readerIndex和writerIndex
         * 3.通过readerIndex和writerIndex和capacity,将buffer分成三个区域
         * 0--readerIndex:已经读取的区域
         * readerIndex--writerIndex:可读的区域
         * writer--capacity:可写的区域
         */
        ByteBuf byteBuf = Unpooled.buffer(10);

        for(int i =0;i<byteBuf.capacity();i++){
            byteBuf.writeByte(i);
        }

        System.out.println("capacity="+byteBuf.capacity());

        for(int i =0;i<byteBuf.capacity();i++){
            System.out.println(byteBuf.getByte(i));
        }

        for(int i =0;i<byteBuf.capacity();i++){
            System.out.println(byteBuf.readByte());
        }
    }
}
