package com.hy.netty.secondexample.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;


/***
 * 这个地方的泛型很重要,这里定义了只是传输String，所以写String，如果要传输其他的，那要写成范围更大的
 * 像是HttpObject,就可以传输HttpRequest和其他的类型
 */

public class MyServerHandler extends SimpleChannelInboundHandler<String> {

    /***
     *
     * @param ctx 上下文的信息,可以获得远程的地址,连接对象是什么
     * @param msg 获取到传输的信息是什么
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("地址:"+ctx.channel().remoteAddress()+",消息:"+msg);
        //获取到与客户端连接的通道,发送数据
        ctx.channel().writeAndFlush("from server"+UUID.randomUUID());
    }

    /***
     * 出现异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }
}
