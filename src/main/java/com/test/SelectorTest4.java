package com.test;

import java.nio.channels.Selector;

public class SelectorTest4 {

    // NIO中的Selector封装了底层的系统调用，其中wakeup用于唤醒阻塞在select方法上的线程，它的实现很简单，
    // 在linux上就是创建一 个管道并加入poll的fd集合，wakeup就是往管道里写一个字节，那么阻塞的poll方法有数据可读就立即返回。
    // 证明这一点很简单，strace即 可知道：
    public static void main(String[] args) throws Exception {
        Selector selector = Selector.open();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Utils.print("先执行唤醒操作");
                selector.wakeup();
            }
        });
        thread.start();
        thread.join();

        Utils.print("第一次selectNow调用");
        selector.selectNow();
        Utils.print("第二次select调用");
        selector.select();
        Utils.print("程序执行");
    }
}
