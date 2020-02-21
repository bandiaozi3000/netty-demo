package com.atguigu.netty.demo.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @ClassName:GroupChatClient
 * @Description:群聊Client
 * @Author:lm.sun
 * @Date:2020/1/3 13:37
 */
public class GroupChatClient {

    private Selector selector;
    private SocketChannel socketChannel;
    private static final Integer port = 6666;
    private static final String host = "127.0.0.1";
    private String username;

    public GroupChatClient() {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            username = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(username + "is ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //向服务器发送消息
    public void sendInfo(String info) {
        info = username + "说:" + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取从服务端获取到的消息
    public void readInfo() {
        try {
            int readChannels = selector.select();
            if (readChannels > 0) {
                Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey selectionKey = selectionKeyIterator.next();
                    if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        socketChannel.read(byteBuffer);
                        String msg = new String(byteBuffer.array());
                        System.out.println(msg.trim());
                    }
                    selectionKeyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupChatClient groupChatClient = new GroupChatClient();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    groupChatClient.readInfo();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            groupChatClient.sendInfo(s);
        }
    }
}
