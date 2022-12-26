package com.tuling.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class FileInputStreamTest {

    public static void main(String[] args)  throws Exception{
        // 创建一条文件输入流
        FileInputStream fis = new FileInputStream("srcFile");
        // 获取文件流的通道
        FileChannel fileChannel = fis.getChannel();

        // 创建一条文件输出流
        FileOutputStream fos = new FileOutputStream("destFile");
        // 获取文件流的通道
        FileChannel outChannel = fos.getChannel();

        // 也可以通过RandomAccessFile 文件随机访问类， 获取FileChannel 文件通道
        // 创建RandomAccessFile 随机访问对象
        RandomAccessFile file = new RandomAccessFile("filename.txt","rx");
        // 获取文件流的通道
        FileChannel lineChannel = file.getChannel();
    }
}
