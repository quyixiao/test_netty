package com.fujian.e1;
/*

import com.my.e1.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;

public class Test1 {


    public static void main(String[] args) {

        // 水平触发，当被监控的文件描述符上有可能读写事件时，通知用户去读写，如果用户一次没有读写完数据，就一直通知用户，在用户确实不怎么关心这个文件描述符情况
        // 下，频繁通知用户会导致用户真正关心的那些文件描述符的处理效率降低 。
        // 边缘触发，当被监控的文件描述符上有可读写事件时，通知用户去读写，但只通知一次，这就需要用户一次性的把数据读取完，如果用户没有一次性
        // 的读写完数据，那就需要等待下一次新数据到来，才能读写上次未读写完的数据 。
        //
        OioEventLoopGroup eventLoopGroup = new OioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 什么时候会创建 OioServerSocketChannel 呢？当服务器启动时，就会通过AbstractBootStrap#initAndRegister方法来创建
            // OioServerSocketChannel
            serverBootstrap.channel(OioServerSocketChannel.class);
            serverBootstrap.group(eventLoopGroup);
            // 设置一些TCP 参数
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128)
                    // 当有客户端链路注册读写事件时，初始化Handler
                    // 阈将Handler 加入管道
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 新增，以前缀4B 的int 类型作为中作为长度解码器
                            // 第一个参数是包的最大长度，第二个参数长度值偏移量， 由于编码时长度值在最前面，无偏移，所以这里设置为0
                            // 第三个参数是长度值占用的字节数
                            // 第四个参数是长度值的调节，假设请求包的大小是20B
                            // 若长度值不包含本身则应该是20 B  ，若长度值包含本身则应该是24B， 需要调整4个字节
                            // 第五个参数是在解析时需要跳过的字节数 （此处为4 ）
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            // 把接收到的ByteBuf 数据包转换成String
                            ch.pipeline().addLast(new StringDecoder());

                            */
/**
                             * 向Worker 线程管理双向链接中添加处理类ServerHandler
                             * 整个处理流向如下 ： HeadContext-channelRead读数据-->ServerHandler-channelRead
                             * 读取数据进行业务逻辑判断， 最后将结果返回给客户端 -->TailContextWrite
                             * -> HeadContext-write
                             *//*

                            ch.pipeline().addLast(new ServerHandler());
                            // 在消息体前面新增加4 个字节的长度值 ， 第一个参数的长度值占用字节数
                            // 第二个参数的长度值的调节，表明是否包含长度值本身
                            ch.pipeline().addLast(new LengthFieldPrepender(4, false));
                            // 把字符串消息转换成ByteBuf
                            // 注意解码器和编码器的顺序
                            // 两者的执行顺序正好相反 ，解码器执行顺序从上往下，编译器执行顺序从下往上

                        }
                    });
            // 同步绑定端口
            ChannelFuture future = serverBootstrap.bind(8080);
            // 阻塞主线程，直到Socket 通道被关闭
            future.channel().closeFuture();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();

        }


    }
}
*/
