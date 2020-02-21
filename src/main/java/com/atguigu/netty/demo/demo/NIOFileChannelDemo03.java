package com.atguigu.netty.demo.demo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @ClassName:NIOFileChannelDemo
 * @Description:NIO Demo
 * @Author:lm.sun
 * @Date:2019/12/26 14:29
 */
public class NIOFileChannelDemo03 {

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("E:\\UpupooResource\\2000022333\\preview.jpg");
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\UpupooResource\\2000022333\\preview2.jpg");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();
        outputStreamChannel.transferFrom(inputStreamChannel,0,inputStreamChannel.size());
        fileInputStream.close();
        fileOutputStream.close();
        inputStreamChannel.close();
        outputStreamChannel.close();
    }
}
 