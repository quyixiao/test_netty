package com.crazymakercircle.imServer.server;

import com.crazymakercircle.im.common.bean.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 实现服务器Socket Session会话
 */
@Data
@Slf4j
// 管理用户与通道的绑定关系
// 详解ServerSession 服务器会话
// 进化论是客户端还是服务器端，为了让通道 （Channel - 连接和用户 (User) 状态的管理使用变得方便，引入了一个非常重要的概念， 会话（Sesssion ））
// 有点类似于Tomcat 的服务器会话。
// 只是在实现上比较简单。
// 由于客户端和服务器分会都有各自的通道，并且相关的参数有一些了不一致， 因此这里使用了两个会话类型，客户端会话，服务端会话ServerSession 。
// 会话和通道直接的导航关系有两个方向，一个是下身导航，通过通道导航到会话，主要用于入站的处理的场景，通过通道获取舖和，以便进一步的进行业务处理
// 如果进行反向导航呢？ 需要用于通道的容器属性
// 通道的容器属性
// Netty 中的Cahannel通道类，有类似于Map的容器功能，可以通过key-value键值对的形式来保存任何 Java Object类型的值，一般来说，可以存放一些
// 与通道实例相关联的属性， 比如说服务器端的ServerSession 会话实例。
// 问题是，Channel 和 HandlerContext 的容器功能，具体是如何实现的呢？
// Netty 没有实现Map 接口， 而是定义了一个类似的接口，叫作AttributeMap,它有且只有一个办法 ，Attribute , attr 此方法接收一个AttributeKey
// 类型的key ，返回一个Attribute 类型的值，现特别说明一下
// 1. 这里的AttributeKey 也不是原始的Key,例如 放在Map 中的key ，而是一个key的包装类， AttributeKey 确保了Key的唯一性，在单个 Netty
// 应用中， Key 的值必须是唯一的。
// 2. 这里的值Attribute 不是原始的value，也是value的包装类，原始的value就放置在Attribute 包装类中， 可以通过Attribute 包装类实现
// value 的读取get和设置 set ，取值和设置 。
//
// 1. 通过ServerSession 实例可以导航到通道，以发送消息
// 在通道收到消息时，通过ServerSession  实例，反向导航到用户，以处理业务逻辑 。
// 需要注意的是SessionId 的类型，它是String 类型而不是数据类型，为什么呢？ 主要是为了使命后期的分布式扩展，在后期，通信服务器需要从单体服务器扩展到多节点的服务器集群
// 为了在分布式集群的应用场景下，在后期sessionId 可以确保全局唯一，使用String 类型比较方便 。
// 从功能上来看， ServerSession和clientSession 类似，也是一个很重要的胶水类
// 通过ServerSession 实例可以导航到用户通道，以发送消息
// 在通道收到消息时， 通过ServerSession实例反向导航到用户，以处理业务逻辑 ， 需要注意的是sessionId 的类型，它是String 类型，现时不是数据类型，
// 为什么呢？ 主要是为了方便后期的分布式扩展，在后期，通信服务器需要从单体服务器扩展到多节点的服务集群中，为了在分布式集群的应用场景下，在后期Session
// SessionID 可以确保全局唯一，使用String 类型比较方便 。
//
public class ServerSession {


    public static final AttributeKey<String> KEY_USER_ID =
            AttributeKey.valueOf("key_user_id");

    public static final AttributeKey<ServerSession> SESSION_KEY =
            AttributeKey.valueOf("SESSION_KEY");


    /**
     * 用户实现服务端会话管理的核心
     */
    //通道
    private Channel channel;
    //用户
    private User user;

    //session唯一标示
    private final String sessionId;

    //登录状态
    private boolean isLogin = false;

    /**
     * session中存储的session 变量属性值
     */
    private Map<String, Object> map = new HashMap<String, Object>();

    public ServerSession(Channel channel) {
        this.channel = channel;
        this.sessionId = buildNewSessionId();
    }

    //反向导航
    public static ServerSession getSession(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        return channel.attr(ServerSession.SESSION_KEY).get();
    }

    //关闭连接
    public static void closeSession(ChannelHandlerContext ctx) {
        ServerSession session =
                ctx.channel().attr(ServerSession.SESSION_KEY).get();

        if (null != session && session.isValid()) {
            session.close();
            SessionMap.inst().removeSession(session.getSessionId());
        }
    }

    //和channel 通道实现双向绑定
    public ServerSession bind() {
        log.info(" ServerSession 绑定会话 " + channel.remoteAddress());
        channel.attr(ServerSession.SESSION_KEY).set(this);
        SessionMap.inst().addSession(getSessionId(), this);
        isLogin = true;
        return this;
    }

    public ServerSession unbind() {
        isLogin = false;
        SessionMap.inst().removeSession(getSessionId());
        this.close();
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    private static String buildNewSessionId() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    public synchronized void set(String key, Object value) {
        map.put(key, value);
    }


    public synchronized <T> T get(String key) {
        return (T) map.get(key);
    }


    public boolean isValid() {
        return getUser() != null ? true : false;
    }

    //写Protobuf数据帧
    public synchronized void writeAndFlush(Object pkg) {
        channel.writeAndFlush(pkg);
    }

    //关闭连接
    public synchronized void close() {
        ChannelFuture future = channel.close();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    log.error("CHANNEL_CLOSED error ");
                }
            }
        });
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        user.setSessionId(sessionId);
    }


}
