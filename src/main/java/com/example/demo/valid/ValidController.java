package com.example.demo.valid;

import com.example.demo.utils.ValidationUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

/**
 * @author zzs
 * @date 2019/12/8 21:39
 */
@Validated
@RestController
public class ValidController {

    /**
     * 1.使用 @Valid + BindingResult 的方式
     *
     * @param dataBean
     * @param bindingResult
     */
    @PostMapping("/add")
    public void add(@RequestBody @Valid DataBean dataBean, BindingResult bindingResult) {
        System.err.println(dataBean);
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                System.err.println(fieldError.getField() + ":" + fieldError.getDefaultMessage());
            }
        }
    }

    /**
     * 2.使用 @Valid + 全局异常捕捉处理
     *
     * @param dataBean
     */
    @PostMapping("/edit")
    public void edit(@RequestBody @Valid DataBean dataBean) {
        System.err.println(dataBean);

    }

    /**
     * 3.使用ValidationUtils手动校验
     *
     * @param dataBean
     */
    @PostMapping("/edit2")
    public void edit2(@RequestBody DataBean dataBean) {
        System.err.println(dataBean);
        Map<String, List<String>> validator = ValidationUtils.validator(dataBean);
        if (validator != null) {

        }
    }

    /**
     * 4.校验单个参数
     *
     * @param telephone
     */
    @PostMapping("/testSingleParam")
    public void testSingleParam(@RequestBody @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "手机号码格式错误") String telephone) {
        System.err.println(telephone);
    }
}
