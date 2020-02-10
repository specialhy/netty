package com.hy.nio.liaotian;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioClient {
    public static void main(String[] args) throws Exception {
        try {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("127.0.0.1",8899));

        while (true) {
                //阻塞 服务端有没有向客户端返回任何事件
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();

                for (SelectionKey selectionKey : keys) {
                    if (selectionKey.isConnectable()) {
                        SocketChannel server = (SocketChannel) selectionKey.channel();
                        //如果连接在可连接阶段,这里是双向的连接模式
                        if (server.isConnectionPending()) {
                            //如果是完成真正的连接
                            server.finishConnect();

                            ByteBuffer wriyeBuffer = ByteBuffer.allocate(1024);
                            //向服务端发送
                            wriyeBuffer.put((LocalDateTime.now() + "连接成功").getBytes());
                            wriyeBuffer.flip();
                            server.write(wriyeBuffer);
                            //起一个线程完成用户输入
                            ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
                            executorService.submit(() -> {
                                while (true) {
                                    try {
                                        //复位
                                        wriyeBuffer.clear();
                                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

                                        String send = bufferedReader.readLine();
                                        wriyeBuffer.put(send.getBytes());
                                        wriyeBuffer.flip();
                                        server.write(wriyeBuffer);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        server.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        SocketChannel serverChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        //获取到服务端发送的数据
                        int count = serverChannel.read(byteBuffer);
                        if (count > 0) {
                            String receiveMessage = new String(byteBuffer.array(), 0, count);
                            System.out.println(receiveMessage);
                        }
                    }
                }
                //清理
                keys.clear();;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
