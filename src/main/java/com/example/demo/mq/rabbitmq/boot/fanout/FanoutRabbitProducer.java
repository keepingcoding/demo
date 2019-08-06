package com.example.demo.mq.rabbitmq.boot.fanout;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zzs
 * @date 2019/7/9 22:44
 */
public class FanoutRabbitProducer {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        String context = "hello, this is message";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("FanoutExchange", "", context);
    }
}
