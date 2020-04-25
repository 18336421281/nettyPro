package com.fhs.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author fhs
 * 2020/4/24 17:55
 * 文件说明: 图片拷贝  通道拷贝
 */
public class NioFileChannel4 {

    public static void main(String[] args) throws IOException {

        FileInputStream fileInputStream = new FileInputStream("1.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("3.jpg");

        FileChannel channel1 = fileInputStream.getChannel();
        FileChannel channel2 = fileOutputStream.getChannel();
        // channel2.transferFrom(channel1, 0 , channel1.size() / 2);
        channel2.transferFrom(channel1, 0 , channel1.size());
        channel1.close();
        channel2.close();
        fileInputStream.close();
        fileOutputStream.close();


    }
}
