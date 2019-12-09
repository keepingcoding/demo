package com.example.demo.valid;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

/**
 * @author zzs
 * @date 2019/12/8 22:39
 */
@ToString
@Setter
@Getter
public class DataBean {

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
