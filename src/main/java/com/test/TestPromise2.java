package com.test;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.InternalThreadLocalMap;

import java.util.Date;

public class TestPromise2 {

    public static void main(String[] args) throws Exception {


        NioEventLoopGroup loopGroup = new NioEventLoopGroup();
        EventLoop next = loopGroup.next();
        Promise<String> promise = (Promise) next.terminationFuture();

        promise.addListener(new GenericFutureListener<Future<? super String>>() {
            @Override
            public void operationComplete(Future<? super String> future) throws Exception {
                Object s = future.get();
                System.out.println(new Date() + "listner1---promise的future返回值：" + s);

                ((DefaultPromise) future).addListener(new GenericFutureListener<Future<? super String>>() {
                    @Override
                    public void operationComplete(Future<? super String> future) throws Exception {
                        Object s = future.get();

                        final InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
                        final int stackDepth = threadLocals.futureListenerStackDepth();
                        System.out.println(new Date() + "listner1 inner ---promise的future返回值：" + s + ", stackDepth = " + stackDepth);


                        ((DefaultPromise) future).addListener(new GenericFutureListener<Future<? super String>>() {
                            @Override
                            public void operationComplete(Future<? super String> future) throws Exception {
                                final InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
                                final int stackDepth = threadLocals.futureListenerStackDepth();

                                Object s = future.get();
                                System.out.println(new Date() + "listner1 inner  inner ---promise的future返回值：" + s + ", stackDepth ="+stackDepth);
                            }
                        });


                    }
                });
            }
        });


        promise.setSuccess("promise set ");//设置返回结果 并且通知所有Listeners执行回调方法operationComplete方法
    }
}