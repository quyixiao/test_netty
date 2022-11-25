package com.my.e3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;

public class NettyServer {


    public static void main(String[] args) {
        /**
         * 创建两个线程组， Boss 线程级启动一条线程，监听 OP_ACCEPT事件
         *  WORKer 线程组默认启动核数 *2 的线程  *
         *  监听客户端连接的OP_READ 和 OP_WRITE事件，处理I/O事件
         * */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // ServerBootstrap 为Netty服务启动辅助类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);
            // 设置 TCP Socket 通道为NioServerSocketChannel
            // 如果是UDP通信 ， 则将其设置为DategramChannel
            serverBootstrap.channel(NioServerSocketChannel.class);
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
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0 ,4 ,0 ,4 ));
                            // 把接收到的ByteBuf 数据包转换成String
                            ch.pipeline().addLast(new StringDecoder());

                            /**
                             * 向Worker 线程管理双向链接中添加处理类ServerHandler
                             * 整个处理流向如下 ： HeadContext-channelRead读数据-->ServerHandler-channelRead
                             * 读取数据进行业务逻辑判断， 最后将结果返回给客户端 -->TailContextWrite
                             * -> HeadContext-write
                             */
                            ch.pipeline().addLast(new ServerHandler());
                            // 在消息体前面新增加4 个字节的长度值 ， 第一个参数的长度值占用字节数
                            // 第二个参数的长度值的调节，表明是否包含长度值本身
                            ch.pipeline().addLast(new LengthFieldPrepender(4,false));
                            // 把字符串消息转换成ByteBuf
                            // 注意解码器和编码器的顺序
                            // 两者的执行顺序正好相反 ，解码器执行顺序从上往下，编译器执行顺序从下往上

                        }
                    });
            // 同步绑定端口
            ChannelFuture future = serverBootstrap.bind(8080).sync();
            // 阻塞主线程，直到Socket 通道被关闭
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 最终关闭线程姐
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }


    }

}
