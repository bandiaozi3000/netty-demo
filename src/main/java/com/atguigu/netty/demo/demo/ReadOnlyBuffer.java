package com.atguigu.netty.demo.demo;

import java.nio.ByteBuffer;

/**
 * @ClassName:ReadOnlyBuffer
 * @Description:只读Buffer
 * @Author:lm.sun
 * @Date:2019/12/27 14:12
 */
public class ReadOnlyBuffer {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        for(int i =0;i<10;i++){
            byteBuffer.putInt(i);
        }
        byteBuffer.flip();
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        for(int i=0;i<10;i++){
            System.out.println(readOnlyBuffer.getInt());
        }
//        readOnlyBuffer.putInt(1); //java.nio.ReadOnlyBufferException
    }
}
