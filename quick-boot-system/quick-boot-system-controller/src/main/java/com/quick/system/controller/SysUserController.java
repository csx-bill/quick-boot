package com.quick.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.common.vo.Result;
import com.quick.system.entity.SysUser;
import com.quick.system.service.ISysUserService;
import com.quick.system.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "用户信息")
@PreAuth(replace = "system:sys_user:")
public class SysUserController extends SuperController<ISysUserService, SysUser, String> {

    @GetMapping(value = "/getUserInfo")
    @Operation(summary = "获取当前用户信息", description = "获取当前用户信息")
    public Result<UserInfoVO> getUserInfo() {
        String userId = StpUtil.getLoginId().toString();
        return Result.success(baseService.getUserInfo(userId));
    }
}
