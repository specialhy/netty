package com.hy.nio.liaotian;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/***
 * 这里使用一个channel,区别与传统的网络编程，每来一个客户端就新启一个线程
 * NIO服务端只会有一个线程
 *
 * 通过ServerSocketchannel获取与客户端的连接，将ServerSocketchannel注册到selector上面，监听连接事件,当有客户端发生成事件(例如连接事件)时,会通过这些事件生成selectionKey集合,
 * 通过遍历selectionkey集合,判断这个selectionokey是什么事件(例如是连接事件,那么会通过selectionkey获取一个新的ServerSocketchannel(因为上一步使用ServerSocketchannel对于accpet方法进行了监听,那么能到里面来的一定是ServerSocketchannel),
 * 在通过这个新的服务端channel.accept()方法生成一个socketChannel,这个channel是正真与客户端交互数据的channel,然后在这个socketchannel中绑定读事件，监听客户端有没有数据发送来),
 * 如果有就通过key获取一个SocketChannel(因为上一步使用SocketChnnel对于read方法进行了监听,那么能到里面来的一定是SocketChannel),使用这个socketChannel进行数据的读取与发送
 */
public class NioServer {

    //用来存储所有客户端连接,key为uuid值为连接通道
    private static Map<String,SocketChannel> socketChannelMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        //新建serverSocketChannel,这个channel关心的只是连接事件,下面一个channel关注读写
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        //获取到一个与这个通道关联的serversocket,用于与客户端交互
        ServerSocket serverSocket = serverSocketChannel.socket();
        //绑定端口号
        serverSocket.bind(new InetSocketAddress(8899));

        //创建selector(选择器)
        Selector selector = Selector.open();
        //这个channel与selector和buffer都有关联关系
        //我们将channel注册到selector上面,都会创建一个(返回值)selectkey,感兴趣的事件是OP_ACCEPT(接受连接)
        //可以通过selectkey获取关联的channel
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //使用死循环对监听事件进行处理
        while (true){
            try {
                //这里会一致阻塞这，一直会等到监听的OP_ACCEPT事件发生就会返回,返回的是他所关注的OP_ACCEPT事件的数量
                selector.select();
                //之前注册的所有的已经发生的事件,SelectionKey，我们循环判断这是一个什么事件(连接),然后调用相对应的处理
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                selectionKeys.forEach(selectionKey -> {
                    //真正与客户端交互
                    final SocketChannel client;
                    try {
                        //如果这是一个连接事件,表面客户端发起了一个连接
                        if (selectionKey.isAcceptable()){
                            //获取到这个key关联的通道  也就是这个事件发生在那个通道(因为上面使用serverSocketChannel对于accprt方法进行了监听,那么能到里面来的一定是serverSocketChannel)
                            ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) selectionKey.channel();

                            //将这通道与客户端建立连接，通过socketChannel,
                            client = serverSocketChannel1.accept();
                            client.configureBlocking(false);
                            //利用这个channel监听读客户端数据
                            client.register(selector,SelectionKey.OP_READ);
                            //使用uuid生成key
                            String key = "["+ UUID.randomUUID().toString() +"]";
                            //存到容器
                            socketChannelMap.put(key,client);
                        }else if(selectionKey.isReadable()){//上面监听连接 这里监听有没有数据过来
                            //因为上面使用SocketChnnel对于read方法进行了监听,那么能到里面来的一定是SocketChannel
                            client = (SocketChannel) selectionKey.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                            int i = client.read(byteBuffer);
                            //如果读到了数据,没读到返回-1
                            if(i > 0){
                                byteBuffer.flip();
                                Charset charset = Charset.forName("utf-8");
                                //charset.decode(byteBuffer)将一个字节数据解码成一个char,然后再.array()就可以得到一个char数组
                                String receivedMessage  =String.valueOf(charset.decode(byteBuffer).array());
                                System.out.println(client +":"+receivedMessage);
                                String senderKey = null;
                                //获取到发送端的key
                                for(Map.Entry<String,SocketChannel> entry : socketChannelMap.entrySet()){
                                    senderKey = entry.getKey();
                                    //获取到发送端
                                    if(client == entry.getValue()){
                                        senderKey = entry.getKey();
                                        ByteBuffer writeBuffer  =ByteBuffer.allocate(1024);
                                        //谁发的发的什么,这里是读到buffer中
                                        writeBuffer.put(("I:"+receivedMessage).getBytes());
                                        writeBuffer.flip();
                                        client.write(writeBuffer);
                                    }else {
                                        //获取到其他SocketChannel
                                        SocketChannel value = entry.getValue();
                                        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                                        //谁发的发的什么,这里是读到buffer中
                                        writeBuffer.put((senderKey + ":" + receivedMessage).getBytes());
                                        writeBuffer.flip();
                                        value.write(writeBuffer);
                                    }
                                }

                            }
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
                selectionKeys.clear();
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }
}
