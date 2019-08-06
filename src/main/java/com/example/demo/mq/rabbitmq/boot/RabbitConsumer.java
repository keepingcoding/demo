package com.example.demo.mq.rabbitmq.boot;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zzs
 * @date 2019/7/9 21:12
 */
@Component
@RabbitListener(queues = "direct.message")
public class RabbitConsumer {


    /**
     * 消息消费
     *
     * @RabbitHandler 代表此方法为接受到消息后的处理方法
     */
    @RabbitHandler
    public void received(String msg) {
        System.out.println("[str] received message:" + msg);
    }

}
