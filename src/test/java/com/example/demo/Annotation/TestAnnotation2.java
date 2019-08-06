package com.example.demo.Annotation;

import com.example.demo.annotation.ext.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zzs
 * @date 2019/7/8 20:56
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestAnnotation2 {

    @Autowired
    private TestService testService;

    @Test
    public void test1(){
        this.testService.test();
    }
}
