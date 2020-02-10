package com.hy.nio;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/***
 * 对于文件的读写
 */
public class NioTest2 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream  = new FileInputStream("Nio.txt");
        //通过文件输入流获取通道对象
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(521);
        //将管道数据读到ByteBuffer 使用ByteBuffer来接受通道数据,对于ByteBuffer来说也相当于写到ByteBuffer中
        fileChannel.read(byteBuffer);
        //将ByteBuffer由写转换到读
        byteBuffer.flip();
        //读出来
        while (byteBuffer.remaining()>0){
            byte b = byteBuffer.get();
            System.out.println("Character: "+(char)b);
        }
        fileInputStream.close();
    }
}
