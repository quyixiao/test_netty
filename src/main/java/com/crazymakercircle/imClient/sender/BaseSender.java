package com.crazymakercircle.imClient.sender;

import com.crazymakercircle.im.common.bean.User;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imClient.client.ClientSession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
// 一般来说，在Netty中会调用write(pkg)/writeAndFlush(pkg)这一组方法来发送数据包，前面多次反复讲到， 发送方法channel.writeAndflush(pkg)
// 立即返回的，返回的类型是一个ChannelFuture异步任务实例， 问题是，当channel.writeAndFlush(pkg)函数返回时， 如何判断是否已经成功将数据包写入到
// 底层的TCP呢？
// 答案是没有，在writeAndFlush(pkg)方法返回时，真正的TPC 写入的操作其实还没有执行，为什么呢？
// 由于这个知识确实比较重要，因此在这里再一次强调一遍吧，在Netty 中，无论是出站操作，还是出站操作都有两大特点 。
// 1. 同一条通道的所有出/入站处理都是串行的， 而不是并行的，换句话，同一条通道上的所有出/入站处理都会在它所绑定的EventLoop 线程上执行，既然
// 只有一个线程负责，那就中只有串行的问题.
// Netty 是如何保障这一点的呢？
// 假如某个出入/站在最开始执行的时候，会对当前的执行线程进行判断，如果当前线程不是通道的EventLoop线程，则当前出/入站的处理暂时不执行， Netty
// 会将当前出/入站的处理，通过建立一个新的异步可执行任务，加入到通道的EventLoop线程的任务队列中。
// EventLoop线程的任务队列是一个MPSC 队列（即多生产者间消息者队列），什么是MPSC 队列呢？ 只有EventLoop 线程自己是唯一的消费者，它将遍历任务队列，逐个
// 执行任务，其他线程只能作为ktgutftj，它们的出入站操作都会作为异步任务加入队列任务队列，通过MPSC 队列，确保了EventLoop 线程能做到，同一个通道
// 所有的IO 操作是，不是并行的，这样，不同的Handler 业务处理器之间不需要进行线程同步，这点也能大大的提升的IO 的性能 。
// Netty的一个出入站操作不是一次间五的Handler 业务处理器操作，而是流水线中的一系列的出/入站处理流程，只有整个流程都处理完，出/入站才真正
// 的处理完成 。
// 基于以上两点，大家可以简单的推断，在调用完channel.writeAndFlush(pkg) 后，真正的出站操作肯定是没有执行完成的， 可能需要在EventLoop
// 的任务队列 中排队等待.
// 如何才能判断writeAndFlush()执行完毕了呢？ writeAndFlush()方法会返回一个ChannelFuture异步任务实例，通过ChannelFuture异步任务增加了GenericFutureListener
// 监听器的方式来判断writeAndFlush()是否已经执行完毕，当GenericFutureListener监听器的operationComplete方法被回调时，表示方法已经执行完毕，
// 而且具体的回调业务逻辑，可以放在operationComplete()监听器方法中.
// 在上面的代码中，设计了两个sendSucced/sendfailed 业务回调方法，旋转在operationComplete监听器方法中， 在发送完成后进行回调。 并且 将
// sendSucesstd和sendfaild方法封装在发送器baseSender 基类中， 如果需要改变默认的回调处理逻辑，可以在发送器子类重写基类的sendSuceed和sendFailded
// 方法即可。
// 再看另外一个话题，在上面的代码中，为了获取客户端的通道，使用了ClientSession 客户端会话，什么是会话呢？会话的作用是什么呢？ 什么时候创建会话呢？
//
public abstract class BaseSender {


    private User user;
    private ClientSession session;


    public boolean isConnected() {
        if (null == session) {
            log.info("session is null");
            return false;
        }

        return session.isConnected();
    }

    public boolean isLogin() {
        if (null == session) {
            log.info("session is null");
            return false;
        }

        return session.isLogin();
    }

    public void sendMsg(ProtoMsg.Message message) {


        if (null == getSession() || !isConnected()) {
            log.info("连接还没成功");
            return;
        }

        Channel channel = getSession().getChannel();
        ChannelFuture f = channel.writeAndFlush(message);
        f.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future)
                    throws Exception {
                // 回调
                if (future.isSuccess()) {
                    sendSucced(message);
                } else {
                    sendfailed(message);

                }
            }

        });



/*

        try {
            f.sync();
        } catch (InterruptedException e) {

            e.printStackTrace();
            sendException(message);
        }

*/
    }

    protected void sendSucced(ProtoMsg.Message message) {
        log.info("发送成功");

    }

    protected void sendfailed(ProtoMsg.Message message) {
        log.info("发送失败");
    }



}
