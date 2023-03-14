package com.quick.common.handler;

import com.quick.common.exception.OAuth2Exception;
import com.quick.common.exception.UsernameNotFoundException;
import com.quick.common.exception.code.ExceptionCode;
import com.quick.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;

/**
 * 全局异常处理
 */
@Slf4j
public abstract class AbstractGlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> nullPointerException(Exception ex){
        log.error("NullPointerException:{}", ex.getMessage());
        return Result.fail(ExceptionCode.NULL_POINT_EX.getCode(),ExceptionCode.NULL_POINT_EX.getMsg());
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> sqlException(SQLException ex) {
        log.error("SQLException:{}", ex.getMessage());
        return Result.fail(ExceptionCode.SQL_EX.getCode(),ExceptionCode.SQL_EX.getMsg() );
    }

    @ExceptionHandler(OAuth2Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> exception(OAuth2Exception ex){
        log.error("OAuth2Exception:{}", ex.getMsg());
        return Result.fail(ex.getCode(),ex.getMsg());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> usernameNotFoundException(UsernameNotFoundException ex){
        log.error("UsernameNotFoundException:{}", ex.getMsg());
        return Result.fail(ex.getCode(),ex.getMsg());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> exception(Exception ex){
        log.error("Exception:{}", ex.getMessage());
        return Result.fail(ExceptionCode.SYSTEM_BUSY.getCode(),ExceptionCode.SYSTEM_BUSY.getMsg());
    }
}
