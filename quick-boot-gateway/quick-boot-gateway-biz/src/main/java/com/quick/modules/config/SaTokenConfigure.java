package com.quick.modules.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.quick.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * [Sa-Token 权限认证] 配置类
 */
@Slf4j
@Configuration
public class SaTokenConfigure {

    // 注册 Sa-Token全局过滤器
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")    /* 拦截全部path */
                // 开放地址
                .addExclude("/favicon.ico",
                        "/doc.html",
                        "/**/v3/api-docs/**",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/actuator/**",
                        "/instances/**",
                        "/api/system/SysMenu/getRoutes",
                        "/**/doLogin",
                        "/**/oauth2/**"
                )
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    // 登录校验 -- 拦截所有路由
                    SaRouter.match("/**", r -> StpUtil.checkLogin());
                })
                // 前置函数：在每次认证函数之前执行
                .setBeforeAuth(obj -> {
                // ---------- 设置跨域响应头 ----------
                    SaHolder.getResponse()
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", "*")
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "*")
                            // 有效时间
                            .setHeader("Access-Control-Max-Age", "3600")
                            // 允许的header参数
                            .setHeader("Access-Control-Allow-Headers", "*");

                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            .free(r -> {
                            })
                            .back();
                })
                // 异常处理方法：每次setAuth函数出现异常时进入
                .setError(e -> {
                    log.error("SaReactorFilter:{}", e.getMessage());
                    return JSON.toJSONString(Result.fail(e.getMessage()));
                })
                ;
    }
}
