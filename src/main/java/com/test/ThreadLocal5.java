package com.test;

import java.lang.reflect.Field;

public class ThreadLocal5 {


    public static void main(String[] args) throws Exception {
        ThreadLocal<LargeObject> threadLocal1 = new ThreadLocal<>();
        int threadLocal1HashCode = (int) getFieldValue(threadLocal1, "threadLocalHashCode");
        System.out.println("threadLocal1的索引值为：" + (threadLocal1HashCode & (getTableLength() -1 )));
        for (int i = 0; i < 15; i++) {
            methodThreadLocal();
        }
        ThreadLocal<LargeObject> threadLocal2 = new ThreadLocal<>();
        int threadLocal2HashCode = (int) getFieldValue(threadLocal2, "threadLocalHashCode");
        System.out.println("threadLocal2的索引值为：" + (threadLocal2HashCode & (getTableLength() -1 )));
    }

    public static int getTableLength() throws Exception{
        Object threadLocals = getFieldValue(Thread.currentThread(), "threadLocals");
        Object ob[] = (Object[]) getFieldValue(threadLocals, "table");
        System.out.println("ThreadLocalMap.table[]长度是" + ob.length);
        return ob.length;
    }

    public static void methodThreadLocal() throws Exception {
        ThreadLocal<LargeObject> threadLocal = new ThreadLocal<>();
        threadLocal.set(new LargeObject());
        threadLocal.get();
        // 【注意】：模拟系统gc
        int threadLocal1HashCode = (int) getFieldValue(threadLocal, "threadLocalHashCode");
        Object threadLocals = getFieldValue(Thread.currentThread(), "threadLocals");
        Object ob[] = (Object[]) getFieldValue(threadLocals, "table");

        Object  entry= ob[threadLocal1HashCode & (ob.length-1)];

        Field field = entry.getClass().getSuperclass().getSuperclass().getDeclaredField("referent");
        field.setAccessible(true);
        field.set(entry,null);
    }

    public static Object getFieldValue(Object threadLocal, String fieldName) throws Exception {
        Field field = threadLocal.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(threadLocal);


    }
}
