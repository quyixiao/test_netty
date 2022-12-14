package com.my.e4;

import com.alibaba.fastjson.JSONObject;
import com.my.e2.RequestFuture;
import com.my.e2.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Promise;
import lombok.Data;


@Data
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private Promise<Response> promise;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //读取服务端返回的响应结果 ， 并将其转换成response 对象
        // 由于经过StringDecoder 解码器所以msg为String类型
        Response response  = JSONObject.parseObject(msg.toString(),Response.class);


        RequestFuture.received(response);




        // 设置响应结果并唤醒主线程
      //   promise.setSuccess(response);

    }



}
