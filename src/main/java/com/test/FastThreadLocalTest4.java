package com.test;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.InternalThreadLocalMap;

import java.lang.reflect.Field;

public class FastThreadLocalTest4 {




    public static void main(String[] args) throws Exception {

        InternalThreadLocalMap.nextVariableIndex();
        InternalThreadLocalMap.nextVariableIndex();
        InternalThreadLocalMap.nextVariableIndex();
        InternalThreadLocalMap.nextVariableIndex();

        FastThreadLocal fastThreadLocal = new FastThreadLocal();

        System.out.println("variablesToRemoveIndex = "+getFieldValue(fastThreadLocal , "variablesToRemoveIndex"));

        fastThreadLocal.set(10);

        System.out.println("index = " + getFieldValue(fastThreadLocal , "index"));


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
