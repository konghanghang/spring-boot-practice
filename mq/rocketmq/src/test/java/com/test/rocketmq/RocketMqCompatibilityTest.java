package com.test.rocketmq;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.rocketmq.model.User;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConverter;

import static org.assertj.core.api.Assertions.assertThat;

class RocketMqCompatibilityTest {

    @Test
    void autoConfigurationAndLegacyMessageFormatWorkWithoutBroker() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(RocketMQAutoConfiguration.class))
                .run(context -> {
                    assertThat(context).hasNotFailed().hasSingleBean(RocketMQMessageConverter.class);
                    // 不配置 name-server / producer.group，因此不会创建连接 broker 的 producer。
                    assertThat(context).doesNotHaveBean("defaultMQProducer");
                    MessageConverter converter = context.getBean(RocketMQMessageConverter.class).getMessageConverter();
                    User user = new User();
                    user.setName("zhansan");
                    user.setPassword("123456");
                    Message<?> message = converter.toMessage(user, null);
                    assertThat(message).isNotNull();
                    Object payload = message.getPayload();
                    String json = payload instanceof byte[] bytes
                            ? new String(bytes, StandardCharsets.UTF_8) : payload.toString();
                    JsonNode actual = new ObjectMapper().readTree(json);
                    assertThat(actual).isEqualTo(new ObjectMapper().readTree(
                            "{\"name\":\"zhansan\",\"password\":\"123456\",\"roles\":[],\"passwordGenerated\":false}"));
                    User restored = (User) converter.fromMessage(message, User.class);
                    assertThat(restored).isNotNull();
                    assertThat(restored.getName()).isEqualTo(user.getName());
                    assertThat(restored.getPassword()).isEqualTo(user.getPassword());
                    assertThat(restored.getRoles()).isEqualTo(List.of());
                    assertThat(restored.isPasswordGenerated()).isFalse();
                });
    }
}
