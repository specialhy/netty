package com.hy.nio;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/***
 * slice 截取的创建一个新的,这个新的Buffer相当于原有Buffer的快照,也相当于数据只有一份，共享数据,但是拥有自己的三个重要数据,
 */
public class NioTest6 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        for (int i =0;i<buffer.capacity();++i){
            buffer.put((byte)i);
        }
        //截取2到6
        buffer.position(2);
        buffer.limit(6);
        //通过截取的创建一个新的,这个新的Buffer相当于原有Buffer的快照,
        //也相当于数据只有一份，共享数据,但是拥有自己的三个重要数据
        ByteBuffer buffer1 = buffer.slice();
        //这里修改一下然后放进去
        for (int i = 0; i < buffer1.capacity(); ++i) {
            byte b = buffer1.get(i);
            b*=2;
            buffer1.put(i,b);
        }

        //将其归为
        buffer.position(0);
        buffer.limit(buffer.capacity());
        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }
}
