package com.hy.netty.fourthexample.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/***
 * 责任链模式:一个请求来了之后通过一个handler前然后再第二个handler前，在然后第一个handler后，第二个handler后
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //绑定netty自带的空闲状态检测处理器 一段时间没有读写就会触发 5秒服务端没有读到客户端   7秒服务端没有向客户端写  10秒没有读和写
        pipeline.addLast(new IdleStateHandler(5,7,10, TimeUnit.SECONDS));
        //检测空闲状态事件
        pipeline.addLast(new MyServerHandler());
    }
}
