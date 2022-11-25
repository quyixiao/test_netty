package com.my.e2;

import java.util.ArrayList;
import java.util.List;

/**
 * 主线程类， 主线程模拟发送请求，开启额外线程模拟获取响应结果
 * 并异步的将响应响应结果返回给主线程
 */
public class FutureMain {

    public static void main(String[] args) {
        // 请求列表
        List<RequestFuture> reqs = new ArrayList<>();
        // 此处用for 循环模拟连接发送100个请求
        // 异步构建100条线程获取请求，并返回响应结果
        // 当然，此处还可以用线程池模拟构建100条线程发送请求
        // 然后主线程等待所有的子线程获取对应的响应结果，希望读者能对代码进行相应的修改
        for (int i = 0; i < 100; i++) {
            // 请求 id
            long id = i;
            // 构建请求对象
            RequestFuture req = new RequestFuture();
            req.setId(id);
            // 设置请求内容
            req.setRequest("hello world");
            // 请求缓存起来
            RequestFuture.addFuture(req);
            // 把请求加入请求列表
            reqs.add(req);
            //  模拟发送请求
            sendMsg(req);
            SubThread subThread = new SubThread(req);

            subThread.start();
        }

        for(RequestFuture req : reqs){
            // 主线程获取响应结果
            Object resut = req.get();
            // 输出结果
            System.out.println(resut.toString());
        }
    }


    private static void sendMsg(RequestFuture req) {
        System.out.println("客户端发送数据， 请求id 为 ==== " + req.getId());
    }

}
