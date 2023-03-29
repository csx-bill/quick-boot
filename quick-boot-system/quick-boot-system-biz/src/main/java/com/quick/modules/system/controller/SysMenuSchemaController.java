package com.quick.modules.system.controller;

import com.quick.common.vo.Result;
import com.quick.modules.system.entity.SysMenuSchema;
import com.quick.modules.system.service.ISysMenuSchemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/SysMenuSchema")
@RequiredArgsConstructor
@Tag(name = "页面配置信息")
public class SysMenuSchemaController {
    private final ISysMenuSchemaService sysMenuSchemaService;

    @GetMapping(value = "/getMenuSchema")
    @Operation(summary = "根据菜单ID查询页面配置", description = "根据菜单ID查询页面配置")
    public Result<SysMenuSchema> getMenuSchema(@RequestParam(value = "menuId") String menuId){
        return Result.success(sysMenuSchemaService.getById(menuId));
    }

    @PostMapping(value = "/save")
    @Operation(summary = "保存页面配置", description = "保存页面配置")
    public Result<Boolean> save(@RequestBody SysMenuSchema sysMenuSchema){
        return Result.success(sysMenuSchemaService.saveOrUpdate(sysMenuSchema));
    }

    @GetMapping(value = "/getSchemaByPath")
    @Operation(summary = "根据路径查询页面配置", description = "根据路径查询页面配置")
    public Result<SysMenuSchema> getSchemaByPath(@RequestParam(value = "path") String path){
        return Result.success(sysMenuSchemaService.queryByPath(path));
    }
}
