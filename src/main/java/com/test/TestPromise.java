package com.test;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

import java.util.Date;

public class TestPromise {

    public static void main(String[] args) throws Exception {
        NioEventLoopGroup loopGroup = new NioEventLoopGroup();
        EventLoop next = loopGroup.next();
        Promise<String> promise = (Promise) next.terminationFuture();

        promise.addListener(new GenericFutureListener<Future<? super String>>() {
            @Override
            public void operationComplete(Future<? super String> future) throws Exception {
                Object s = future.get();
                System.out.println(new Date() + "listner1---promise的future返回值：" + s);
            }
        });

        promise.addListener(new GenericFutureListener<Future<? super String>>() {
            @Override
            public void operationComplete(Future<? super String> future) throws Exception {
                Object s = future.get();
                System.out.println(new Date() + "listner2---promise的future返回值：" + s);
            }
        });

        next.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(new Date() + "execute异步执行开始");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(new Date() + "execute异步执行结束");
                promise.setSuccess("promise set ");//设置返回结果 并且通知所有Listeners执行回调方法operationComplete方法
            }
        });

        System.out.println(new Date() + "--------主线程的打印");


        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(new Date() + "--------多线程的打印");
                try {
                    promise.sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(new Date() + "--------多线程继续运行");
            }
        }).start();
        System.out.println(new Date() + "--------主线程继续运行");
    }
}