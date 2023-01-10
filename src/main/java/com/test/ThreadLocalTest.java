package com.test;

import org.springframework.boot.devtools.tunnel.client.TunnelClient;

public class ThreadLocalTest {


    public static void main(String[] args) {



        ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

        threadLocal.set(1);
        threadLocal.get();


    }
}
