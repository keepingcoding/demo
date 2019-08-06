package com.example.demo.apollo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApolloTest {

    @Value("${name:jack}")
    private String name;

    @Value("${age:20}")
    private int age;

    @Test
    public void testGetConfig() {
        System.err.println("name = " + name);
        System.err.println("age = " + age);
    }
}
