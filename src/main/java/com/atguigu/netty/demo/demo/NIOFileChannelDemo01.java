package com.atguigu.netty.demo.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @ClassName:NIOFileChannelDemo
 * @Description:NIO Demo
 * @Author:lm.sun
 * @Date:2019/12/26 14:29
 */
public class NIOFileChannelDemo01 {

    public static void main(String[] args) throws IOException {
        File file = new File("d:\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)(file.length()));
        fileChannel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array()));
        fileChannel.close();
    }
}
 