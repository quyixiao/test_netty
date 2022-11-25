package com.my.e2;

public class SubThread extends Thread {


    private RequestFuture request;

    public SubThread(RequestFuture future){
        this.request = future;
    }


    @Override
    public void run() {
        // 模拟额外线程获取响应结果
        Response resp = new Response();
        // 此处的id为请求id, 模拟服务器接收到请求后,将请求的id直接赋值给对象的id
        resp.setId(request.getId());
        //为响应结果赋值
        resp.setResult("server response " + Thread.currentThread().getId());
        // 子线程模拟睡眠1s
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 此处响应结果返回主线程
        RequestFuture.received(resp);
    }
}
