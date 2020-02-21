package com.atguigu.netty.demo.demo;

import java.io.FileInputStream;
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
public class NIOFileChannelDemo02 {

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("d:\\file01.txt");
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\file02.txt");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        while(true){ //循环读取

            /**
             *  public final Buffer clear() {
                   position = 0;
                   limit = capacity;
                   mark = -1;
                   return this;
             }  重要操作,如果没有改行代码,read会一直为0
             */
            byteBuffer.clear(); //清空buffer，position和limit都会初始化,如果不清空,write完之后,position和limit一样,则会读不到任何东西
            int read = inputStreamChannel.read(byteBuffer);
            if(read ==-1){
                break;
            }
            //buffer翻转
            byteBuffer.flip();
            outputStreamChannel.write(byteBuffer);
        }
        fileInputStream.close();
        fileOutputStream.close();
        inputStreamChannel.close();
        outputStreamChannel.close();
    }
}
 