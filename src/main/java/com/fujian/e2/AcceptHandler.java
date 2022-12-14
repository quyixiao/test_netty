package com.fujian.e2;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AioServer> {



    // 连接成功后，等待客户端的消息，当消息来到时， 触发ReadCompletionHandler回调
    @Override
    public void completed(AsynchronousSocketChannel socketChannel, AioServer aioServer) {
        System.out.println("create connection :" + socketChannel);
        aioServer.serverSocketChannel.accept(aioServer, this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        socketChannel.read(buffer, buffer, new ReadCompletionHandler());
    }

    @Override
    public void failed(Throwable exc, AioServer attachment) {

    }
}
