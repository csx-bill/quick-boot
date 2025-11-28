package com.quik.boot.modules.common.vo;

import com.quik.boot.modules.common.constant.CommonConstants;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
/**
 * 通用API响应结果封装类
 * @param <T> 响应数据类型
 */
@ToString  // 自动生成toString方法
@NoArgsConstructor  // 自动生成无参构造器
@AllArgsConstructor  // 自动生成全参构造器
@Accessors(chain = true)  // 支持链式调用
@FieldNameConstants  // 生成字段名称常量
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private int status;

    @Getter
    @Setter
    private String msg;

    @Getter
    @Setter
    private T data;

    public static <T> R<T> ok() {
        return restResult(null, CommonConstants.SUCCESS, null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, CommonConstants.SUCCESS, null);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, CommonConstants.SUCCESS, msg);
    }

    public static <T> R<T> failed() {
        return restResult(null, CommonConstants.FAIL, null);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, CommonConstants.FAIL, msg);
    }

    public static <T> R<T> failed(T data) {
        return restResult(data, CommonConstants.FAIL, null);
    }
    public static <T> R<T> failed(int code, String msg) {
        return restResult(null, code, msg);
    }
    public static <T> R<T> failed(T data, String msg) {
        return restResult(data, CommonConstants.FAIL, msg);
    }

    public static <T> R<T> restResult(T data, int status, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setStatus(status);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }
}
