package com.example.demo.valid;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * @author zzs
 * @date 2019/11/25 23:23
 */
@Configuration
public class ValidateConfig {

    /**
     * JSR和Hibernate validator的校验只能对Object的属性进行校验，不能对单个的参数进行校验，
     * 但Spring 在此基础上进行了扩展，添加了MethodValidationPostProcessor拦截器，可以实现对方法参数的校验。
     *
     * todo 可以直接使用，不用单独配置此类
     *
     * 使用时需要在类上面添加@Validated，参数上直接添加校验规则就行了
     * 类上加了@Validated注解后，校验不通过的直接返回异常或者走全局异常处理了，BindingResult就不生效了
     *
     * @return
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
