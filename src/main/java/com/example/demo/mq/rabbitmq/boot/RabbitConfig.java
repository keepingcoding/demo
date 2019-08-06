package com.example.demo.mq.rabbitmq.boot;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zzs
 * @date 2019/7/9 21:09
 */
@Configuration
public class RabbitConfig {

    /**
     * 定义队列名
     */
    private final static String STRING = "str";


    /**
     * 定义string队列
     *
     * @return
     */
    @Bean
    Queue string() {
        return new Queue(STRING);

    }
}