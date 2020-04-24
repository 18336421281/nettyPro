package com.fhs.nio;

import java.nio.IntBuffer;

/**
 * @author fhs
 * 2020/4/24 11:40
 * 文件说明: BasicBuffer 基本使用
 */
public class BasicBuffer {

    public static void main(String[] args) {

        // 1. 创建存放 int 类型 Buffer
        IntBuffer intBuffer = IntBuffer.allocate(5);

        intBuffer.put(4);
        intBuffer.put(3);
        intBuffer.put(7);
        intBuffer.put(5);
        // intBuffer.put(4);

        //切换读写模式  使用读要先切换模式  重要！
        intBuffer.flip();

        while (intBuffer.hasRemaining()) {
            int i = intBuffer.get();
            System.out.println(i);
        }
    }
}

