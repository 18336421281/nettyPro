package com.fhs.nio.groupchat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.security.PublicKey;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author fhs
 * 2020/4/25 12:16
 * 文件说明: 群聊系统  客户端
 */
public class GroupChatClient {

    private final String HOST = "127.0.0.1"; // 服务器IP
    private final int PORT = 6777; // 服务器端口
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    // 构造器
    public GroupChatClient(){
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            username = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(username + " is ok ...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 写数据
    public void sendInfo(String info){
        info = username + " 说： " + info;
        try {
            // 发送数据
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取服务器消息
    public void readInfo(){
        try {
            int select = selector.select();
            if(select > 0 ){  // 有可用通道
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if (key.isReadable()){ // 读取
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        socketChannel.read(byteBuffer);
                        String msg = new String(byteBuffer.array());
                        // 去空格
                        System.out.println("读取消息： " + msg.trim());
                    }
                    iterator.remove();
                }
            } else {
                //System.out.println("无可用通道...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public static void main(String[] args) {

        GroupChatClient groupChatClient = new GroupChatClient();

        new Thread() {
            @Override
            public void run(){
                while (true){
                    groupChatClient.readInfo();
                }
            }
        }.start();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            groupChatClient.sendInfo(s);
        }
    }
}
