package com.hy.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest4 {
    public static void main(String[] args) {
        try {
            FileInputStream inputStream = new FileInputStream("input.txt");
            FileOutputStream outputStream = new FileOutputStream("output.txt");

            FileChannel inputchannel = inputStream.getChannel();
            FileChannel outputchannel = outputStream.getChannel();

            ByteBuffer byteBuffer =ByteBuffer.allocate(1024);
            int read=0;
            while((read = inputchannel.read(byteBuffer)) != -1){
                byteBuffer.flip();

                outputchannel.write(byteBuffer);
                byteBuffer.clear();//想想把这个注释掉会怎么样
            }
            inputchannel.close();
            outputchannel.close();
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
