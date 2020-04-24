package com.fhs.nio;


import com.sun.org.apache.xml.internal.utils.StringToIntTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author fhs
 * 2020/4/24 15:02
 * 文件说明: 使用 缓冲 + 文件通道 向本地 读取 文件
 */
public class NioBufferFileChannel2 {

    public static void main(String[] args) throws IOException {

        // 1. 创建原生 读入流对象
        File file = new File("d:\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        // 2. 创建 文件通道
        FileChannel channel = fileInputStream.getChannel();

        // 3. 创建 字节 缓冲区
        ByteBuffer allocate = ByteBuffer.allocate((int) file.length());

        int read = channel.read(allocate);

        String s = allocate.toString();
        System.out.println("s:"+ s);
        System.out.println(new String(allocate.array()));

        fileInputStream.close();
    }
}
