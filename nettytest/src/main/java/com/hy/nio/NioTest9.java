package com.hy.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/***
 * 内存映射文件，一个文件的内存映射区域,通过Filechannel,我不需要和文件交互,直接和内存交互，结果会映射到磁盘的文件
 * 可以将文件的一部分或全部映射到堆外内存,应用程序只需要操作堆外内存
 */
public class NioTest9 {
    public static void main(String[] args) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("Nio9.txt","rw");
            FileChannel fileChannel = randomAccessFile.getChannel();

            //得到内存映射  映射模式(读,读写) 从哪个位置  映射多少
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE,0,5);
            //修改内存中的信息,相当于修改文件的值
            mappedByteBuffer.put(0,(byte) 'a');
            mappedByteBuffer.put(3,(byte) 'b');

            randomAccessFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
