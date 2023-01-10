package com.test;

import java.util.HashSet;
import java.util.Set;

public class FastThreadLocalTest5 {

    public static void main(String[] args) {
        Set<Integer> set = new HashSet<>();
        for(int i = 10000;i <= 20000 ;i ++){
            set.add(newCapacity(i));

        }
        System.out.println(set);
    }


    public static int newCapacity(int newCapacity) {
        newCapacity |= newCapacity >>> 1;
        newCapacity |= newCapacity >>> 2;
        newCapacity |= newCapacity >>> 4;
        newCapacity |= newCapacity >>> 8;
        newCapacity |= newCapacity >>> 16;
        newCapacity++;
        return newCapacity;
    }
}
