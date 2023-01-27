package com.quick.modules.system.api.controller;

import com.quick.common.vo.Result;
import com.quick.modules.system.entity.SysUser;
import com.quick.modules.system.service.ISysUserApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/SysUser/Api")
@RequiredArgsConstructor
@Tag(name = "用户信息API")
public class SysUserApiController {
    private final ISysUserApiService sysUserApiService;
    @GetMapping(value = "/findByUsername")
    @Operation(summary = "查询用户", description = "根据用户账号查询用户")
    public Result<SysUser> findByUsername(@RequestParam(value = "username") String username) {
        return Result.success(sysUserApiService.findByUsername(username));
    }
}
