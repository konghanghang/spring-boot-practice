package com.test.kafka.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;
import java.util.Objects;

/**
 * 计数,成功和失败数
 * @author yslao@outlook.com
 * @since 2021/3/26
 */
public class CounterInterceptor implements ProducerInterceptor<String, String> {

    private int success = 0;
    private int error = 0;

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (Objects.nonNull(metadata)) {
            success++;
        } else {
            error++;
        }
    }

    @Override
    public void close() {
        System.out.println("成功:" + success + ",失败:" + error);
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
