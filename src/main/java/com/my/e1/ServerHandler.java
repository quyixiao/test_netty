package com.my.e1;

import com.alibaba.fastjson.JSONObject;
import com.my.e2.RequestFuture;
import com.my.e2.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;


@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //if (msg instanceof ByteBuf) {
            // ByteBuf 的toString() 方法把二进制数据转换成字符串，默认编码为UTF-8
         //   System.out.println(((ByteBuf) msg).toString(Charset.defaultCharset()));
        //}
        //ctx.channel().writeAndFlush("msg has recived !");
        // 获取客户端发送的请求 ， 并将其转换成RequestFuture对象
        // 由于经过StringDecoder解码器，所以msg为String类型
        RequestFuture request = JSONObject.parseObject(msg.toString(),RequestFuture.class);
        // 获取请求id
        long id = request.getId();
        System.out.println("请求的信息为====" + msg.toString());
        // 构建响应结果
        Response response = new Response();
        response.setId(id);
        response.setResult("服务器响应ok");
        // 把响应结果返回给客户端
        ctx.channel().writeAndFlush(JSONObject.toJSONString(response));
        
    }


}
