package com.test;

public class ThreadTest4 {

    public static void main(String[] args) {
        int n = 16;


        do {
            System.out.println(n);
        } while ((n >>>= 1) != 0);

    }
}
