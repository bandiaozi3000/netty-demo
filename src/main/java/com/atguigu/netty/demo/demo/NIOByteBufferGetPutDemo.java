package com.atguigu.netty.demo.demo;

import java.nio.ByteBuffer;

/**
 * @ClassName:NIOByteBufferGetPutDemo
 * @Description:ByteBuffer
 * @Author:lm.sun
 * @Date:2019/12/27 14:07
 */
public class NIOByteBufferGetPutDemo {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        byteBuffer.putInt(100);
        byteBuffer.putLong(100L);
        byteBuffer.putShort((short)1);
        byteBuffer.putChar('a');
        byteBuffer.flip();
        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        //ByteBuffer读写的类型顺序应该一样,如果不一样,则会报 java.nio.BufferUnderflowException 异常
        System.out.println(byteBuffer.getLong()); //会报异常
        System.out.println(byteBuffer.getShort());
        System.out.println(byteBuffer.getChar());
    }
}
