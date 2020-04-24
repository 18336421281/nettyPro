package com.fhs.nio;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author fhs
 * 2020/4/24 15:21
 * 文件说明:  使用1个Buffer 完成读写操作  实现复制文件功能  1.txt  复制出来 2.txt
 */
public class NioBufferFileChannel3 {


    public static void main(String[] args) throws IOException {
        // 1. 创建输入流
        FileInputStream file1 = new FileInputStream("1.txt");
        FileChannel channel1 = file1.getChannel();

        //  输出流
        FileOutputStream file2 = new FileOutputStream("2.txt");
        FileChannel channel2 = file2.getChannel();

        // 2. 创建一个 Buffer 缓冲
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while (true){
            byteBuffer.clear();
            int read = channel1.read(byteBuffer);
            System.out.println(read);
            if(read == -1){
                break;
            } else {
                byteBuffer.flip();
                channel2.write(byteBuffer);
            }

        }

    }
}
