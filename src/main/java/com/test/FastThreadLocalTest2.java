package com.test;

import io.netty.util.concurrent.FastThreadLocal;

public class FastThreadLocalTest2 {


    public static void main(String[] args) {
       /* final FastThreadLocal<String> threadLocal = new FastThreadLocal<String>();
        threadLocal.set("1");

        System.out.println(threadLocal.get());

        threadLocal.remove();;
        FastThreadLocal.removeAll();
*/


        FastThreadLocal<Boolean> threadLocal = new FastThreadLocal<Boolean>() {
            @Override
            protected Boolean initialValue() {
                return Boolean.TRUE;
            }

            @Override
            protected void onRemoval(Boolean value) {
                System.out.println("移除的value值是" + value);
            }
        };
        System.out.println(threadLocal.get());

        threadLocal.remove();

        FastThreadLocal.removeAll();

    }

}
