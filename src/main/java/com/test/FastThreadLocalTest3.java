package com.test;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.InternalThreadLocalMap;

import java.lang.reflect.Field;

public class FastThreadLocalTest3 {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FastThreadLocal fastThreadLocal0 = new FastThreadLocal();
                    System.out.println(Thread.currentThread().getName() + ":" + getFieldValue(fastThreadLocal0, "variablesToRemoveIndex"));
                    fastThreadLocal0.set(10);
                    System.out.println(Thread.currentThread().getName() + ":" + getFieldValue(fastThreadLocal0, "index"));


                    FastThreadLocal fastThreadLocal2 = new FastThreadLocal();
                    System.out.println(Thread.currentThread().getName() + ":" + getFieldValue(fastThreadLocal2, "variablesToRemoveIndex"));
                    fastThreadLocal2.set(20);
                    System.out.println(Thread.currentThread().getName() + ":" + getFieldValue(fastThreadLocal2, "index"));


                    FastThreadLocal fastThreadLocal3 = new FastThreadLocal();
                    System.out.println(Thread.currentThread().getName() + ":" + getFieldValue(fastThreadLocal3, "variablesToRemoveIndex"));
                    fastThreadLocal3.set(20);
                    System.out.println(Thread.currentThread().getName() + ":" + getFieldValue(fastThreadLocal3, "index"));


                    FastThreadLocal fastThreadLocal4 = new FastThreadLocal();
                    System.out.println(Thread.currentThread().getName() + ":" + getFieldValue(fastThreadLocal4, "variablesToRemoveIndex"));
                    fastThreadLocal4.set(20);
                    System.out.println(Thread.currentThread().getName() + ":" + getFieldValue(fastThreadLocal4, "index"));

                }
            }).start();
        }
        try {
            Thread.sleep(1000);

            FastThreadLocal fastThreadLocal = new FastThreadLocal();

            System.out.println(Thread.currentThread().getName() + ":" + getFieldValue(fastThreadLocal, "variablesToRemoveIndex"));

            fastThreadLocal.set(10);

            System.out.println("========" + Thread.currentThread().getName() + ":" + getFieldValue(fastThreadLocal, "index"));
            InternalThreadLocalMap internalThreadLocalMap = InternalThreadLocalMap.get();
            Field field = InternalThreadLocalMap.class.getSuperclass().getDeclaredField("indexedVariables");
            field.setAccessible(true);
            Object[] indexedVariables = (Object[]) field.get(internalThreadLocalMap);
            System.out.println(indexedVariables);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static Object getFieldValue(Object threadLocal, String fieldName) {
        try {
            Field field = threadLocal.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(threadLocal);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;

    }
}
