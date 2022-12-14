package com.fujian.e2;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

public class AioServer {

    public AsynchronousServerSocketChannel serverSocketChannel;

    public AioServer(int port ){
        try {
            // 这个代码创建了一个AIO 服务器，并指定了AcceptHandler作为接受连接后的回调函数
            //
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.accept(this,new AcceptHandler());
            System.out.println("服务器启动完毕 ");
        } catch (Exception e) {

            e.printStackTrace();
        }

    }


    // 通过上述代码，我们可以了解到，AIO编程的最大特点就是异步回调，例如，当消息到来时，消息实际上已经读取好了。 但如果使用NIO
    // 开发者需要等待数据就绪方面的通知，然后才同步读取数据
    
}
