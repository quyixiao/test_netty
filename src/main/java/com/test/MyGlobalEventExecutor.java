package com.test;

import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.concurrent.ScheduledFutureTask;
import io.netty.util.internal.ThreadExecutorMap;

import java.util.concurrent.Executors;

public class MyGlobalEventExecutor extends GlobalEventExecutor {

    ScheduledFutureTask<Void> quietPeriodTaskNew = new ScheduledFutureTask<Void>(
            this, Executors.<Void>callable(new Runnable() {
        @Override
        public void run() {
            // NOOP
        }
    }, null), ScheduledFutureTask.deadlineNanos(SCHEDULE_QUIET_PERIOD_INTERVAL));

    public MyGlobalEventExecutor() {
        scheduledTaskQueue().remove();
        scheduledTaskQueue().add(quietPeriodTaskNew);
        threadFactory = ThreadExecutorMap.apply(new DefaultThreadFactory(
                DefaultThreadFactory.toPoolName(getClass()), false, Thread.NORM_PRIORITY, null), this);
    }

}
