package com.hy.netty.firstexample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/***
 * bz 3 自己的处理器,这里是接受所以是SimpleChannelInboundHandler，不是SimpleChannelOutboundHandler
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /***
     * 读取客户端请求，并响应客户端
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        System.out.println(msg.getClass());
        //获取端口
        System.out.println(ctx.channel().remoteAddress());
        Thread.sleep(8000);
        if(msg instanceof HttpRequest) {//使用浏览器访问不会报错,但是使用控制台的curl访问会报错，但是加上这个判断就不会报错
           System.out.println("Client:" + msg);
           //转换,方便下面取值
           HttpRequest httpRequest = (HttpRequest)msg;
           System.out.println("请求方法名:"+httpRequest.method().name());
           //获取请求的url
           URI url = new URI(httpRequest.uri());
           //请求是这个的时候就不执行下面代码
           if("/favicon.ico".equals(url.getPath())){
               System.out.println("请求favicon.ico");
               return;
           }
           //向客户端返回的内容 ByteBuf比ByteBuffer性能更好一些,这是netty的
           ByteBuf content = Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8);
           //设置响应的协议,状态和内容
           FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
           //设置头信息
           response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
           //响应多少字节
           response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
           //返回回去,只是write的话没有返回只是放在缓冲区,必须在flush才可以
           ctx.writeAndFlush(response);
           //关闭连接,一般需要判断是http1.0还是1.1，加上超时时间
           ctx.channel().close();
       }
    }

    //连接处于活动状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        super.channelActive(ctx);
    }

    //注册
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered");
        super.channelRegistered(ctx);
    }

    //建立
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded");
        super.handlerAdded(ctx);
    }

    //连接不活动
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
        super.channelInactive(ctx);
    }

    //连接取消阻塞
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered");
        super.channelUnregistered(ctx);
    }

    //连接失去
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved");
        super.handlerRemoved(ctx);
    }
}
