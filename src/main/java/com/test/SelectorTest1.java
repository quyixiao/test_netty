package com.test;

import java.nio.channels.Selector;

public class SelectorTest1 {


    public static void main(String[] args) throws Exception {
        Selector selector = Selector.open();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Utils.sleep(2000);
                Utils.print("开始唤醒操作");
                selector.wakeup();
            }
        }).start();
        Utils.print("开始阻塞");
        selector.select();
        Utils.print("程序执行");
    }
}
