package com.atguigu.netty.demo.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName:NIOServer
 * @Description:NIOServer
 * @Author:lm.sun
 * @Date:2020/1/2 16:47
 */
public class NIOServer {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //注册serverSocketChannel到selector，监听事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            //等待1秒,没有监听到事件则返回
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了1秒,无连接");
                continue;
            }
            /**
             * 如果返回的结果大于0,就获取到相关的selectionKey集合,表示已经获取到关注的事件
             * selector.selectedKeys()返回关注事件的集合
             * 通过selectedKeys反向获取通道
             *
             */
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeySet.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功,生成了一个socketChannel:" + socketChannel.hashCode());
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(512));
                }
                if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                    socketChannel.read(byteBuffer);
                    System.out.println("from 客户端" + new String(byteBuffer.array()));
                }
                //手动从集合中移动当前的selectionKey,防止重复操作
                keyIterator.remove();
            }

        }

    }
}
