package com.crazymakercircle.imClient.client;

import com.crazymakercircle.im.common.bean.User;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 实现客户端 Session会话
 */
@Slf4j
@Data
// ClientSession是一个非常重要的脱水类， 有两个成员，一个是user代表用户，另一个是channel 代表了连接通道，在实际开发中， 这两个成员的作用是
// 1 . 通过user， 可以获取当前用户的信息
// 2. 通过channel,可以向服务器端发送消息
// ClientSession 会话 左拥右抱， 左手拥有用户消息，右手拥有服务器端的连接，通过user 成员可以获取当前的用户信息，借助channel通道，ClientSession
// 可以写入Protobuf数据包。 或者关闭Netty 连接
// 其次， 客户端会话ClientSession 保存着当前的状态
// 1. 是否成功连接isConnected
// 2. 是否成功合建isLogin
// 第三，ClientSession 绑定在通道上，因而可以在入站处理器中通过通道反射取得绑定的ClientSession
public class ClientSession {


    public static final AttributeKey<ClientSession> SESSION_KEY =
            AttributeKey.valueOf("SESSION_KEY");


    /**
     * 用户实现客户端会话管理的核心
     */
    private Channel channel;
    private User user;

    /**
     * 保存登录后的服务端sessionid
     */
    private String sessionId;

    private boolean isConnected = false;
    private boolean isLogin = false;

    /**
     * session中存储的session 变量属性值
     */
    private Map<String, Object> map = new HashMap<String, Object>();

    //绑定通道
    public ClientSession(Channel channel) {
        this.channel = channel;
        this.sessionId = String.valueOf(-1);
        channel.attr(ClientSession.SESSION_KEY).set(this);
    }

    //登录成功之后,设置sessionId
    public static void loginSuccess(
            ChannelHandlerContext ctx, ProtoMsg.Message pkg) {
        Channel channel = ctx.channel();
        ClientSession session = channel.attr(ClientSession.SESSION_KEY).get();
        session.setSessionId(pkg.getSessionId());
        session.setLogin(true);
        log.info("登录成功");
    }

    //获取channel
    public static ClientSession getSession(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        ClientSession session = channel.attr(ClientSession.SESSION_KEY).get();
        return session;
    }

    public String getRemoteAddress() {
        return channel.remoteAddress().toString();
    }

    //写protobuf 数据帧
    public ChannelFuture witeAndFlush(Object pkg) {
        ChannelFuture f = channel.writeAndFlush(pkg);
        return f;
    }

    public void writeAndClose(Object pkg) {
        ChannelFuture future = channel.writeAndFlush(pkg);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    //关闭通道
    public void close() {
        isConnected = false;

        ChannelFuture future = channel.close();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    log.error("连接顺利断开");
                }
            }
        });
    }


}
