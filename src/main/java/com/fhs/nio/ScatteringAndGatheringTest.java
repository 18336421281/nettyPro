package com.fhs.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author fhs
 * 2020/4/24 20:26
 * 文件说明:
 */
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws IOException {

        // 1. 创建 ServerSocketChannel 和 SocketChannel
        ServerSocketChannel open = ServerSocketChannel.open();

        // 2. 绑定端口 到 ServerSocket 并启动
        open.socket().bind(new InetSocketAddress(7000));

        // 3. 创建 ByteBuffer 数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 4. 等待客户端连接 telnet 连接
        SocketChannel accept = open.accept();

        int byteLength = 8;
        // 循环读取
        while (true){
            int byteRead = 0;
            while (byteRead < byteLength){
                long write = accept.read(byteBuffers);
                byteRead += write;

                System.out.println("Read= " + byteRead);

                // 使用jdk8 流打印
                Arrays.asList(byteBuffers).stream().map(buffer ->
                        "position=" + buffer.position()
                        + ", limit" + buffer.limit()).forEach(System.out::println);
            }
            // 将所有字节 进行反转
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            long byteWrite = 0;
            while (byteWrite < byteLength){
                long write = accept.write(byteBuffers);
                byteWrite += write;
            }
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());

            System.out.println("Read= " + byteRead + "Write= " + byteWrite);



        }

    }
}
