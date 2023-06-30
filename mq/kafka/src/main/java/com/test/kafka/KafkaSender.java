package com.test.kafka;

import com.iminling.common.json.JsonUtil;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

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
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, jsonObj);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                logger.info("Produce: The message failed to be sent:{}", throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                //TODO 业务处理
                logger.info("Produce: The message was sent successfully:");
                logger.info("Produce: _+_+_+_+_+_+_+ result: {}", stringObjectSendResult.toString());
            }
        });
    }

}
