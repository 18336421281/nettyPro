package com.fhs.nio;

import jdk.management.resource.internal.inst.SocketOutputStreamRMHooks;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author fhs
 * 2020/4/25 10:07
 * 文件说明:
 */
public class NioClient {

    public static void main(String[] args) throws IOException {

        // 1. 创建一个 SocketChannel
        SocketChannel socketChannel = SocketChannel.open();
        // 设置非阻塞
        socketChannel.configureBlocking(false);
        // 连接地址
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 9999);


        if(!socketChannel.connect(inetSocketAddress)){
            while (!socketChannel.finishConnect()){
                System.out.println("因为连接需要事件， 客户端不会阻塞");
            }
        }

        // 发送数据
        socketChannel.write(ByteBuffer.wrap("白羊".getBytes()));
        socketChannel.write(ByteBuffer.wrap("白羊2".getBytes()));

        System.in.read();
    }
}
