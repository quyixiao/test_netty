package com.test;

import java.nio.channels.Selector;

public class SelectorTest5 {

    // NIO中的Selector封装了底层的系统调用，其中wakeup用于唤醒阻塞在select方法上的线程，它的实现很简单，
    // 在linux上就是创建一 个管道并加入poll的fd集合，wakeup就是往管道里写一个字节，那么阻塞的poll方法有数据可读就立即返回。
    // 证明这一点很简单，strace即 可知道：
    public static void main(String[] args) throws Exception {
        Selector selector = Selector.open();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            selector.selectNow();
        }
        Utils.print("结束 : " + (System.currentTimeMillis() - start));
    }
}
