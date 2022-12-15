package com.crazymakercircle.imServer.handler;

import com.crazymakercircle.cocurrent.FutureTaskScheduler;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imServer.server.ServerSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/

/**
 *
 * 服务器端的空闲检测
 * 想解决空闲检测，服务器端的有效手段是-空闲检测
 * 何为空闲检测呢？就是每隔一段时间，检测子通道是否有数据读写， 如果有，则子通道是正常的，如果没有，子通道被判断为假死，关掉子通道
 * 服务器端如何实现空闲检测呢？ 使用Netty 自带的IdleStateHandler 空闲状态处理器就可以实现这个功能，下面示例程序继承自IdleStateHandler，
 * 定义了一个假死的处理类。
 *
 *
 *
 *
 * HeartBeatServerHandler 实现主要功能是空闲检测，需要客户端定时发送心跳数据包， 或者报文消息，进行配合，而且客户端发送心跳数据包的时间
 * 间隔需要远远小于服务器端的空闲检测时间间隔 。
 *
 *
 * HeartBeatServerHandler 收到客户端心跳数据包之后，可以直接回复到客户端，让客户端也能进行类似的空闲检测，由于 IdleStateHandler
 * 本身也是一个入站处理器， 只需要重写这个类的HeartBeatServerHandler 的channelRead方法，然后将心跳数据包直接回复客户端即可。
 *
 *
 */
@Slf4j
public class HeartBeatServerHandler extends IdleStateHandler {

    private static final int READ_IDLE_GAP = 150;

    public HeartBeatServerHandler() {
        // 1. 其中第一个参数表示入站空闲检测时长，指定的是一段时间内如果没有数据入站，就判定连接假死 。 第二个参数是出站空闲检测时长
        // 2. 指的是一段时间内，如果没有数据出站，则判断连接假死  。
        // 3. 第3个参数是出/入站检测时长， 表示在一段时间内如果没有出站或者入站，就判定连接假死。
        // 4. 最后一个参数表示时间单位 ，TimeUnit.SECONDS表示秒
        super(READ_IDLE_GAP, 0, 0, TimeUnit.SECONDS);

    }

    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        //判断消息实例
        if (null == msg || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
        //判断消息类型
        ProtoMsg.HeadType headType = pkg.getType();
        if (headType.equals(ProtoMsg.HeadType.HEART_BEAT)) {
            //异步处理,将心跳包，直接回复给客户端
            FutureTaskScheduler.add(() -> {
                if (ctx.channel().isActive()) {
                    ctx.writeAndFlush(msg);
                }
            });
        }

        // 如果HeartBeatServerHandler 要重写channelRead方法，一定要记得调用基类的super.channelRead(ctx,msg) 方法，不然IdleStateHandler
        // 的入站空闲检测会无效
        super.channelRead(ctx, msg);

    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        System.out.println(READ_IDLE_GAP + "秒内未读到数据，关闭连接");
        ServerSession.closeSession(ctx);
    }


    public static void main(String[] args) {
    }
}