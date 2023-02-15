package com.test;

import io.netty.channel.SelectStrategy;

import java.util.concurrent.TimeUnit;

public class Testxx2 {


    public static void main(String[] args) {

        int normalizedCapacity = 512;
        normalizedCapacity --;
        //511 = 0000 0000 0000 0000 0000 0001 1111 1111
        System.out.println(Integer.toBinaryString(normalizedCapacity));
        System.out.println(normalizedCapacity >>>  1);

        normalizedCapacity |= normalizedCapacity >>>  1;

        System.out.println(normalizedCapacity);
        normalizedCapacity |= normalizedCapacity >>>  2;
        normalizedCapacity |= normalizedCapacity >>>  4;
        normalizedCapacity |= normalizedCapacity >>>  8;
        normalizedCapacity |= normalizedCapacity >>> 16;
        normalizedCapacity ++;

    }
}
