package com.test;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class MyProducer {

    public static void main(String[] args) {
        Properties properties = new Properties();
        // 指定kafka集群信息
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.20.63.61:9092");
        // 应答级别
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        // 重试次数
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, "1");
        // 批次大小，字节 16k
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        // 等待时间 1毫秒
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1");
        // recordAccumulator缓冲区大小 32MB
        properties.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        // 序列化
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        for (int i = 0; i < 10; i++) {
            kafkaProducer.send(new ProducerRecord<>("first", "msg--" + i));
        }

        kafkaProducer.close();

    }

}
