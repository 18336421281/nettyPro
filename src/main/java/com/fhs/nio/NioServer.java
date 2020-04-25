package com.fhs.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author fhs
 * 2020/4/25 9:39
 * 文件说明:
 */
public class NioServer {

    public static void main(String[] args) throws IOException {

        // 1. 创建 ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 2. 得到一个 Selector 选择器
        Selector selector = Selector.open();

        // 绑定 并启动 9999 Socket 端口
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));
        // 设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 将 ServerSocketChannel 注册到 Selector 关心事件为 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 3. 循环等待事件
        while (true){

            if(selector.select(1000) == 0){ // 没有事件发生
                System.out.println("Selector 等待 1 秒, 没有事件发送");
                continue;
            }
            // 发生了事件 获取发生事件集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> ketIterator = selectionKeys.iterator();
            System.out.println(selector.keys().size());

            System.out.println("selectionKeys的长 = " + selectionKeys.size());
            while (ketIterator.hasNext()){
                // 得到 SelectionKey 发生事件
                SelectionKey key = ketIterator.next();

                // 判断发生的事件 进行相应的处理
                if(key.isAcceptable()){ // 如果是 OP_ACCEPT
                    // 创建一个 SocketChannel 客户端通道
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    System.out.println("客户端连接成功 = " + socketChannel.hashCode());
                    // 设置为 非阻塞
                    socketChannel.configureBlocking(false);

                    // 将 SocketChannel 注册到 Selector 选择器上 关注事件为 OP_READ 读
                    // 关联一个 ByteBuffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (key.isReadable()){ // 如果是 OP_READ
                    // 获取 socketChannel
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    // 获取 Buffer 的数据
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    socketChannel.read(buffer);
                    System.out.println("服务器读取到数据 = " + new String(buffer.array(), 0, buffer.position()));
                }
                ketIterator.remove();
            }
            


        }






    }
}
