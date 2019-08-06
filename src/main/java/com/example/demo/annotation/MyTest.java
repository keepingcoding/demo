package com.example.demo.annotation;

import java.lang.annotation.Annotation;

/**
 * @author zhaozisheng
 * @version 1.0
 * @date 2019-05-15 11:26
 */
public class MyTest {
    public static void main(String[] args) {
        boolean annotation = TestClass.class.isAnnotationPresent(TestAnnotation.class);
        System.err.println(annotation);

        TestAnnotation annotation1 = TestClass.class.getAnnotation(TestAnnotation.class);
        System.err.println(annotation1.name());

        Annotation[] annotations = TestClass.class.getAnnotations();
        for (Annotation  a: annotations) {
            if(a instanceof TestAnnotation){
                TestAnnotation t = (TestAnnotation)a;
                System.err.println(t.name());
            }
        }


    }
}
