package com.fhs.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author fhs
 * 2020/4/24 14:47
 * 文件说明: 使用 缓冲 + 文件通道 向本地写入文件
 */
public class NioBufferFileChannel {

    /**
     *    实例要求:
     *
         1。 使用前面学习后的Byte Buffer(缓冲)和 FileChannel(通道)
         将"hello 世界"写入到file01.txt中

         2。文件不存在就创建
     */
    public static void main(String[] args) throws IOException {
        String s = "Hello 世界";

        // 1. 输出流
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\file01.txt");

        // 2. 获取 FileChannel 文件通道
        FileChannel channel = fileOutputStream.getChannel();

        // 3. 创建 Byte 缓冲
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        byteBuffer.put(s.getBytes());

        // 缓冲转换为 读
        byteBuffer.flip();

        // 4. 将缓存区的数据写入 通道
       channel.write(byteBuffer);
       fileOutputStream.close();



    }
}
