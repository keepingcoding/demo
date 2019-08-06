package com.example.demo.mq.rabbitmq.boot.direct;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zzs
 * @date 2019/7/9 22:30
 */
@Component
public class DirectRabbitProducer {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        String context = "hello, this is message";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("DirectExchange", "direct.route.key", context);
    }
}
