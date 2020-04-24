package com.fhs.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author fhs
 * 2020/4/23 20:51
 * 文件说明: BIO模型
 */
public class BIOServer {

    public static void main(String[] args) throws IOException {

        // 1. 创建线程池
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        // 创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);

        System.out.println("6666服务器启动了！");

        while (true) {
            // 监听连接
            final Socket socket = serverSocket.accept();
            System.out.println("连接到客户端");

            // 创建线程
            newCachedThreadPool.execute(new Runnable() {
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                    Head(socket);
                }
            });
        }
    }

    // 2. 创建 Web Secock
    public static void Head(Socket socket){
        try {
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            while (true){
                int read = inputStream.read(bytes);
               System.out.println(read);
                if (read != -1){
                    System.out.println(Thread.currentThread().getName());
                    System.out.println(new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭socket");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
