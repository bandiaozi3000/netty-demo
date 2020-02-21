package com.atguigu.netty.demo.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @ClassName:ScatteringAndGatheringTest
 * @Description:
 * @Author:lm.sun
 * @Date:2019/12/27 14:46
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        serverSocketChannel.socket().bind(inetSocketAddress);
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8;
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                /**
                 * 当管道内没有内容时,read方法会阻塞在此.
                 */
                long l = socketChannel.read(byteBuffers);
                byteRead += l;
                System.out.println("byteRead=" + byteRead);
                Arrays.asList(byteBuffers).stream().map(byteBuffer -> "position" +
                        byteBuffer.position() + ",limit=" + byteBuffer.limit()).forEach(System.out::println);
            }

            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());
            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());
            System.out.println("byteRead:=" + byteRead + "byteWrite=" + byteWrite + ",messagelength" + messageLength);
        }

    }
}
