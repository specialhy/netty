package com.hy.nio;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/***
 * 锁住文件
 */
public class NioTest10 {
    public static void main(String[] args) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("Nio10","rw");
            FileChannel fileChannel = randomAccessFile.getChannel();
            //设置锁从第三个位置到第六个位置true共享锁(都能读) false排它锁(只能有一个程序在写,可以多个一起读)
            FileLock fileLock = fileChannel.lock(3,6,true);
            //有效性,
            System.out.println("valid:"+fileLock.isValid());
            //锁的类型
            System.out.println("lock type"+fileLock.isShared());
            //解锁
            fileLock.release();
            randomAccessFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
