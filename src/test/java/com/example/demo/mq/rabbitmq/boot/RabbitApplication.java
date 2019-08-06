package com.example.demo.mq.rabbitmq.boot;

import com.example.demo.mq.rabbitmq.boot.direct.DirectRabbitProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zzs
 * @date 2019/7/9 21:14
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitApplication {

    @Autowired
    private RabbitProducer producer;

    @Autowired
    private DirectRabbitProducer directRabbitProducer;

    @Test
    public void testStringSend() {
        directRabbitProducer.send();
    }
}