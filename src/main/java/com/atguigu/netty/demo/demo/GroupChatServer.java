package com.atguigu.netty.demo.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @ClassName:GroupChatServer
 * @Description:群聊Server,服务器接收消息并实现转发,处理上线和离线
 * @Author:lm.sun
 * @Date:2020/1/3 11:38
 */
public class GroupChatServer {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private static final Integer port = 6666;

    private GroupChatServer() {
        try {
            selector = Selector.open();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(inetSocketAddress);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        try {
            while (true) {
                int count = selector.select();
                if (count > 0) {
                    Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                    while (selectionKeyIterator.hasNext()) {
                        SelectionKey selectionKey = selectionKeyIterator.next();
                        if (selectionKey.isAcceptable()) {
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() + "上线了");
                        }
                        if (selectionKey.isReadable()) {
                            readData(selectionKey);
                        }
                        selectionKeyIterator.remove();
                    }
                } else {
                    System.out.println("*******服务器等待连接*******");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取渠道消息到缓冲区
    private void readData(SelectionKey selectionKey) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int count = socketChannel.read(byteBuffer);
            if (count > 0) {
                String msg = new String(byteBuffer.array());
                System.out.println("从客户端接收到消息:" + msg);
                sendInfoToOtherClient(msg, socketChannel);
            }
        } catch (IOException e) {
            try {
                System.out.println(socketChannel.getRemoteAddress() + "离线了....");
                //取消注册
                selectionKey.cancel();
                //关闭通道
                socketChannel.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }

    //转发消息给其他客户端
    private void sendInfoToOtherClient(String msg, SocketChannel selfChannle) throws IOException {
        System.out.println("******服务器转发消息中*****");
        //遍历所有注册到selector上的socketChannel，并排除自己
        for (SelectionKey selectionKey : selector.keys()) {
            //通过key取出对应的channel
            Channel targetChannel = selectionKey.channel();
            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != selfChannle) {
                SocketChannel dest = (SocketChannel) targetChannel;
                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(byteBuffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
