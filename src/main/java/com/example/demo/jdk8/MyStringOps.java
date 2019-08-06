package com.example.demo.jdk8;

/**
 * @author zzs
 * @date 2019/6/12 0:12
 */
public class MyStringOps {
    //静态方法： 反转字符串
    public static String strReverse(String str) {
        String result = "";
        for (int i = str.length() - 1; i >= 0; i--) {
            result += str.charAt(i);
        }
        return result;
    }
}
