package com.atguigu.netty.demo.demo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @ClassName:MapperByteBuffer
 * @Description:MapperByteBuffer
 * @Author:lm.sun
 * @Date:2019/12/27 14:21
 */

/**
 * MappedByteBuffer 可以让文件直接在内存(堆外内存)修改,操作系统不需要拷贝一次
 */
public class MapperByteBuffer {

    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("d:\\file01.txt","rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        /**
         * 参数1:FileChannel.MapMode.READ_WRITE 使用的读写模式
         * 参数2: 0:可以直接修改的起始位置
         * 参数3: 5:是映射到内存的大小(不是索引位置),即将file01.txt的多少个字节映射到内存
         * 可以直接修改的范围就是0-5
         * 实际类型DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0,(byte)'H');
        //会报错 java.lang.IndexOutOfBoundsException
        mappedByteBuffer.put(5,(byte)'H');

    }
}
