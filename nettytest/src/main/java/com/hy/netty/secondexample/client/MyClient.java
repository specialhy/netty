package com.hy.netty.secondexample.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/***
 * 客户端
 */
public class MyClient  {

    public static void main(String[] args) throws Exception {
        //还有Handler和childHandler,那么Handler是对于bossGroup操作,childHandler是对于workerGroup操作
        //一般在客户端只会有一个线程组，也只需要handler
        //客户端只需要连接,不需要两个线程组
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(eventExecutors).channel(NioSocketChannel.class)
                    .handler(new MyClinetInitializer());//只需要handler不需要childhandler
            ChannelFuture channelFuture = bootstrap.connect("localhost",8899).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
