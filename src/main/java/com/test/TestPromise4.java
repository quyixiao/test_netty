package com.test;

import io.netty.util.concurrent.*;

import java.util.Date;

public class TestPromise4 {


    public static void main(String[] args) throws Exception {

        GlobalEventExecutor INSTANCE = new MyGlobalEventExecutor();
        for(int i = 0 ;i < 2 ;i ++){

            Promise<String> promise = new DefaultPromise<>(INSTANCE);
            promise.addListener(new GenericFutureListener<Future<? super String>>() {
                @Override
                public void operationComplete(Future<? super String> future) throws Exception {
                    Object s = future.get();
                    System.out.println(new Date() + "listner1---promise的future返回值：" + s);
                }
            });
            promise.setSuccess(Thread.currentThread().getName() + " promise set ");//设置返回结果 并且通知所有Listeners执行回调方法operationComplete方法


            Thread.sleep(10000);

        }




    }
}