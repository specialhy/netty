package com.hy.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest8 {
    public static void main(String[] args) {
        try {
            FileInputStream inputStream = new FileInputStream("input.txt");
            FileOutputStream outputStream = new FileOutputStream("output.txt");

            FileChannel inputchannel = inputStream.getChannel();
            FileChannel outputchannel = outputStream.getChannel();

            //这种是通过一个byte数据创建一个buffer，这个buffer里面存放了这个byte数组,
            // 这种方式可以通过修改byte数组方式也可以通过修修改byteBuffer方式修改数据
            /*byte[] b = {1,23,45,65,78};
            ByteBuffer byteBuffer = ByteBuffer.wrap(b);*/

            //这个返回的是
           ByteBuffer directbyteBuffer =ByteBuffer.allocateDirect(1024);

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
