package com.quick.boot.modules.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.quick.boot.modules.common.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理类
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(CheckedException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public R checkedException(CheckedException e) {
        log.error("校验异常信息 ex={}", e.getMessage(), e);
        return R.failed(e.getLocalizedMessage());
    }

    /**
     * 登录过期
     * @param e
     * @return
     */
    @ExceptionHandler(NotLoginException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public R notLoginException(NotLoginException e) {
        log.error("登录过期异常信息 ex={}", e.getMessage(), e);
        return R.failed(HttpStatus.UNAUTHORIZED.value(),e.getLocalizedMessage());
    }

    /**
     * 文件找不到
     * @param e
     * @return
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public R noResourceFoundException(NoResourceFoundException e) {
        log.error("文件找不到异常信息 ex={}", e.getMessage(), e);
        return R.failed(HttpStatus.NOT_FOUND.value(),e.getLocalizedMessage());
    }

    /**
     * 全局异常信息
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    public R exception(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        return R.failed(e.getLocalizedMessage());
    }

}
