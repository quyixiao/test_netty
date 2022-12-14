package com.my.e2;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


@Data
public class RequestFuture {
    // 请求缓存类, key为每次请求的id,value 请求对象
    public static Map<Long, RequestFuture> futures = new ConcurrentHashMap<>();
    // 对每次请求的id ，可以设置原子性增长
    private Long id;
    // 请求参数
    private Object request;
    // 响应结果
    private volatile Object result;
    // 超时时间默认为5s
    private long timeout = 1000;

    private static final AtomicLong aid = new AtomicLong(1);

    public RequestFuture() {
        // 当前值新增加1，并将结果返回给id
        this.id = aid.incrementAndGet();
        // 在构建请求时，需要把请求加入到缓存中
        addFuture(this);
    }

    // 将请求放入到缓存中
    public static void addFuture(RequestFuture future) {
        futures.put(future.getId(), future);
    }

    // 同步获取响应结果
    public Object get() {
        // 此处可以把同步块与wait换成 REentrantLock与Condition
        synchronized (this) {
            while (this.result == null) {
                try {
                    // 主线程默认等待5 s ,然后查看是否获取到结果
                    this.wait(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return this.result;
    }

    // 异步线程将结果返回给主线程
    public static void received(Response resp){
        RequestFuture future = futures.remove(resp.getId());
        // 设置响应结果
        if(future !=null){
            future.setResult(resp.getResult());
        }
        // 通知主线程
        synchronized (future){
            future.notify();
        }
    }

}
