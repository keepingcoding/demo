package com.example.demo.mq.rabbitmq.boot.direct;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zzs
 * @date 2019/7/9 22:51
 */
@Configuration
public class DirectRabbitConfig {

    @Bean
    public Queue directQueue() {
        return new Queue("direct.message");
    }


    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("DirectExchange");
    }

    @Bean
    public Binding bindingExchangeMessage() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with("direct.route.key");
    }
}
