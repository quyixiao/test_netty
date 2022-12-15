package com.crazymakercircle.imServer.processer;

import com.crazymakercircle.im.common.ProtoInstant;
import com.crazymakercircle.im.common.bean.User;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imServer.protoBuilder.LoginResponceBuilder;
import com.crazymakercircle.imServer.server.ServerSession;
import com.crazymakercircle.imServer.server.SessionMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("LoginProcesser")
// LoginProcesser 通过数据库或远程接口完成用户验证，根据验证处理结果，生成登录成功或者登录失败的响应报文，并发送给客户端
// LoginProcesser 用户验证逻辑主要包括，密码验证，将验证的结果写入到通道，如果登录验证成功，还需要绑定服务端的会话，并且加入到在线用户列表中。
// 在前面的章节中，已经堤了一个疑问，为什么在服务器端整个登录处理需要分成两个模块 ， 一个是NettyHandler , 实际上，Reactor反应器
// 和业务线程相互隔离 ， 在服务器端非常重要，为什么呢？
// 首先，以读通道 channelRead 为例，看一下次普通的入站处理的基本逻辑 ， 而不是在EventLoop 线程中去执行用户验证的逻辑，实际上
// Reactor 反应器线程和业务线程相互隔离，在服务器中端非常非常重要呢？
// 首先，看读通道 channelRead为例子， 看一下普通的入站处理的基本步骤 。
// 1. 判断消息是否需要处理
// 2. 取得消息，并判断类型
// 3. 耗时的业务处理操作。
// 4. 把结果写入到连接通道
// 其中第3步，通常会涉及到一些比较耗时的业务处理操作，例如 ：
// 1.数据库操作，100ms 以上
// 2. 远程接口调用，百毫秒以上， 一般在200ms 以上， 慢在1000ms 以上
// 再看Netty 内部的IO 读写操作，通常都是毫秒级也就是说,Netty 内部的IO操作和业务处理操作都在时间上不在一个数量级。
// 问题来了， 在大量的成千上万的子通道复用一个EventLoop线程的应用场景 中，一旦耗时的业务处理操作也执行在EventLoop线程上，就会导致其他
// 子通道的IO操作发生严重的性能问题， 为什么说会是严重的性能问题呢？
// 大家都知道 ，在默认的情况下， Netty 的一个EventLoop 实例会开启2倍的CPU 数的内部线程， 通常情况下 ，一个Netty 服务端会有几万或者几十万
// 在一个EventLoop 内部的线程上的任务是串行的，如果一个Handler 业务处理器的channelRead()入站处理方法执行1000ms或者几分钟才，最终
// 的结果，阻塞了EventLoop 内部线程其他几十万个通道的出站和入站处理，阻塞时长为1000ms 或者 几秒钟，而耗时的入站出站处理越多， 就越会
// 线程的其他IO 处理， 最张导致严重的性能问题
// 这样严重的性能问题就出来了， 咋办呢？ 解决办法是，业务操作和EventLoop 线程相隔离 ， 具体来说，就是专门开辟一个独立的线程池，负责一个独立的
// 异步任务处理队列，对于耗时的业务操作封装成异步任务，并放入到异步任务队列中去处理，这样的话，服务器端的性能会提升很多
// 
public class LoginProcesser extends AbstractServerProcesser {
    @Autowired
    LoginResponceBuilder loginResponceBuilder;

    @Override
    public ProtoMsg.HeadType type() {
        return ProtoMsg.HeadType.LOGIN_REQUEST;
    }

    @Override
    public boolean action(ServerSession session,
                          ProtoMsg.Message proto) {
        // 取出token验证
        ProtoMsg.LoginRequest info = proto.getLoginRequest();
        long seqNo = proto.getSequence();

        User user = User.fromMsg(info);

        //检查用户
        boolean isValidUser = checkUser(user);
        if (!isValidUser) {
            ProtoInstant.ResultCodeEnum resultcode =
                    ProtoInstant.ResultCodeEnum.NO_TOKEN;
            //构造登录失败的报文
            ProtoMsg.Message response =
                    loginResponceBuilder.loginResponce(resultcode, seqNo, "-1");
            //发送登录失败的报文
            session.writeAndFlush(response);
            return false;
        }

        session.setUser(user);
        //
        session.bind();

        //登录成功
        ProtoInstant.ResultCodeEnum resultcode =
                ProtoInstant.ResultCodeEnum.SUCCESS;
        //构造登录成功的报文
        ProtoMsg.Message response =
                loginResponceBuilder.loginResponce(
                        resultcode, seqNo, session.getSessionId());
        //发送登录成功的报文
        session.writeAndFlush(response);
        return true;
    }

    private boolean checkUser(User user) {

        if (SessionMap.inst().hasLogin(user)) {
            return false;
        }

        //校验用户,比较耗时的操作,需要100 ms以上的时间
        //方法1：调用远程用户restfull 校验服务
        //方法2：调用数据库接口校验

        return true;

    }

}
