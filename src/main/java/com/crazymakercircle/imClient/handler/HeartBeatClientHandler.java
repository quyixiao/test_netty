package com.crazymakercircle.imClient.handler;


import com.crazymakercircle.im.common.bean.User;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imClient.client.ClientSession;
import com.crazymakercircle.imClient.protoBuilder.HeartBeatMsgBuilder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@ChannelHandler.Sharable
@Service("HeartBeatClientHandler")
// 与服务器商的空间检测相配合，客户端要定期发送数据包到服务器端， 通常这个数据包称为心跳数据包，接下来，定义一个Handler业务处理器
// 定期发送心跳数据包给服务器端
// 在HeartBeatClientHandler 实例被加入到流水线时， 它重写了handlerAdded方法被回调，在headlerAdded(...) 方法中，开始调用heartBeat()方法
// 发送
public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {
    //心跳的时间间隔，单位为s
    //  客户端的心跳间隔，要比服务器端的空闲检测时间要短， 一般来说，要比它的一半还要短一些，可以直接定义为空闲检测时间间隔 1/3 ，这样做的目的就是
    // 为了防止公网偶发的秒级的抖动
    private static final int HEARTBEAT_INTERVAL = 100;

    //在Handler被加入到Pipeline时，开始发送心跳
    @Override
    public void handlerAdded(ChannelHandlerContext ctx)
            throws Exception {
        ClientSession session = ClientSession.getSession(ctx);
        User user = session.getUser();
        HeartBeatMsgBuilder builder =
                new HeartBeatMsgBuilder(user, session);

        ProtoMsg.Message message = builder.buildMsg();
        //发送心跳
        heartBeat(ctx, message);
    }

    //使用定时器，发送心跳报文
    public void heartBeat(ChannelHandlerContext ctx,
                          ProtoMsg.Message heartbeatMsg) {
        ctx.executor().schedule(() -> {

            if (ctx.channel().isActive()) {
                log.info(" 发送 HEART_BEAT  消息 to server");
                ctx.writeAndFlush(heartbeatMsg);

                //递归调用，发送下一次的心跳
                heartBeat(ctx, heartbeatMsg);
            }

        }, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * 接受到服务器的心跳回写
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //判断消息实例
        if (null == msg || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        //判断类型
        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType headType = pkg.getType();
        if (headType.equals(ProtoMsg.HeadType.HEART_BEAT)) {

            log.info(" 收到回写的 HEART_BEAT  消息 from server");

            return;
        } else {
            super.channelRead(ctx, msg);

        }

    }

    // 在登录成功之后，在ChannelPipeline 通道流水线上， HeartBeatClientHandler 心跳客户端处理器实例被动态的插入到解码器之后。
    // 服务器端的空闲检测处理器在收到客户端的心跳数据包后， 会进行回写， 在HeartBeatClientHandler 的channelRead方法中， 对加写的数据包
    //

}
