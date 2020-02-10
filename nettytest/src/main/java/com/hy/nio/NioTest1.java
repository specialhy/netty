package com.hy.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

/***
 * 只是对于BUffer的读写
 */
public class NioTest1 {
    public static void main(String[] args) {
        //创建一个容量为10的Buffer,永远不会变化
        //返回一个HeapIntBuffer,只能在当前包下面使用
        IntBuffer buffer = IntBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity() ; i++) {
            //获取随机数
           int randomNumber = new SecureRandom().nextInt(30);
           //放入
            buffer.put(randomNumber);
        }
        //读写切换, 由写切换到读
        buffer.flip();
        //判断还有没有下一个
        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }

}
