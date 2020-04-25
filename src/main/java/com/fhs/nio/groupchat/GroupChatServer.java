package com.fhs.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author fhs
 * 2020/4/25 11:07
 * 文件说明: 群聊系统 服务器端
 */
public class GroupChatServer {

    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6777;

    public GroupChatServer(){
        try {
            // 获取 选择器 和 通道
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            // 绑定端口 并启动
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            // 非阻塞
            listenChannel.configureBlocking(false);
            // 注册通道
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 监听
    public void listen(){
        try {
            while (true) {
                int conut = selector.select(2000);
                if(conut > 0){ // 有事件处理
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()){  // OP_ACCEP
                            SocketChannel channel = listenChannel.accept();
                            channel.configureBlocking(false);
                            channel.register(selector, SelectionKey.OP_READ);
                            System.out.println(channel.getRemoteAddress() + " 上线");
                        } if (key.isReadable()){ // OP_READ
                            readData(key);
                        }
                        // 使用完 删除 SelectionKey 避免多线程造成重复读
                        iterator.remove();
                    }
                } else {
                    //System.out.println("等待连接...");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取客户端消息
    private void readData(SelectionKey key){
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int count = channel.read(byteBuffer);
            if(count > 0){
                String msg = new String(byteBuffer.array());
                System.out.println("form 客户端： " + msg);
                // 向其他客户端显示 排除自己
                sendInfoToOtherClinets(msg, channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + " 离线了");
                // 取消注册
                key.cancel();
                // 关闭通道
                channel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    // 向其他客户端发送消息（通道） 排除自己
    private void sendInfoToOtherClinets(String msg, SocketChannel self) throws IOException {
        System.out.println("转发消息中...");
        // 遍历 选择器所有的 key
        for (SelectionKey key : selector.keys()){
            // 得到key
            Channel channel = key.channel();
            // 判断 排除自己通道
            if (channel instanceof SocketChannel && channel != self){
                // 强转
                SocketChannel socketChannel = (SocketChannel) channel;
                // 通知发送
                socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
            }
        }
    }



    public static void main(String[] args) {

        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();

    }
}
