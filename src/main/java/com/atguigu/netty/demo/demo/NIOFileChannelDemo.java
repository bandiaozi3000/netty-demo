package com.atguigu.netty.demo.demo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @ClassName:NIOFileChannelDemo
 * @Description:NIO Demo
 * @Author:lm.sun
 * @Date:2019/12/26 14:29
 */
public class NIOFileChannelDemo {

    public static void main(String[] args) throws IOException {
        String str = "Hello,尚硅谷";
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\file01.txt");
        FileChannel fileChannel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes());
        byteBuffer.flip();
        fileChannel.write(byteBuffer);
        fileChannel.close();
    }
}
 