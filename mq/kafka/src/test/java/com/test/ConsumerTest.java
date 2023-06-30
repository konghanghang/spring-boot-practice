package com.test;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerTest {

    private KafkaConsumer<String, String> consumer;

    private Properties properties;

    @DisplayName("初始化properties数据")
    @BeforeEach
    void before(){
        properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "first-group");
        // 重置消费者offset
        // 换组
        // 数据被删除触发（记录的消费到10，但最新的是1000，重置1000开始消费）
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    @DisplayName("关闭连接,好像也没有什么用")
    @AfterEach
    void after() {
        consumer.close();
    }

    @DisplayName("自动提交消费者")
    @Test
    void autoCommitConsumer(){
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("first"));
        while (true) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                System.out.println(consumerRecord.key() + "----" + consumerRecord.value());
            }
        }
    }

    @DisplayName("非自动提交消费者")
    @Test
    void closeAutoCommitConsumer(){
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("first"));
        while (true) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                System.out.println(consumerRecord.key() + "----" + consumerRecord.value());
            }
            consumer.commitSync();
        }
    }

}
