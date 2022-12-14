package com.my.e3;

import com.alibaba.fastjson.JSONObject;
import com.my.e2.RequestFuture;
import com.my.e2.Response;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


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
        String xxx = JSONObject.toJSONString(response);
        ctx.channel().writeAndFlush(xxx);
        
    }


    // Netty 客户端线程模型比服务端线程模型简单一些， 它只需要一个线程组，底层采用了Java 的 NIO , 通过IP 和商品连接目标服务器， 请求发送和
    // 如果没有参数据流进行一些额外的加工处理， 那么将无法区分每次请求的数据包， 编码是指在传输数据前， 对数据包进行加工处理，解码发生在读取数据包时
    //
}
