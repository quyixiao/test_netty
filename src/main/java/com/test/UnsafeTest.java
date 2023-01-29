package com.test;
/*

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeTest {

    public static void main(String[] args) {
        Unsafe unsafe = reflectGetUnsafe();

        long byteArrayIndexScale = unsafe.arrayIndexScale(byte[].class);
        System.out.println(byteArrayIndexScale);

        long intArrayIndexScale = unsafe.arrayIndexScale(int[].class);
        System.out.println(intArrayIndexScale);

        long longArrayIndexScale = unsafe.arrayIndexScale(long[].class);
        System.out.println(longArrayIndexScale);
    }



    private static Unsafe reflectGetUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

*/
