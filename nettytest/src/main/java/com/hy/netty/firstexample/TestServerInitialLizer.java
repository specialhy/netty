package com.hy.netty.firstexample;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/***
 * bz 2 ChannelInitializer通道初始化器,泛型指定SocketChannel，因为这里是接受的这个SocketChannel
 */
public class TestServerInitialLizer extends ChannelInitializer<SocketChannel> {

    /***
     * 有客户端连接之后就会调用这个,初始化通道
     * @param ch
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //这个是管道,然后每一个管道都有许多的handler,么一个handker都对应不同的处理
        ChannelPipeline pipeline = ch.pipeline();
        //增加到最后,httpServerCodec处理器名字,
        //zy HttpServerCodec是HttpRequestDecoder(请求到达服务器之后解码)和HttpResponseEncoder(返回给客户端的编码)组合
        pipeline.addLast("httpServerCodec",new HttpServerCodec());
        //bz 4 添加自定义处理
        pipeline.addLast("testHttpServerHandler",new TestHttpServerHandler());
    }
}
