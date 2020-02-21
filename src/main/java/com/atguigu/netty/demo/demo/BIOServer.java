package com.atguigu.netty.demo.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName:BIOServer
 * @Description:传统BIO服务
 * @Author:lm.sun
 * @Date:2019/12/25 14:11
 */
public class BIOServer {

    public static void main(String[] args) throws IOException {

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(6666);
        while (true) {
            System.out.println("等待客户端连接............");
            /**
             * 接受不到客户端的请求,线程会阻塞在这里.当收到连接请求时,会继续运行
             */
            final Socket socket = serverSocket.accept();
            System.out.println("客户端连接............");
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        execute(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    public static void execute(Socket socket) throws IOException {
        try {
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            while (true) {
                System.out.println("等待客户端输送内容...........");
                /**
                 * 若客户端没有内容发送,线程会阻塞在这里.当收到内容时,会继续运行
                 */
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println("接收客户端内容为:" + new String(bytes, 0, read));
                }else{
                    /**
                     * 当无接收内容时,断开该Socket
                     */
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
