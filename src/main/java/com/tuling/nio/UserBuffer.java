package com.tuling.nio;

import java.nio.IntBuffer;

public class UserBuffer {


    public static void main(String[] args) {
        // 调用allocate 方法，而不是new
        IntBuffer intBuffer = IntBuffer.allocate(20);
        for(int i = 0 ; i < 5 ;i ++){
            // 写入一个整数到缓冲区
            intBuffer.put(i);

        }
        // 输出buffer的主要属性值
        System.out.println("after allocat =====" );
        System.out.println("position = " + intBuffer.position());
        System.out.println("limit = " + intBuffer.limit());
        System.out.println("capacity = " + intBuffer.capacity());

        intBuffer.flip();

        System.out.println("==========after flip =====" );
        System.out.println("position = " + intBuffer.position());
        System.out.println("limit = " + intBuffer.limit());
        System.out.println("capacity = " + intBuffer.capacity());


        intBuffer.clear();
        System.out.println("==========after clear =====" );
        System.out.println("position = " + intBuffer.position());
        System.out.println("limit = " + intBuffer.limit());
        System.out.println("capacity = " + intBuffer.capacity());

    }


}
