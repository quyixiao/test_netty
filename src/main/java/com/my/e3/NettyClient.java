package com.my.e3;

import com.alibaba.fastjson.JSONObject;
import com.my.e2.RequestFuture;
import com.my.e2.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;

import java.nio.charset.Charset;

public class NettyClient {

    public static EventLoopGroup group = null;
    public static Bootstrap bootstrap = null;

    static {
        // 客户端启动辅助类
        bootstrap = new Bootstrap();
        // 开启一个线程组
        group = new NioEventLoopGroup();
        // 设置一个socket通道
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
        // 设置内存配置器
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }

    public static void main(String[] args) throws Exception {
        // 新建一个promise对象
        Promise<Response> promise = new DefaultPromise<>(group.next());
        // 业务 Handler
        final ClientHandler handler = new ClientHandler();
        // 把promise对象赋值给handler , 用于获取返回服务器响应结果
        handler.setPromise(promise);
        // 把handler 对象加入到管道中
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                // 把接收到的ByteBuf数据包转换成String
                ch.pipeline().addLast(new StringDecoder());
                // 业务逻辑处理Handler
                ch.pipeline().addLast(handler);
                ch.pipeline().addLast(new LengthFieldPrepender(4, false));
                // 把字符口中 消息转换成ByteBuf
                ch.pipeline().addLast(new StringEncoder(Charset.forName("utf-8")));
            }
        });
        // 连接服务器
        ChannelFuture future = bootstrap.connect("127.0.0.1", 8080).sync();
        // 构建request请求
        RequestFuture request = new RequestFuture();
        // 设置请求id,此处请求id可以设置为自动自增模式
        // 可以采用AtomicLong类的incrementAndGet()方法
        request.setId(1l);
        // 请求消息内容，此处的内容可以是任意的java对象
        request.setRequest("hello world !");
        // 转换成JSON格式发送给编译器StringEncode
        // StringEncode 编码器再发送给LengthFieldPrepender长度编码器， 最终定到TCP缓存中，并传送给客户端
        String requestStr = JSONObject.toJSONString(request);
        future.channel().writeAndFlush(requestStr);
        // 同步阻塞等待响应结果
        Response response = promise.get();
        // 打印最终的的结果
        System.out.println(JSONObject.toJSONString(response));

    }
}
