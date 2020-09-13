package com.test.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo3 {

    /**
     * 把hello.txt复制成hello2.txt
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(new File("./hello.txt"));
        FileChannel channel01 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream(new File("./hello2.txt"));
        FileChannel channel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while (true) {
            byteBuffer.clear();
            int read = channel01.read(byteBuffer);
            if (read == -1) break;
            byteBuffer.flip();

            channel02.write(byteBuffer);
        }

        fileInputStream.close();
        fileOutputStream.close();
    }

}
