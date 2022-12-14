package com.crazymakercircle.imClient.handler;


import com.crazymakercircle.im.common.ProtoInstant;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imClient.client.ClientSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@ChannelHandler.Sharable
@Service("LoginResponceHandler")
// LoginResponceHandler 登录响应处理器对消息类型进行判断
 // 如果消息类型是请求响应的消息并且登录成功，则取出绑定的会话Session, 再设置登录成功之后，进行其他的客户端业务处理
// 2. 如果消息类型不是请求响应消息，则调用父类默认的super.channelRead()入站处理方法，将数据包交给流水线的下一站Handler 业务处理器去处理

public class LoginResponceHandler extends ChannelInboundHandlerAdapter {
    /**
     * 业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        //判断消息实例
        if (null == msg || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        //判断类型
        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType headType = ((ProtoMsg.Message) msg).getType();
        if (!headType.equals(ProtoMsg.HeadType.LOGIN_RESPONSE)) {
            super.channelRead(ctx, msg);
            return;
        }


        //判断返回是否成功
        ProtoMsg.LoginResponse info = pkg.getLoginResponse();

        ProtoInstant.ResultCodeEnum result =
                ProtoInstant.ResultCodeEnum.values()[info.getCode()];

        if (!result.equals(ProtoInstant.ResultCodeEnum.SUCCESS)) {
            //登录失败
            log.info(result.getDesc());
        } else {
            //登录成功
            ClientSession.loginSuccess(ctx, pkg);
            ChannelPipeline p = ctx.pipeline();
            // 在登录成功后，需要将LoginResponseHandler登录响应处理实例从流水线上移除，因为不需要再处理响应了，同时，需要在客户端和服务器
            // 之间开启心跳处理，心跳处理是一个比较复杂的议题，后面会有个单独的小节，专门来介绍客户端和服务器之间的心跳 ，
            //移除登录响应处理器 ,
            p.remove(this);

            //在编码器后面，动态插入心跳处理器
            p.addAfter("encoder", "heartbeat", new HeartBeatClientHandler());
        }

    }

}
