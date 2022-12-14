package com.crazymakercircle.im.common.codec;


import com.crazymakercircle.im.common.ProtoInstant;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * create by 尼恩 @ 疯狂创客圈
 *
 * 编码器
 */
// 基于Netty 的单体的IM系统的开发实践
@Slf4j
// 本章是Netty 的应用综合实践篇，将综合使用前面学习编码器，解码器业务处理等知识，完成一个聊天系统的设计与实现。
// 9.1 自定义ProtoBuf 编解码器
// 前面已经详细介绍过， 在Netty 中，内置了一组ProtoBuf 编解码器，ProtoBuf Decoder 解码器和ProtobufEncoder 编码器，它们负责
// Protobuf 半包处理器，可变长度ProtobufVarint32FrameDecoder解码器 ， ProtobufVarint32LengthFieldPrepender编码器，为二进制 ByteBuf
// 加上varint32格式的可变长度，解决了Protobuf 传输过程中的粘包和半包问题。
// 使用Netty内置的Protobuf 系统编解码器， 可以解决简单的Protobuf协议的传输问题， 不过 ， 面对复杂的Head-Content协议的解析，例如，在数据包
// Head部分，加上魔数，版本号字段，具体如图9-1所示，内置了Protobuf 编解码器，就显得无能为力了。
//  魔数  版本  长度   content (9-1图)
// 在通信数据包中， 魔数是什么呢？ 魔数可以理解为通信的口令，例如，在电影《智取威虎山》中，土匪的接头暗号在原理上是一样的，无论是服务器还是
// 客户端，通信之前首先要对口令，如果口令不对，就不是符合自定义协议规范的数据包， 也不是安全的数据包， 通过魔数对比， 服务器端能够在第一时间识别
//  出不符合规范的数据包， 为了安全考虑，可以直接关闭连接 。
// 在通信数据包中， 版本号是什么呢？ 如果在程序中有协议升级的需求，又需要同时 兼顾新旧的版本协议，就会用这个版本号， 例如，APP 协议升级后
// 旧版本还是需要使用
// 解析复杂的Head-Content 协议就需要自定义的Protobuf编解码器，需要开发自己的解决半包问题， 包括以下的两个方面。
// 1. 继承netty提供的MessageToByteEncoder编码器，完成Head-Content 协议的复杂数据包的编码，将Protobuf POJO 编码成Head-Contet
// 协议的二进制ByteBuf数据包
// 2. 继承Netty 提供了ByteToMessageDecoder 解码器， 完成Head-Content 协议的复杂数据包解码，将二进制ByteBuf 数据包最终解码成Protobuf POJO实例。
// 9.1.1 自定义Protobuf 编码器
// 自定义Protobuf 编码器，通过继承Netty中基础的MessageToByteEncoder 编码器类，实现其抽象的编码方法encode, 在该方法中把以下的内容写到目标
// ByteBuf :
// 1. 写入到Protobuf POJO 的字节码长
// 2. 写入其他的字段，如魔数，版本号
// 3. 写入Protobuf 的POJO 的字节码内容

public class ProtobufEncoder extends MessageToByteEncoder<ProtoMsg.Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          ProtoMsg.Message msg, ByteBuf out)
            throws Exception {
        out.writeShort(ProtoInstant.MAGIC_CODE);
        out.writeShort(ProtoInstant.VERSION_CODE);

        byte[] bytes = msg.toByteArray();// 将对象转换为byte

        // 加密消息体
        /*ThreeDES des = channel.channel().attr(Constants.ENCRYPT).get();
        byte[] encryptByte = des.encrypt(bytes);*/
        int length = bytes.length;// 读取消息的长度


        // 先将消息长度写入，也就是消息头
        // 这里可以用writeShort(length)方法，仅仅两个字节，这就存在一个问题，数据包最大的净内容的长度为32767 个字节，有符号的短整形
        // 如果一个数据包需要传输更多的内容，  可以调用writeInt(length)方法 。
        out.writeInt(length);
        // 消息体中包含我们要发送的数据
        out.writeBytes(msg.toByteArray());
/*        log.debug("send "
                + "[remote ip:" + ctx.channel().remoteAddress()
                + "][total length:" + length
                + "][bare length:" + msg.getSerializedSize() + "]");*/


    }

}
