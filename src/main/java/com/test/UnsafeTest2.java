package com.test;
/*

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class UnsafeTest2 {

    public static void main(String[] args) throws Exception {
        Unsafe finalUnsafe = reflectGetUnsafe();
        ByteBuffer direct = ByteBuffer.allocateDirect(1);

        final Field field = Buffer.class.getDeclaredField("address");
        // Use Unsafe to read value of the address field. This way it will not fail on JDK9+ which
        // will forbid changing the access level via reflection.
        final long offset = finalUnsafe.objectFieldOffset(field);
        final long address = finalUnsafe.getLong(direct, offset);
        System.out.println(address);
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
