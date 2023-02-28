package com.quick.common.exception.code;

import com.quick.common.constant.CommonConstant;

public enum ExceptionCode {
    //系统相关 start
    SUCCESS(CommonConstant.SUCCESS_CODE, CommonConstant.SUCCESS_MSG),
    SYSTEM_BUSY(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, "系统繁忙~请稍后再试~"),
    SYSTEM_TIMEOUT(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, "系统维护中~请稍后再试~"),
    PARAM_EX(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, "参数类型解析异常"),
    SQL_EX(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, "运行SQL出现异常"),
    NULL_POINT_EX(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, "空指针异常"),
    ILLEGAL_ARGUMENT_EX(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, "无效参数异常"),
    MEDIA_TYPE_EX(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, "请求类型异常"),
    LOAD_RESOURCES_ERROR(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, "加载资源出错"),
    BASE_VALID_PARAM(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, "统一验证参数异常"),
    OPERATION_EX(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, "操作异常"),
    SERVICE_MAPPER_ERROR(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, "Mapper类转换异常"),
    CAPTCHA_ERROR(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, "验证码校验失败"),
    JSON_PARSE_ERROR(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, "JSON解析异常"),
    OAUTH2_CLIENT(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, "无效client_id"),
    USERNAME_NOT_FOUND(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, "用户名或密码错误"),
    ;

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    ExceptionCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
