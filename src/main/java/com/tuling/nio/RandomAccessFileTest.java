package com.tuling.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class RandomAccessFileTest {


    public static void main(String[] args)  throws Exception{
        RandomAccessFile aFile = new RandomAccessFile("fileName","rw");
        // 获取通道
        FileChannel inChannel = aFile.getChannel();
        // 获取一个字节缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        int length = 1 ;
        // 调用通道的read方法，读取数据并买入字节类型的缓冲区
        while((length = inChannel.read(buf)) != -1 ){
            // 省略，处理读取buf中的数据
        }
        // 【注意 】： 虽然对于通道来说是读取数据，但对于ByteBuffer来说，缓冲区来说是写入数据 ， 这里，ByteBuffer 缓冲区处于写入模式 。
    }
}
