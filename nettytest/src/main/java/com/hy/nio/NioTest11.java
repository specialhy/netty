package com.hy.nio;



import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/***
 * Buffer的
 * Scattering(散开,多个)可以传递一个channel数组,可以做一个比如http请求,把头分为一个数组，body分成一个数组
 * Gathering(合并),将数组都合并起来
 */
public class NioTest11 {
    public static void main(String[] args) throws Exception {
        //NIO的网络编程
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //绑定的端口号
        InetSocketAddress address = new InetSocketAddress(8899);
        //绑定到channel
        serverSocketChannel.socket().bind(address);
        //每个buffer长度  2 3 4
        int messageLength = 2 + 3 + 4;
        //使用一个buffer数组接受三个buffer,这里每次读满就
        ByteBuffer[] buffers = new ByteBuffer[3];

        buffers[0] = ByteBuffer.allocate(2);
        buffers[1] = ByteBuffer.allocate(3);
        buffers[2] = ByteBuffer.allocate(4);

        //接受客户端的请求
        SocketChannel socketChannel = serverSocketChannel.accept();

        while (true){
            int byteRead = 0;
            while (byteRead < messageLength){
                long r = socketChannel.read(buffers);
                byteRead +=r;
                System.out.println("byteRead: "+byteRead);
                //打印值
                Arrays.asList(buffers).stream().
                        map(buffer ->"position: "+buffer.position()+",limit: "+buffer.limit())
                        .forEach(System.out::println);
            }
            //读完之后进行反转
            Arrays.asList(buffers).forEach(buffer ->{
                buffer.flip();
            });
            long bytesWritten = 0;
            //回显回去
            while (bytesWritten < messageLength){
                long t = socketChannel.write(buffers);
                bytesWritten += t;
            }
            Arrays.asList(buffers).forEach(buffer -> {
                buffer.clear();
            });
            System.out.println("byteRead: "+byteRead+",byteWritten: "+bytesWritten+"messageLength:"+messageLength);
        }
    }
}
