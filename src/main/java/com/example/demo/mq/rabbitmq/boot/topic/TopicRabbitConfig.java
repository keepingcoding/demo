package com.example.demo.mq.rabbitmq.boot.topic;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zzs
 * @date 2019/7/9 22:27
 */
@Configuration
public class TopicRabbitConfig {

    @Bean
    public Queue topicQueueA() {
        return new Queue("topic.message");
    }

    @Bean
    public Queue topicQueueB() {
        return new Queue("topic.message2");
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("TopicExchange");
    }

    @Bean
    public Binding bindingExchangeMessage(Queue topicQueueA, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueueA).to(topicExchange).with("topic.message");
    }

    @Bean
    public Binding bindingExchangeMessages(Queue topicQueueB, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueueB).to(topicExchange).with("topic.#");
    }
}
