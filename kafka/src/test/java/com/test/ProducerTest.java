package com.test;

import org.apache.kafka.clients.producer.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Properties;

/**
 * @author yslao@outlook.com
 * @since 2021/3/25
 */
public class ProducerTest {

    private Properties properties = null;
    private KafkaProducer<String, String> kafkaProducer = null;

    @DisplayName("初始化properties数据")
    @BeforeEach
    void initProperties() {
        properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
    }

    @DisplayName("关闭kafka连接")
    @AfterEach
    void closeKafka() {
        kafkaProducer.close();
    }


    @DisplayName("普通消费者")
    @Test
    void commonProducer() {
        kafkaProducer = new KafkaProducer<>(properties);
        for (int i = 0; i < 10; i++) {
            kafkaProducer.send(new ProducerRecord<String, String>("first", "test", "msg -" + i));
        }
    }

    @DisplayName("带回调函数消费者")
    @Test
    void callbackProducer() {
        kafkaProducer = new KafkaProducer<>(properties);
        for (int i = 0; i < 5; i++) {
            kafkaProducer.send(new ProducerRecord<String, String>("first", "test - " + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (Objects.isNull(e)) {
                        System.out.println(recordMetadata.partition() + "- " + recordMetadata.offset());
                    }
                }
            });
        }
    }

    @DisplayName("自定义分区器")
    @Test
    void customPartition() {
        properties.setProperty(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.test.kafka.partition.MyPartition");
        kafkaProducer = new KafkaProducer<>(properties);
        for (int i = 0; i < 10; i++) {
            kafkaProducer.send(new ProducerRecord<String, String>("first", "msg -" + i));
        }
    }

}
