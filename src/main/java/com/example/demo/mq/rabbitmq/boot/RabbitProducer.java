package com.example.demo.mq.rabbitmq.boot;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zzs
 * @date 2019/7/9 21:10
 */
@Component
public class RabbitProducer {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        Date date = new Date();
        String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
        System.out.println("[str] send msg:" + dateString);

        // 第一个参数为刚刚定义的队列名称
        this.rabbitTemplate.convertAndSend("str", dateString);
    }
}