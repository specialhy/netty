package com.hy.netty.thirdexample.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyChatClients {
    public static void main(String[] args) throws Exception {
        Map<String,String> map = new HashMap<>();
        Map<String,Map<String,String>> maps = new HashMap<>();
        List<Map<String,Map<String,String>>> list = new ArrayList<>();

        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(eventExecutors).channel(NioSocketChannel.class)
                    .handler(new MyChatInitializer());//只需要handler不需要childhandler

            Channel channel = bootstrap.connect("localhost",8899).sync().channel();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            for(;;){
                channel.writeAndFlush(br.readLine()+"\r\n");
            }
        }finally {
            eventExecutors.shutdownGracefully();
        }


    }
}
