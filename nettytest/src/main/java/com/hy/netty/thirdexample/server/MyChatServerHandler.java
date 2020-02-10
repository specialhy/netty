package com.hy.netty.thirdexample.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {

    //存放连接对象的容器
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /***
     * 发送的内容需要广播到其他客户端
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channels.forEach(ch ->{
            if(channel != ch){//如果不是发送消息的客户端,那么就发送
                ch.writeAndFlush(channel.remoteAddress()+"发送的消息:"+msg+"\n");
            }else{//否则就发送给自己
                ch.writeAndFlush("自己:"+msg+"\n");
            }
        });
    }

    /***
     * 加入到容器，并且广播到其他对象
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //获取Channel连接对象
        Channel channel = ctx.channel();
        //告诉其他连接对象某某已经上线,广播到其他连接对象,需要一个容器，获取其他连接对象
        //先广播到这个容器中的所有对象
        channels.writeAndFlush("[服务器] - "+channel.remoteAddress()+"加入\n");
        //将已经连接的对象放进容器
        channels.add(channel);
    }

    /***
     * 连接断掉之后,不用手动移除,会自动移除
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.writeAndFlush("[服务器] - "+channel.remoteAddress()+"离开\n");
        System.out.println(channels.size());
    }

    /***
     * 处于活动状态
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress()+"上线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress()+"下线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
