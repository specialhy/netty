package com.hy.netty.firstexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/***
 *  bz 1 服务端 http
 */
public class TestServer {

    public static void main(String[] args) throws Exception{
        //可以看做是两个线程组,就是死循环,等待客户端连接,对于服务器端的编程都会存在死循环,像是tomcat都会有死循环
        //还有Handler和childHandler,那么Handler是对于bossGroup操作,childHandler是对于workerGroup操作
        //一般在客户端只会有一个线程组，也只需要handler
        EventLoopGroup bossGroup = new NioEventLoopGroup();//只是接受客户端的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();//处理连接
        try {
            //启动服务器,服务器启动类简化服务器配置
            //对于服务端属性,配置做了一些封装,使得我们可以轻松的启动,下面是使用一种方法链的方式
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //这里使用了反射创建一个NioServerSocketChannel管道
            ServerBootstrap serverBootstrap1=serverBootstrap.group(bossGroup, workerGroup);
                    //Handler是处理bossGroup这个线程组里面的事情
                    //childHandler是处理workerGroup这个线程组里面的事情
            //NioServerSocketChannel网络编程的一个通道,所以这个是连接的线程组使用用于接收客户端连接的,bossGroup
            //TestServerInitialLizer是用来连接之后进行处理的，workerGroup
            ServerBootstrap serverBootstrap2=serverBootstrap1.channel(NioServerSocketChannel.class);
            //bz 5这里存放我们自己的初始化器
            serverBootstrap2.childHandler(new TestServerInitialLizer());

            //监听8899端口,启动服务,sync等待同步,阻塞操作,
            //ChannelFuture是java并发包下面Future接口的更加完善的实现,
            //Future接口:异步操作不会等待消息返回,而是立即返回,返回一个Future,
            //我们可以在返回的Future里面判断操作是不是成功,然后如果执行完毕做什么，未执行完毕做什么
            ChannelFuture channelFuture = serverBootstrap.bind(9999).sync();
            //关闭
            channelFuture.channel().closeFuture().sync();
        }finally {
            //关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
