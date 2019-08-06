package com.example.demo.jdk8;

import java.net.SocketTimeoutException;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * jdk8新特性 —— Optional类
 * @author zzs
 * @date 2019/6/11 23:11
 */
public class OptionalTest {
    public static void main(String[] args) {
        Integer value1 = null;
        Integer value2 = 10;

        Optional optional = Optional.ofNullable(value1);
        boolean present = optional.isPresent();


    }
}
