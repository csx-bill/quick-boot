package com.quick.modules.oauth2.controller;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Handle;
import cn.dev33.satoken.stp.StpUtil;
import com.quick.common.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "token")
public class TokenEndpointController {
    // 处理所有OAuth相关请求
    @RequestMapping("/oauth2/*")
    public Object request() {
       log.info("------- 进入请求:{} ", SaHolder.getRequest().getUrl());
        return SaOAuth2Handle.serverRequest();
    }

    @GetMapping("/logout")
    @Operation(summary = "注销登录", description = "注销登录")
    public Result logout() {
        log.info("------- 注销登录 :{} ",StpUtil.getLoginId());
        StpUtil.logout();
        return Result.success();
    }
}
