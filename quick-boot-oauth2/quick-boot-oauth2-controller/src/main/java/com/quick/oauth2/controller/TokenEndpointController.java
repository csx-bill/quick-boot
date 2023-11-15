package com.quick.oauth2.controller;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Handle;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.quick.common.exception.UsernameNotFoundException;
import com.quick.common.exception.code.ExceptionCode;
import com.quick.common.vo.Result;
import com.quick.oauth2.dto.LoginDTO;
import com.quick.system.api.ISysUserApi;
import com.quick.system.api.dto.SysUserApiDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "token")
public class TokenEndpointController {
    @Lazy
    @Autowired
    private ISysUserApi sysUserApi;
    // 处理所有OAuth相关请求
    @RequestMapping("/oauth2/*")
    public Object request() {
       log.info("------- 进入请求:{} ", SaHolder.getRequest().getUrl());
        return SaOAuth2Handle.serverRequest();
    }

    @PostMapping("/doLogin")
    @Operation(summary = "登录", description = "登录")
    public Result doLogin(@RequestBody LoginDTO loginDTO) {
        Result<SysUserApiDTO> result = sysUserApi.findByUsername(loginDTO.getUsername());
        SysUserApiDTO sysUserApiDTO = result.getData();
        if(sysUserApiDTO!=null){
            if(sysUserApiDTO.getUsername().equals(loginDTO.getUsername()) && BCrypt.checkpw(loginDTO.getPassword(),sysUserApiDTO.getPassword())) {
                // 第1步，先登录上
                StpUtil.login(sysUserApiDTO.getId());
                // 第2步，获取 Token  相关参数
                SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
                log.info("------- 登录成功 :{} ",StpUtil.getLoginId());
                return Result.success(tokenInfo);
            }
        }
        log.info("------- 登录失败 :{} ",StpUtil.getLoginId());
        throw new UsernameNotFoundException(ExceptionCode.USERNAME_NOT_FOUND.getCode(),ExceptionCode.USERNAME_NOT_FOUND.getMsg());
    }

    @GetMapping("/logout")
    @Operation(summary = "注销登录", description = "注销登录")
    public Result logout() {
        log.info("------- 注销登录 :{} ",StpUtil.getLoginId());
        StpUtil.logout();
        return Result.success();
    }
}
