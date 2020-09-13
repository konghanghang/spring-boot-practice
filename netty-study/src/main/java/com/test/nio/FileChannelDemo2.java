package com.test.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo2 {

    /**
     * 把文件中的内容打印出来
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        File file = new File("./hello.txt");

        FileInputStream fileInputStream = new FileInputStream(file);

        FileChannel channel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        channel.read(byteBuffer);

        System.out.println(new String(byteBuffer.array()));

        fileInputStream.close();
    }

}
