package com.quick.system.controller.api;

import cn.dev33.satoken.annotation.SaIgnore;
import com.quick.common.vo.Result;
import com.quick.system.service.ISysMenuApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/SysMenu/Api")
@RequiredArgsConstructor
@Tag(name = "菜单信息API")
public class SysMenuApiController {
    private final ISysMenuApiService sysMenuApiService;

    @SaIgnore
    @GetMapping(value = "/getPermission")
    @Operation(summary = "查询菜单权限", description = "查询菜单权限")
    public Result<List<String>> getPermission() {
        return Result.success(sysMenuApiService.getPermission());
    }
}
