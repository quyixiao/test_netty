package com.test;

import java.util.concurrent.CompletableFuture;

public class CallableFutureTest {

		
    public static void main(String[] args) {
        System.out.println("start");
      	/**
         * 异步非阻塞
         */
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("sleep done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("done");
    }
}