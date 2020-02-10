package com.hy.netty.fourthexample.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/***
 * ChannelInboundHandlerAdapter 这个事件触发之后就会转发给下一个handler
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //如果这个事件是一个空闲状态
        if(evt instanceof IdleStateEvent){
           //强制类型转换
           IdleStateEvent event = (IdleStateEvent)evt;
           String eventType = null;
           //判断状态
           switch (event.state()){
               case READER_IDLE:
                   eventType = "dukonxian";
               case WRITER_IDLE:
                   eventType = "xiekonxian";
               case ALL_IDLE:
                   eventType = "duxiekonxian";
           }
            System.out.println(ctx.channel().remoteAddress()+"chaoshishijian:"+eventType);
           //关闭连接
           ctx.channel().close();
        }
    }
}
