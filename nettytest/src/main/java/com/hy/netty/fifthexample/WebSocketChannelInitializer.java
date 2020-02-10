package com.hy.netty.fifthexample;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //HttpServerCodec是HttpRequestDecoder(请求到达服务器之后解码)
        // 和HttpResponseEncoder(返回给客户端的编码)组合
        pipeline.addLast(new HttpServerCodec());
        //以块的方式写
        pipeline.addLast(new ChunkedWriteHandler());
        /***
         *  nettty会将客户端对于服务端的请求分段，比如1000，每100分成一段,
         *  也就是十段,每一段都会走一个完整的流程,每一次channelRead0都会调用一段,
         *  把十个段聚合到一起形成一个完整的请求
         */
         pipeline.addLast(new HttpObjectAggregator(8192));
        /***
         * 对于websocket的操作,负责WenSocket握手，以及对于frame(帧)(对于websocket来说,数据都是以frame(帧)形式进行
         *  (像是closeWebSocketFrame,pingWebSocketFrame,pongWebSocketFrame,文本的(TextWebSocketFrame),
         * 二进制的(BinaryWebSocketFrame),ContinuationWebSocketFrame(是数据没发送完，还会继续传递))
         * 处理,也有可能一帧发送完毕，也可能多帧发送完毕(也就是上一行代码nettty会将客户端对于服务端的请求分段)
         * (这里的ping,pong就是相当于心跳的处理,发起方ping,接收端pong),文本的，二进制的都会被传递给下一个处理器(TextWebSocketFrameHandler)进行处理
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        pipeline.addLast(new TextWebSocketFrameHandler());

    }
}
