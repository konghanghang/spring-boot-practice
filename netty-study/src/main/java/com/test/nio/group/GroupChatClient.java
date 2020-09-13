package com.test.nio.group;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {

    private final String HOST = "127.0.0.1";

    private final int PORT = 6667;

    private Selector selector;

    private SocketChannel socketChannel;

    private String username;

    public GroupChatClient() {
        try {
            selector = Selector.open();

            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));

            socketChannel.configureBlocking(false);

            socketChannel.register(selector, SelectionKey.OP_READ);

            username = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(username + " is ok ...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInfo(String info) {

        info = username + " 说：" + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readInfo() {
        try {
            int readChannels = selector.select();
            if (readChannels > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        sc.read(byteBuffer);
                        String msg = new String(byteBuffer.array());
                        System.out.println(msg.trim());
                    }
                    iterator.remove();
                }
            } else {
                System.out.println("没有可用的通道。。。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupChatClient client = new GroupChatClient();
        new Thread(() -> {
            while (true) {
                client.readInfo();
                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            client.sendInfo(s);
        }
    }

}
