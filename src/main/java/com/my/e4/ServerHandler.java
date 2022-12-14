package com.my.e4;

import com.alibaba.fastjson.JSONObject;
import com.my.e2.RequestFuture;
import com.my.e2.Response;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //if (msg instanceof ByteBuf) {
            // ByteBuf 的toString() 方法把二进制数据转换成字符串，默认编码为UTF-8
         //   System.out.println(((ByteBuf) msg).toString(Charset.defaultCharset()));
        //}
        //ctx.channel().writeAndFlush("msg has recived !");
        // 获取客户端发送的请求 ， 并将其转换成RequestFuture对象
        // 由于经过StringDecoder解码器，所以msg为String类型
        RequestFuture request = JSONObject.parseObject(msg.toString(),RequestFuture.class);
        // 获取请求id
        Long id = request.getId();
        System.out.println("请求的信息为====" + msg.toString());
        // 构建响应结果
        Response response = new Response();
        response.setId(id);
        response.setResult("服务器响应ok");
        // 把响应结果返回给客户端
        ctx.channel().writeAndFlush(JSONObject.toJSONString(response));
        
    }


    // Netty 客户端线程模型比服务端线程模型简单一些， 它只需要一个线程组，底层采用了Java 的 NIO , 通过IP 和商品连接目标服务器， 请求发送和
    // 如果没有参数据流进行一些额外的加工处理， 那么将无法区分每次请求的数据包， 编码是指在传输数据前， 对数据包进行加工处理，解码发生在读取数据包时
    //


    // 2.5零拷贝
    // 序列化的主要传输数据格式有关，不管是Kryo还是Protobuf ，它们都能对数据内容进行压缩， 并能完整的恢复  ， 零拷贝是Netty的一个
    // ，主要发生在操作数据上， 无须将数据从Buffer从一个内存区域拷贝到另外一个内存区域，少了一次拷贝，CPU效率会提升，Netty的零拷贝主要有以下3个场景
    // 1. Netty接收和发送ByteBuffer采用的都是堆外直接内存，使用堆外内存进行Socket的读写，无须进行字节缓冲区的二次拷贝，如果使用传统的堆内存
    // 进行Socket的读/写， 则JVM会将堆内存Buffer数据拷贝到堆外直接内存中，然后才写入Socket中， 然后才写入到Socket中，与堆外直接内存相比
    // 使用传统的堆内存， 在消息发送过程中多了一次缓冲区的内存拷贝。
    // 2. 在网络传输中，一条消息很可能会被分割成多个数据包进行发送， 只有当收到一个完整的数据包后，才完成解码工作，Netty 通过组合内存
    // 的方式把这些内存数据包逻辑组合到一块， 而不是对每个数据进行一次拷贝， 这类似于数据库中的视图，  CompositeByteBuffer是Netty
    // 在此零拷贝方案中的组合Buffer,在第4章节中会对它进行详细的剖析 。
    // 3. 传统拷贝文件的方法需要先把文件采用FileInputStream文件输入流读取到一个临时的byte[]数组中， 然后通过FileOutputStream 文件输出流
    // 把临时的byte[]数据内容写入到目标文件中，当拷贝大文件时，频繁的内存拷贝操作会消耗大量的系统资源，Netty底层运用Java NIO 的FileChannel.transfer()方法
    // 该方法依赖操作系统实现零拷贝， 可以直接将文件缓冲区的数据发送到目标的Channel中，避免传统通过循环写入的方式导致内存数据拷贝问题。
    //
}
