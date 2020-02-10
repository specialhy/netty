package com.hy.nio;

import java.nio.ByteBuffer;

/***
 * 只读buffer 我们可以将一个普通的buffer调用asReadOnlyBuffer，返回一个只读buffer
 * 但不能将只读buffer转换为普通buffer
 */
public class NioTest7 {
    public static void main(String[] args) {
        //这里具体会返回一个HeapByteBuffer(堆上的buffer)
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        for (int i = 0; i < byteBuffer.capacity(); i++) {
            byteBuffer.put((byte)i);
        }
        //返回一个HeapByteBufferR(只读buffer),
        // HeapByteBufferR在所有写的方法都直接抛出异常，这样就不能写了，只能读
        ByteBuffer readByteBuffer1 = byteBuffer.asReadOnlyBuffer();

        System.out.println(readByteBuffer1.getClass());

        readByteBuffer1.position(0);

       // readByteBuffer1.put((byte) 3);//这句代码会抛异常
    }
}
