package com.crazymakercircle.imServer.handler;

import com.crazymakercircle.cocurrent.CallbackTask;
import com.crazymakercircle.cocurrent.CallbackTaskScheduler;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imServer.processer.LoginProcesser;
import com.crazymakercircle.imServer.server.ServerSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("LoginRequestHandler")
@ChannelHandler.Sharable
// 属于Handler
// 2. UserLoginRequestHandler 登录处理器用于处理Protobuf 数据包 ， 进行一些必要的判断和预处理，启动LoginProcesser 登录业务处理器
// 开始以异步方式进行登录验证处理
//
public class LoginRequestHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    LoginProcesser loginProcesser;

    /**
     * 收到消息
     * LoginRequestHandler 登录请求处理器
     * 这是个入站处理器，它继承自ChannelInboundHandlerAdapter入站适配器，重写了channelRead方法，主要的工作如下 ：
     * 对消息进行必要的判断，判断是否为登录请求，Protobuf数据包，如果不是，通过super.channelRead(ctx.msg) 将消息交给流水线下一站
     * 创建一个ServerSession ，即为客户端创建一个服务器端会话。
     * 使用自定义的CallbackTaskScheduler异步任务调度器， 提交一个异步任务，启动LoginProcessor执行合建用户验证的逻辑 。
     *
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        if (null == msg
                || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;

        //取得请求类型
        ProtoMsg.HeadType headType = pkg.getType();

        if (!headType.equals(loginProcesser.type())) {
            super.channelRead(ctx, msg);
            return;
        }


        ServerSession session = new ServerSession(ctx.channel());

        //异步任务，处理登录的逻辑
        CallbackTaskScheduler.add(new CallbackTask<Boolean>() {
            @Override
            public Boolean execute() throws Exception {
                boolean r = loginProcesser.action(session, pkg);
                return r;
            }

            //异步任务返回
            @Override
            public void onBack(Boolean r) {
                if (r) {
                    ctx.pipeline().remove(LoginRequestHandler.this);
                    log.info("登录成功:" + session.getUser());

                } else {
                    ServerSession.closeSession(ctx);
                    log.info("登录失败:" + session.getUser());

                }

            }
            //异步任务异常

            @Override
            public void onException(Throwable t) {
                ServerSession.closeSession(ctx);
                log.info("登录失败:" + session.getUser());

            }
        });

    }


}
