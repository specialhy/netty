package com.hy.nio;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

/***
 * ByteBuffer类型化的put与get
 */
public class NioTest5 {

    public static void main(String[] args) {
        ByteBuffer buffe = ByteBuffer.allocate(64);
        buffe.putInt(15);
        buffe.putLong(400000L);
        buffe.putDouble(14.12354);
        buffe.putFloat(14);
        buffe.putChar('我');
        buffe.putShort((short) 2);
        buffe.putChar('你');

        buffe.flip();

        System.out.println(buffe.getInt());
        System.out.println(buffe.getLong());
        System.out.println(buffe.getChar());
        System.out.println(buffe.getFloat());
        System.out.println(buffe.getChar());
        System.out.println(buffe.getShort());
        System.out.println(buffe.getChar());



    }
}
