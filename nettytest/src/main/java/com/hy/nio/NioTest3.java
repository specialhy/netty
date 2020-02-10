package com.hy.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/***
 * 对于文件的写
 */
public class NioTest3 {
    public static void main(String[] args) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream("Nio.txt");
        FileChannel fileChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        byte[] b = "hello netty".getBytes();
        for(int i = 0;i < b.length;i++){
            byteBuffer.put(b[i]);
        }
        byteBuffer.flip();
        fileChannel.write(byteBuffer);
    }
}
