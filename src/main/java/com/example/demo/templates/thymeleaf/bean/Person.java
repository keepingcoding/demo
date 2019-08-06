package com.example.demo.templates.thymeleaf.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zzs
 * @date 2019/8/5 10:46
 */
@Data
@AllArgsConstructor
public class Person {

    private String name;
    private int age;
    private String telephone;
    private String address;

}
