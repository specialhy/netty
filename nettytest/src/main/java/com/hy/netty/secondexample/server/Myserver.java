package com.hy.netty.secondexample.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ReflectiveChannelFactory;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/***
 * 服务端 TCP 这里与firstexample不同，firstexample使用http,这里使用TPC只会发送我需要的消息，
 * 只是传输的,不用发送一些head body这些http必须要发送的
 */
public class Myserver {
    public static void main(String[] args) throws Exception {
        //如果写了就创建指定数量线程，这里创建一个线程
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workergroup = new NioEventLoopGroup(1);

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workergroup).channel(NioServerSocketChannel.class)
                    .childHandler(new MyServerInitiaLizer());
            //创建一个新的channel绑定到这个端口号上面
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync().addListener(future -> {

            });
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workergroup.shutdownGracefully();
        }
    }

}
