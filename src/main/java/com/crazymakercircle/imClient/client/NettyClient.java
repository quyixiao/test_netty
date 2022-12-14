package com.crazymakercircle.imClient.client;

import com.crazymakercircle.im.common.bean.User;
import com.crazymakercircle.im.common.codec.ProtobufDecoder;
import com.crazymakercircle.im.common.codec.ProtobufEncoder;
import com.crazymakercircle.imClient.handler.ChatMsgHandler;
import com.crazymakercircle.imClient.handler.ExceptionHandler;
import com.crazymakercircle.imClient.handler.LoginResponceHandler;
import com.crazymakercircle.imClient.sender.ChatSender;
import com.crazymakercircle.imClient.sender.LoginSender;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Data
@Service("NettyClient")
// 对于客户端的业务处理流水线，首先需要装配一个ProtobufDecoder解码器和一个ProtobufEncoder编码器，编码器和解码器一般都是装配在最前面
// 然后需要装配业务处理器一LoginResponseHandler 登录响应处理器的实例。
// 一般来说，在流水线最末端还需要装配一个ExceptionHandler 异常处理器，它也是一个入站处理器，用来实现Netty 异常的处理以及连接异常中断后
// 进行重连接
public class NettyClient {
    // 服务器ip地址
    private String host = "127.0.0.1";
    // 服务器端口
    private int port = 8080;


    @Autowired
    private ChatMsgHandler chatMsgHandler;

    @Autowired
    private LoginResponceHandler loginResponceHandler;


    @Autowired
    private ExceptionHandler exceptionHandler;


    private Channel channel;
    private ChatSender sender;
    private LoginSender l;

    /**
     * 唯一标记
     */
    private boolean initFalg = true;
    private User user;
    private GenericFutureListener<ChannelFuture> connectedListener;

    private Bootstrap b;
    private EventLoopGroup g;

    public NettyClient() {

        /**
         * 客户端的是Bootstrap，服务端的则是 ServerBootstrap。
         * 都是AbstractBootstrap的子类。
         **/

        /**
         * 通过nio方式来接收连接和处理连接
         */

        g = new NioEventLoopGroup();


    }

    /**
     * 重连
     */
    public void doConnect() {
        try {
            b = new Bootstrap();

            b.group(g);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.remoteAddress(host, port);

            // 设置通道初始化
            b.handler(
                    new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast("decoder", new ProtobufDecoder());
                            ch.pipeline().addLast("encoder", new ProtobufEncoder());
                            ch.pipeline().addLast(loginResponceHandler);
                            ch.pipeline().addLast(chatMsgHandler);
                            ch.pipeline().addLast(exceptionHandler);
                        }
                    }
            );
            log.info("客户端开始连接 [疯狂创客圈IM]");

            ChannelFuture f = b.connect();
            f.addListener(connectedListener);


            // 阻塞
            // f.channel().closeFuture().sync();

        } catch (Exception e) {
            log.info("客户端连接失败!" + e.getMessage());
        }
    }

    public void close() {
        g.shutdownGracefully();
    }


}
