package com.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class InEventLoopTest {

    volatile Thread thread;

    private final TaskRunner taskRunner = new TaskRunner();

    private final AtomicBoolean started = new AtomicBoolean();

    public final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();

    public static void main(String[] args) {
        InEventLoopTest eventLoop = new InEventLoopTest();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " 执行任务2 ");
            }
        }, "线程1 ");

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " 执行任务1 ");
                eventLoop.execute(thread1);
            }
        }, "线程2 ");
        eventLoop.execute(thread2);
    }

    public void execute(Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        addTask(task);
        if (!inEventLoop()) {
            startThread();
        } else {
            System.out.println(" 执行任务为同一线程，只将任务加入到队列中，并不会创建新的线程去执行 ");
        }
    }

    public void startThread() {
        if (started.compareAndSet(false, true)) {
            final Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    taskRunner.run();
                }
            }, "执行任务线程");

            thread = t;
            t.start();
        }
    }

    public void addTask(Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        taskQueue.add(task);
    }

    public boolean inEventLoop() {
        return inEventLoop(Thread.currentThread());
    }

    public boolean inEventLoop(Thread thread) {
        return thread == this.thread;
    }


    final class TaskRunner implements Runnable {
        @Override
        public void run() {
            for (; ; ) {
                Runnable task = taskQueue.poll();
                if (task != null) {
                    try {
                        task.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (task != null) {
                        continue;
                    }
                }
                if (taskQueue.isEmpty()) {
                    boolean stopped = started.compareAndSet(true, false);
                    assert stopped;
                    if (taskQueue.isEmpty()) {
                        break;
                    }
                    if (!started.compareAndSet(false, true)) {
                        break;
                    }
                }
            }
        }
    }


}
