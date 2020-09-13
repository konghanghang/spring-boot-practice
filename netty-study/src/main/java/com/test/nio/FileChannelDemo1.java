package com.test.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo1 {

    /**
     * 把str写进文件
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("./hello.txt");

        FileChannel channel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        String str = "nio test";

        byteBuffer.put(str.getBytes());

        byteBuffer.flip();

        channel.write(byteBuffer);

        fileOutputStream.close();
    }

}
