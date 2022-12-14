package com.fujian.e2;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {


    private ByteBuffer by;

    @Override
    public void completed(Integer result, ByteBuffer byteBuffer) {
        byteBuffer.flip();
        byte[] body = new byte[result];
        byteBuffer.get(body);
        String requestMsg = new String(body, StandardCharsets.UTF_8);
        System.out.println("read Msg :" + requestMsg);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
