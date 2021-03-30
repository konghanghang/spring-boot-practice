package com.test.tree.huffman.code;

import com.test.tree.huffman.Node;

import java.io.*;
import java.util.*;

/**
 * 生成的哈夫曼树不唯一，所以哈夫曼编码也不唯一，
 * 只要最终数据长度对的上就可以了
 * @author yslao@outlook.com
 * @since 2021/3/29
 */
public class HuffmanCode {

    // 哈夫曼编码
    private Map<Byte, String> huffmanCode = new HashMap<>();

    /**
     * 根据byte数组生成node
     * @param bytes
     * @return
     */
    public List<Node> getNodes(byte[] bytes) {
        List<Node> nodes = new ArrayList<>();
        Map<Byte, Integer> map = new HashMap<>();
        for (Byte byt :bytes){
            Integer count = map.get(byt);
            if (count == null) {
                map.put(byt, 1);
            } else {
                map.put(byt, count + 1);
            }
        }
        // 生成node
        for (Map.Entry<Byte, Integer> entry : map.entrySet()) {
            nodes.add(new Node(entry.getKey(), entry.getValue()));
        }
        return nodes;
    }

    /**
     * 创建哈夫曼树
     * @param nodes
     * @return
     */
    public Node createHuffmanTree(List<Node> nodes) {
        while (nodes.size() > 1) {
            Collections.sort(nodes);
            // 如果是倒序排,则要从后边取
            Node leftNode = nodes.get(0);
            Node rightNode = nodes.get(1);
            // 生成的节点只有权值, 没有data值
            Node parent = new Node(null, leftNode.getWeight() + rightNode.getWeight());
            parent.setLeft(leftNode);
            parent.setRight(rightNode);
            nodes.add(parent);

            // 删除节点
            nodes.remove(leftNode);
            nodes.remove(rightNode);
        }
        return nodes.get(0);
    }

    /**
     * 获取huffman编码
     * @param node
     * @return
     */
    public Map<Byte, String> getCodes(Node node) {
        if (Objects.nonNull(node)) {
            getCodes(node, "", new StringBuffer());
        }
        return huffmanCode;
    }

    /**
     * 构建huffman编码
     * @param node
     * @param code  左子节点是0，右子节点是1
     * @param stringBuffer
     */
    private void getCodes(Node node, String code, StringBuffer stringBuffer) {
        StringBuffer sb = new StringBuffer(stringBuffer);
        sb.append(code);
        if (node != null) {
            // 如果数据域为null，则不是叶子节点，需要继续往下遍历
            if (node.getData() == null) {
                // 向左遍历
                getCodes(node.getLeft(), "0", sb);
                // 向右遍历
                getCodes(node.getRight(), "1", sb);
            } else {
                // 如果数据域为null，则是叶子节点,保存编码
                huffmanCode.put(node.getData(), sb.toString());
            }
        }
    }

    /**
     * 将需要压缩的字符串对应的byte数组进行压缩
     * @param content
     */
    public byte[] zip(byte[] content) {
        // 获取原始内容对应的哈夫曼编码
        StringBuilder sb = new StringBuilder();
        for (byte b : content) {
            sb.append(huffmanCode.get(b));
        }
        // System.out.println("byte[] to huffmanCode str:" + sb.toString());
        // 由于是要将哈夫曼编码转成byte数组，所以先计算转后的byte数组长度，每8位一个字节
        int len = (sb.length() + 7) / 8;
        byte[] huffmanCodeBytes = new byte[len];
        int index = 0;
        for (int i = 0; i < sb.length(); i+=8) {
            if (i + 8 > sb.length()) {
                huffmanCodeBytes[index] = (byte) Integer.parseInt(sb.substring(i, sb.length()), 2);
            } else {
                huffmanCodeBytes[index] = (byte) Integer.parseInt(sb.substring(i, i + 8), 2);
            }
            index++;
        }
        return huffmanCodeBytes;
    }

    /**
     * 解压
     * @param huffmanCodeBytes
     * @return
     */
    public byte[] unzip(byte[] huffmanCodeBytes) {
        return unzip(huffmanCodeBytes, huffmanCode);
    }

    /**
     * 解压
     * @param huffmanCodeBytes
     * @return
     */
    public byte[] unzip(byte[] huffmanCodeBytes, Map<Byte, String> customCode) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < huffmanCodeBytes.length; i++) {
            boolean flag = i == huffmanCodeBytes.length - 1;
            sb.append(byte2BitString(huffmanCodeBytes[i], !flag));
        }
        System.out.println("huffmanCodeBytes to binary str:" + sb.toString());
        // 因为要反向查找，所以需要把哈夫曼编码map的key和value对调
        Map<String, Byte> reverseCode = new HashMap<>();
        for (Map.Entry<Byte, String> entry : customCode.entrySet()) {
            reverseCode.put(entry.getValue(), entry.getKey());
        }
        System.out.println("反转后的map:" + reverseCode);
        List<Byte> list = new ArrayList<>();
        for (int i = 0; i < sb.length();) {
            int count = 1;
            boolean flag = true;
            while (flag) {
                String key = sb.substring(i, i + count);
                Byte b = reverseCode.get(key);
                if (b != null) {
                    list.add(b);
                    flag = false;
                } else {
                    count++;
                }
            }
            i += count;
        }
        byte[] result = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    /**
     * 压缩文件
     * @param srcFile 源文件
     * @param distFile 压缩后保存文件
     */
    public void zipFile(String srcFile, String distFile) {
        try(InputStream inputStream = new FileInputStream(srcFile);
            OutputStream outputStream = new FileOutputStream(distFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);

            List<Node> nodes = getNodes(b);
            // 生成哈夫曼树
            Node root = createHuffmanTree(nodes);
            // 获取哈夫曼编码
            getCodes(root);
            // 进行压缩
            byte[] zip = zip(b);
            // 把哈夫曼编码后的字节数组写入压缩文件
            objectOutputStream.writeObject(zip);
            // 把哈夫曼编码写入压缩文件
            objectOutputStream.writeObject(huffmanCode);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解压文件
     * @param zipFile
     * @param distFile
     */
    public void unzipFile(String zipFile, String distFile) {
        try(InputStream inputStream = new FileInputStream(zipFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            OutputStream outputStream = new FileOutputStream(distFile)) {
            // 哈夫曼压缩后的字节数组
            byte[] huffmanCodeBytes = (byte[]) objectInputStream.readObject();
            // 哈夫曼编码
            Map<Byte, String> huffmanCode = (Map<Byte, String>) objectInputStream.readObject();
            byte[] unzip = unzip(huffmanCodeBytes, huffmanCode);

            // 写入文件
            outputStream.write(unzip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * byte转成一个二进制的字符串
     * @param b
     * @param flag 是否需要补高位
     * @return 对应的二进制字符串，（注意是按反码返回）
     */
    private String byte2BitString(byte b, boolean flag) {
        int temp = b;
        if (flag) {
            temp |= 256; //按位或256， 1 0000 0000 | 0000 0001 -> 1 0000 0001
        }
        String s = Integer.toBinaryString(temp);
        // 如果压缩生成的最后一个byte数为负值，会多出24位1,解决方法：这一段判断条件加一个temp<0
        if (flag || temp<0) {
            return s.substring(s.length() - 8);
        } else {
            return s;
        }
    }

}
