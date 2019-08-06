package com.example.demo.annotation.ext;

import org.springframework.stereotype.Service;

/**
 * @author zzs
 * @date 2019/7/8 20:58
 */
@Service
public class TestService {

    @Mu
    public void test(){
        System.err.println("this is service code.");
    }
}
