package com.test;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Test3 {


    private static final AtomicLong nextTaskId = new AtomicLong();
    private static final long START_TIME = System.nanoTime();

    private static final long SCHEDULE_QUIET_PERIOD_INTERVAL = TimeUnit.SECONDS.toNanos(1);

    static long nanoTime() {
        return System.nanoTime() - START_TIME;
    }

    static long deadlineNanos(long delay) {
        long deadlineNanos = nanoTime() + delay;
        // Guard against overflow
        return deadlineNanos < 0 ? Long.MAX_VALUE : deadlineNanos;
    }




    public static void main(String[] args) {
        //System.out.println(Integer.toBinaryString(13));
        // 如数组长度为14
        //for(int i = 14;i < 28 ; i++){
//            System.out.println("i = "+ i+ " i & (array.length -1 ) = "+ (i & (14 -1 ) ));

//        }
        System.out.println(deadlineNanos(SCHEDULE_QUIET_PERIOD_INTERVAL));

         long SCHEDULE_QUIET_PERIOD_INTERVAL = TimeUnit.SECONDS.toNanos(1);
        System.out.println(SCHEDULE_QUIET_PERIOD_INTERVAL);
    }
}






