package com.example.demo.utils;

import javax.validation.*;
import java.util.*;

/**
 * 校验工具类
 */
public class ValidationUtils {

    public static <T> Map<String, List<String>> validator(T t, HashSet<String> skipFields) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        if (constraintViolations != null && constraintViolations.size() > 0) {
            Map<String, List<String>> mapErr = new HashMap<>();
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                for (Path.Node node : constraintViolation.getPropertyPath()) {
                    String fieldName = node.getName();
                    //跳过校验的属性，校验错误不加入错误列表
                    if (skipFields == null || !skipFields.contains(fieldName)) {
                        List<String> lst = mapErr.get(fieldName);
                        if (lst == null) {
                            lst = new ArrayList<>();
                        }
                        lst.add(constraintViolation.getMessage());
                        mapErr.put(node.getName(), lst);
                    }
                }
            }
            return mapErr;
        }
        return null;
    }

    public static <T> Map<String, List<String>> validator(T t) {
        return validator(t, null);
    }
}
