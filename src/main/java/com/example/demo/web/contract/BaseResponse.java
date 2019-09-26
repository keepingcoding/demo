package com.example.demo.web.contract;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author zzs
 * @date 2019/9/26 14:43
 */
@Getter
@Setter
public class BaseResponse<T> implements Serializable {

    //状态码
    private String statusCode = "200";

    //状态描述
    private String msg = "ok";

    //业务处理代码
    private String resultCode;

    //业务模型对象
    private T result;

    //参数校验错误列表
    private Map<String, List<String>> validationErrors;

    public BaseResponse() {

    }

    public BaseResponse(T result) {
        this.result = result;
    }

    public BaseResponse(String statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public BaseResponse(ServiceException e) {
        this(e.getMessage(), e.getStatusCode(), e.getResultCode());
    }

    public BaseResponse(Map<String, List<String>> validationErrors) {
        this(ServiceException.PARAM_VERIFY_EXCEPTION, validationErrors);
    }

    public BaseResponse(ServiceException e, Map<String, List<String>> validationErrors) {
        this(e);
        this.validationErrors = validationErrors;
    }

    public BaseResponse(String statusCode, String resultCode, String msg) {
        this.statusCode = statusCode;
        this.resultCode = resultCode;
        this.msg = msg;
    }

}
