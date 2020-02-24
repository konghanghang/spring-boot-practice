package com.test.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * kafka消费者
 */
@Component
public class KafkaMessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageConsumer.class);

    /*@KafkaListener(topics = {"test"})
    public void receive(@Payload String message, @Headers MessageHeaders headers){
        logger.info("KafkaMessageConsumer 接收到消息：{}", message);
        headers.keySet().forEach(key->logger.info("{}: {}",key,headers.get(key)));
    }*/

    @KafkaListener(topics = "test")
    public void listen2(ConsumerRecord<?, ?> record){
        logger.info("topic：{}", record.topic());
        logger.info("key: {}", record.key());
        logger.info("value: {}", record.value());
        record.headers().forEach(e -> logger.info("key:{}, value:{}", e.key(), e.value()));
    }

}
