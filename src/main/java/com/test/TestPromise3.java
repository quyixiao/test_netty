package com.test;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

import java.util.Date;

public class TestPromise3 {

    public static void main(String[] args) throws Exception {



       for(int i = 0 ;i < 2 ;i ++){

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
           promise.setSuccess( Thread.currentThread().getName() + " promise set ");//设置返回结果 并且通知所有Listeners执行回调方法operationComplete方法
       }
    }
}