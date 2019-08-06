package com.example.demo.annotation.ext;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author zzs
 * @date 2019/7/8 20:51
 */
@Aspect
@Component
public class MuImpl {

    @Pointcut("@annotation(com.example.demo.annotation.ext.Mu)")
    private void cut() {

    }

    // 开始环绕   
    @Around("cut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("1");
        try {
            joinPoint.proceed();//执行程序
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("4");
    }

    @Before("cut()")
    public void before() {
        System.out.println("2");
    }

    @After("cut()")
    public void after() {
        System.out.println("5");
    }
}
