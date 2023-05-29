package com.quick.common.aspect;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.exception.ForbiddenException;
import com.quick.common.exception.code.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 权限校验
 */
@Slf4j
@Aspect
@Component
public class PreAuthAspect {
    /**
     * 表达式处理
     */
    private static final ExpressionParser SP_EL_PARSER = new SpelExpressionParser();
    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();


    /**
     * 切 方法 和 类上的 @PreAuth 注解
     *
     * @param point 切点
     * @return Object
     * @throws Throwable 没有权限的异常
     */
    @Around("execution(public * com.quick.common.controller.SuperController.*.*(..)) || " +
            "@annotation(com.quick.common.aspect.annotation.PreAuth) || " +
            "@within(com.quick.common.aspect.annotation.PreAuth)"
    )
    public Object preAuth(ProceedingJoinPoint point) throws Throwable {
        handleAuth(point);
        return point.proceed();
    }

    /**
     * 处理权限
     *
     * @param point 切点
     */
    private void handleAuth(ProceedingJoinPoint point) {

        MethodSignature ms = (MethodSignature) point.getSignature();
        Method method = ms.getMethod();
        // 读取权限注解，优先方法上，没有则读取类
        PreAuth preAuth = null;
        if (point.getSignature() instanceof MethodSignature) {
            method = ((MethodSignature) point.getSignature()).getMethod();
            if (method != null) {
                preAuth = method.getAnnotation(PreAuth.class);
            }
        }
        String methodName = method != null ? method.getName() : "";

        // 读取目标类上的权限注解
        PreAuth targetClass = point.getTarget().getClass().getAnnotation(PreAuth.class);
        //方法和类上 均无注解
        if (preAuth == null && targetClass == null) {
            log.debug("执行方法[{}]无需校验权限", methodName);
            return;
        }


        String permission = getPermission(preAuth, targetClass);
        if (StrUtil.isBlank(permission)) {
            return;
        }

        boolean hasPermission  = StpUtil.hasPermission(permission);
        if(!hasPermission){
            throw new ForbiddenException(ExceptionCode.FORBIDDEN_EXCEPTION.getCode(),ExceptionCode.FORBIDDEN_EXCEPTION.getMsg());
        }

    }

    /**
     * 组合 类上的 与 方法上的权限
     *
     * @param preAuth
     * @param targetClass
     * @return
     */
    @Nullable
    private String getPermission(PreAuth preAuth, PreAuth targetClass) {
        String permission = preAuth == null ? targetClass.value() : preAuth.value();
        if (permission.contains("{}")) {
            if (targetClass != null && StrUtil.isNotBlank(targetClass.replace())) {
                permission = StrUtil.format(permission, targetClass.replace());
            } else {
                // 子类类上边没有 @PreAuth 注解，证明该方法不需要简要权限
                return null;
            }
        }
        return permission;
    }
}
