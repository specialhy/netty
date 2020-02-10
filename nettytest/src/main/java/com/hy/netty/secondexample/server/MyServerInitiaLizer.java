package com.hy.netty.secondexample.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class MyServerInitiaLizer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //解码 把一些二进制解码为真正的数据
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
        //解码一部分
        pipeline.addLast(new LengthFieldPrepender(4));
        //字符串解码
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        //字符串编码
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        //自己的处理
        pipeline.addLast(new MyServerHandler());
    }
}
