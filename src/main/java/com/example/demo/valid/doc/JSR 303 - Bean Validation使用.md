> 简介：

在我们开发的过程中，对于数据的校验是一项很重要的工作。尤其是现在前后端分离的情况下，我们无法保证调用者传过来的数据就是一定符合规则的，因此为了程序的健壮性，我们要尽可能的做出些防范措施。

JSR 303 是java为Bean数据合法性校验提供的标准框架，JSR-303 是JAVA EE 6 中的一项子规范，叫做Bean Validation，官方参考实现是hibernate Validator。

JSR303 用于对`JavaBean` 中的字段的值进行验证，使得验证逻辑从业务代码中脱离出来，常用于表单数据的验证。`这里要注意，因为JSR303是面向参数是Bean进行的校验，因此直接校验单个基础类型的参数是不生效的，但Spring 在此基础上进行了扩展，添加了MethodValidationPostProcessor拦截器，可实现对单个参数的校验`（后面有介绍）



## 使用

#### 1. 引入依赖

JSR依赖 `hibernate-validator`包，在 `spring-boot-starter-web` 包中已经为我们引入了该包，可以直接使用

#### 2. 使用方式

> 校验Bean：

- ①使用 @Valid + BindingResult
- ②使用 @Valid + 全局异常捕捉处理
- ③使用ValidationUtils手动进行校验

> 校验单个参数：

- 使用@Validated + MethodValidationPostProcessor 拦截器



> 总的来说，本质上就两种方式，
> 一种是校验，然后将校验结果绑定到一个对象中
> 另一种是校验，校验不通过的直接抛出异常，结合全局异常进行处理，校验通过的直接进入业务处理逻辑就行



下面分别介绍这几种方式：

首先看一下常见的校验规则：

```
JSR提供的校验注解：         
@Null           被注释的元素必须为 null    
@NotNull        被注释的元素必须不为 null    
@AssertTrue     被注释的元素必须为 true    
@AssertFalse    被注释的元素必须为 false    
@Min(value)     被注释的元素必须是一个数字，其值必须大于等于指定的最小值    
@Max(value)     被注释的元素必须是一个数字，其值必须小于等于指定的最大值    
@DecimalMin(value)  被注释的元素必须是一个数字，其值必须大于等于指定的最小值    
@DecimalMax(value)  被注释的元素必须是一个数字，其值必须小于等于指定的最大值    
@Size(max=, min=)   被注释的元素的大小必须在指定的范围内    
@Digits (integer, fraction)     被注释的元素必须是一个数字，其值必须在可接受的范围内    
@Past                   被注释的元素必须是一个过去的日期    
@Future                 被注释的元素必须是一个将来的日期    
@Pattern(regex=,flag=)  被注释的元素必须符合指定的正则表达式    


Hibernate Validator提供的校验注解：  
@NotBlank(message =)    验证字符串非null，且长度必须大于0    
@Email                  被注释的元素必须是电子邮箱地址    
@Length(min=,max=)      被注释的字符串的大小必须在指定的范围内    
@NotEmpty               被注释的字符串的必须非空    
@Range(min=,max=,message=)  被注释的元素必须在合适的范围内
```

###### 

##### 2.1 使用 @Valid + BindingResult 的方式

###### ①.首先创建我们的数据Bean

```java
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Getter
@Setter
public class TestBean {

    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotNull
    private String password;

    @Min(18)
    private Integer age;

    @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "手机号码格式错误")
    @NotBlank(message = "手机号码不能为空")
    private String phone;

    @Email
    private String email;

    @Range(min = 1, max = 3, message = "类型只能取1~3")
    private Integer type;
}
```

可以看到，直接在需要校验的字段上添加相应的校验规则就行，还可以添加校验出错时提示信息

###### ②.在Controller中使用校验

```java
// 注意，需要校验的bean对象和其绑定结果的BindingResult对象必须紧挨着成对出现
@PostMapping("/add")
public void add(@RequestBody @Valid TestBean testBean, BindingResult bindingResult) {
    System.err.println(testBean);
    if (bindingResult.hasErrors()) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            System.err.println(fieldError.getField() + ":" + fieldError.getDefaultMessage());
        }
    }
    
    // 业务代码
    // ……
}
```

直接在需要校验的参数前加`@Valid`注解即可，如果有字段校验不通过，BindingResult中就会有相应的错误信息，相应处理就行了。

![](img\01.png)



##### 2.2 使用 @Valid + 全局异常捕捉处理

简单的说这种方式跟上面一种是一样的，只不过去掉Controller中的BindingResult，然后添加一个全局异常，捕捉校验不通过抛出的异常

###### ①.Controller中

```java
@PostMapping("/add")
public void add(@RequestBody @Valid TestBean testBean) {
    System.err.println(testBean);
    
    // 业务代码
    // ……
}
```

Controller中在需要校验的参数前加`@Valid`注解即可

###### ②.编写全局异常处理类

```java
import com.example.demo.web.contract.BaseResponse;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zzs
 * @date 2019/11/25 23:35
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResponse defaultExceptionHandler(ConstraintViolationException ex) {
        Map<String, List<String>> validationErrors = Maps.newHashMap();

        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            for (Path.Node node : constraintViolation.getPropertyPath()) {
                String fieldName = node.getName();
                List<String> lst = validationErrors.get(fieldName);
                if (lst == null) {
                    lst = Lists.newArrayList();
                }
                lst.add(constraintViolation.getMessage());
                validationErrors.put(node.getName(), lst);
            }
        }
        return validationErrors;
    }
}
```

通过全局异常处理器捕捉校验出现的异常，包装成统一的样式返回给前端即可。



##### 2.3 使用ValidationUtils手动校验

这种方式其实是第一种的优化版，即去掉Controller中的@Valid注解和BindingResult，然后通过工具类去手动校验，返回统一的格式

###### ①.编写校验工具类

```java
import javax.validation.*;
import java.util.*;

/**
 * 校验工具类
 */
public class ValidationUtils {
    
    /**
     * @param t 		需要校验的类型
     * @param skipFields	不需要校验的字段
     */
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
```

###### ②.在Controller中使用

```java
@PostMapping("/edit")
public void edit(@RequestBody TestBean testBean) {
    System.err.println(testBean);
    Map<String, List<String>> validator = ValidationUtils.validator(testBean);
    if (validator != null) {
		// 校验不通过处理……
    }
}
```



##### 2.4 校验单个参数

如果参数只有很少几个字段或者就一个参数，那么定义一个Bean来封装显然有点麻烦了。然而JSR和Hibernate validator的校验只能对Object的属性进行校验，不能对单个的参数进行校验，但Spring 在此基础上进行了扩展，提供了@Validated注解，再配合MethodValidationPostProcessor拦截器，可以实现对方法参数的校验。
                                                                                                   
（@Validated是Spring Validation验证框架对参数的验证机制提供的注解，可以看作是标准JSR-303的一个变种）
                                                                                                   


使用方式如下：
首先要添加此配置：
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * @author zzs
 * @date 2019/12/8 21:39
 */
@Configuration
public class ValidateConfig {

    /**
     * JSR和Hibernate validator的校验只能对Object的属性进行校验，不能对单个的参数进行校验，
     * 但Spring 在此基础上进行了扩展，添加了MethodValidationPostProcessor拦截器，可以实现对方法参数的校验。
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
```
然后直接在Controller类上加`@Validated`注解，并且在需要校验的参数前加相应的规则，最后在配合上边的全局异常处理即可

```java
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;

/**
 * @author zzs
 * @date 2019/12/8 21:39
 */
@Validated
@RestController
public class ValidController {

    @PostMapping("/add")
    public void add(@RequestBody @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "手机号码格式错误") String telephone) {

    }
}

```

这里需要注意一下，类上加了@Validated注解后，校验不通过的直接返回异常或者走全局异常处理了，BindingResult就不生效了