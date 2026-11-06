package com.test.kafka;

import com.iminling.common.json.JsonUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Component
public class KafkaSender<T> {

    private static Logger logger = LoggerFactory.getLogger(KafkaSender.class);

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 发送消息
     * @param topic
     * @param obj
     */
    public void send(String topic, T obj) {
        String jsonObj = JsonUtil.obj2Str(obj);
        logger.info("------------ message = {}", jsonObj);
        //发送消息
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, jsonObj);
        future.whenComplete((result, failure) -> {
            if (failure != null) {
                logger.info("Produce: The message failed to be sent:{}", failure.getMessage());
            } else {
                //TODO 业务处理
                logger.info("Produce: The message was sent successfully:");
                logger.info("Produce: _+_+_+_+_+_+_+ result: {}", result);
            }
        });
    }

}
