/**
 * Created by 尼恩 at 疯狂创客圈
 */

package com.crazymakercircle.imClient.protoBuilder;

import com.crazymakercircle.im.common.bean.User;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imClient.client.ClientSession;


/**
 * 心跳消息Builder
 *
 * 客户端心跳检测对于任何长连接的应用来说，都是非常基础的功能，要理解心跳的重要性， 首先需要从网连接假死的现象说起。
 *
 * 网络连接的假死现象
 *
 * 什么时连接的假死现象呢？ 如果底层的TCP连接已经断开，但是服务器端并没有正常的关闭套接字，服务器端认为这条TCP连接仍然是存在的。
 *
 * 1. 服务器端，会有一些处于TCP_ESTABLISHED状态的正常连接
 * 2. 但是客户端，TCP客户端已经显示连接已经断开
 * 3. 客户端此时虽然已经可以进行断线重连操作，但是上一次连接状态依然被服务器端认为是有效的， 并且服务器端的资源得不到正常的释放 ，包括套接字上下文
 *  及接收 /发送缓冲区
 * 连接假死的情况虽然不常见，但是确实存在，服务器端长时间运行后，会面临大量的假死连接得不到正常的释放的情况下，由于每个连接都会耗费CPU 资源
 * 因此大量的假死的连接会逐渐耗光服务器资源，使得服务器越来越低， 最终导致服务器崩溃 。
 *
 * 连接假死通常是由以下多个原因造成的  。。。。。。。。。。
 * 1. 应用程序出现线程堵塞，无法进行数据的读写
 * 2. 网络相关的设备出现故障， 例如，网卡， 机房故障
 * 3. 网络丢包， 公网环境下非常容易出现丢包和网络抖动等现象 ,解决网络假死的有效手段是，客户端定时进行心跳检测，服务器端定时进行空闲检测 。

 */
public class HeartBeatMsgBuilder extends BaseBuilder {
    private final User user;

    public HeartBeatMsgBuilder(User user, ClientSession session) {
        super(ProtoMsg.HeadType.HEART_BEAT, session);
        this.user = user;
    }

    public ProtoMsg.Message buildMsg() {
        ProtoMsg.Message message = buildCommon(-1);
        ProtoMsg.MessageHeartBeat.Builder lb =
                ProtoMsg.MessageHeartBeat.newBuilder()
                        .setSeq(0)
                        .setJson("{\"from\":\"client\"}")
                        .setUid(user.getUid());
        return message.toBuilder().setHeartBeat(lb).build();
    }


}


