package com.example.demo.mq.rabbitmq.boot.topic;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zzs
 * @date 2019/7/9 22:30
 */
@Component
public class TopicRabbitProducer {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send1() {

        String context = "hello, this is message 1";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("TopicExchange", "topic.message", context);
    }

    public void send2() {
        String context = "hello, this is message 2";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("TopicExchange", "topic.message2", context);
    }
}
