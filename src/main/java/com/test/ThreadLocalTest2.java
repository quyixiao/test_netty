package com.test;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLocalTest2 {

    private final int threadLocalHashCode = nextHashCode();


    private static AtomicInteger nextHashCode =
            new AtomicInteger();


    private static final int HASH_INCREMENT = 0x61c88647;


    private static int nextHashCode() {
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }


    public static void main(String[] args) {

        for (int i = 0; i < 35; i++) {
            ThreadLocalTest2 threadLocalTest2 = new ThreadLocalTest2();
            int b = threadLocalTest2.threadLocalHashCode;
            System.out.println(b & (16 - 1));
        }


    }
}
