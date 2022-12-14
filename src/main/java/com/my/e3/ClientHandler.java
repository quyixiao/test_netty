package com.my.e3;

import com.alibaba.fastjson.JSONObject;
import com.my.e2.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Promise;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private Promise<Response> promise;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //读取服务端返回的响应结果 ， 并将其转换成response 对象
        // 由于经过StringDecoder 解码器所以msg为String类型
        Response response  = JSONObject.parseObject(msg.toString(),Response.class);
        // 设置响应结果并唤醒主线程
        promise.setSuccess(response);

        

    }


    public Promise<Response> getPromise() {
        return promise;
    }

    public void setPromise(Promise<Response> promise) {
        this.promise = promise;
    }
}
