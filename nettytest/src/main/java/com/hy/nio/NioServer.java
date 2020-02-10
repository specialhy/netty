package com.hy.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/***
 * 这里是开了五个端口，然后就是五个通道注册到一个selector中
 */
public class NioServer {
    public static void main(String[] args) throws Exception {
        //创建端口号集合
       int[] prots = new int[5];
       prots[0] = 8000;
       prots[1] = 8001;
       prots[2] = 8002;
       prots[3] = 8003;
       prots[4] = 8004;
       //创建selector(选择器)
       Selector selector = Selector.open();
       for (int i = 0; i < prots.length; i++) {
           //创建channel
           ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
           //设置为非阻塞
           serverSocketChannel.configureBlocking(false);
           //获取到一个与这个通道关联的serversocket,用于与客户端交互
           ServerSocket serverSocket = serverSocketChannel.socket();
           //设置绑定的端口号
           InetSocketAddress address = new InetSocketAddress(prots[i]);
           //将Serversocket绑定到端口上
           serverSocket.bind(address);
           //这个channel与selector和buffer都有关联关系
           //我们将channel注册到selector上面,都会创建一个(返回值)selectkey,感兴趣的事件是OP_ACCEPT(接受连接)
           //可以通过selectkey获取关联的channel
           SelectionKey selectkey=serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

           System.out.println("监听端口: "+prots[i]);
       }
        //Nio这块也有死循环,然后调用select方法(阻塞的方法),然后客户端连接之后,我们判断这是一个什么事件(连接),然后调用相对应的处理
        //对于事件的处理也就是一个死循环,一直监听这
        while (true){
            //返回selectkey
            Thread.sleep(1000);
            int keynumber=selector.select();
            System.out.println("keynumber: "+keynumber);

            //监听多个通道返回,如果返回，那么就说明channel存在事件,就需要获取到这个事件
            //返回我们感兴趣的事件(Select key set)
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectionKeys: "+selectionKeys);
            //循环取出
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                Thread.sleep(1000);
                SelectionKey selectionKey = iterator.next();
                //如果有连接进来,就去接受真正的连接,是不是有数据进来
                if (selectionKey.isAcceptable()){
                    //在上面说了可以通过selectkey获取关联的channel
                    //相当于通过selectKey拿到相关的channel
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
                    //将获取到的channel真正连接进来,并设置为非阻塞
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);

                    //这里对真正连接进来的channel进行监听读事件,读取channel中的数据,只有加了这行代码下面的else if才会执行
                    socketChannel.register(selector,SelectionKey.OP_READ);
                    //读完以后将key移除
                    iterator.remove();

                    System.out.println("获取客户端连接: "+socketChannel);
                //判断是都有对于读监听事件
                }else if(selectionKey.isReadable()){
                     SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                     int bytesRead = 0;
                     while (true){
                         ByteBuffer byteBuffer = ByteBuffer.allocate(512);

                         byteBuffer.clear();

                         int read = socketChannel.read(byteBuffer);
                         //没有数据
                         if(read <= 0){
                             break;
                         }
                         //反转
                         byteBuffer.flip();
                         socketChannel.write(byteBuffer);
                         bytesRead += read;
                     }
                     System.out.println("读取: "+ bytesRead + ",来自于: "+socketChannel);
                     //iterator.remove();
                }

            }
        }




    }
}
