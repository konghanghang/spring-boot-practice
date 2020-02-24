package com.test.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * kafka生产者
 */
@Component
@EnableScheduling
public class KafkaMessageProducer {

    private static Logger logger = LoggerFactory.getLogger(KafkaMessageProducer.class);

    @Resource
    private KafkaSender kafkaSender;

    @Scheduled(cron = "0/10 * * * * ?")
    public void send() {
        String topic = "test";
        String message = "Hello World---" + System.currentTimeMillis();
        logger.info("topic:{}, message:{}", topic, message);
        kafkaSender.send(topic, message);
    }

}
