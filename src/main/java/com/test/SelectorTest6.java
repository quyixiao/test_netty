package com.test;

import jdk.management.resource.internal.inst.SocketOutputStreamRMHooks;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class SelectorTest6 {

    public static boolean stop = false;

    // NIO中的Selector封装了底层的系统调用，其中wakeup用于唤醒阻塞在select方法上的线程，它的实现很简单，
    // 在linux上就是创建一 个管道并加入poll的fd集合，wakeup就是往管道里写一个字节，那么阻塞的poll方法有数据可读就立即返回。
    // 证明这一点很简单，strace即 可知道：
    public static void main(String[] args) throws Exception {
        Selector selector = Selector.open();
        long start = System.currentTimeMillis();
        FutureTask<Boolean> futureTask = new FutureTask<Boolean>(new MyCallable(selector));
        Thread thread = new Thread(futureTask);
        thread.start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                for (;;) {
                    if (stop) {
                        break;
                    }
                    selector.wakeup();
                }
            }
        }).start();



        stop = futureTask.get();
        Utils.print("结束 : " + (System.currentTimeMillis() - start));
    }
    static class MyCallable implements Callable<Boolean> {
        public Selector selector;
        public MyCallable(Selector selector) {
            this.selector = selector;
        }
        @Override
        public Boolean call() throws Exception {
            for (int i = 0; i < 100000; i++) {
                try {
                    selector.select(50000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    }
}
