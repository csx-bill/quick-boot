package com.quick.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.common.vo.Result;
import com.quick.modules.system.entity.SysMenuSchema;
import com.quick.modules.system.req.SysMenuPageParam;
import com.quick.modules.system.req.SysMenuSchemaPageParam;
import com.quick.modules.system.service.ISysMenuSchemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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

    @PostMapping(value = "/page")
    @Operation(summary = "分页查询", description = "分页查询")
    public Result<IPage<SysMenuSchema>> page(@RequestBody SysMenuSchemaPageParam pageParam) {
        Page page = pageParam.buildPage();
        SysMenuSchema sysMenu = new SysMenuSchema();
        BeanUtils.copyProperties(pageParam,sysMenu);
        LambdaQueryWrapper<SysMenuSchema> queryWrapper = new LambdaQueryWrapper<>(sysMenu);
        return Result.success(sysMenuSchemaService.page(page,queryWrapper));
    }

    @PostMapping(value = "/save")
    @Operation(summary = "保存", description = "保存")
    public Result<Boolean> save(@RequestBody SysMenuSchema sysMenuSchema){
        return Result.success(sysMenuSchemaService.save(sysMenuSchema));
    }

    @GetMapping(value = "/getById")
    @Operation(summary = "根据ID获取", description = "根据ID获取")
    public Result<SysMenuSchema> getById(String id) {
        return Result.success(sysMenuSchemaService.getById(id));
    }

    @PutMapping(value = "/updateById")
    @Operation(summary = "根据ID更新", description = "根据ID更新")
    public Result<Boolean> updateById(@RequestBody SysMenuSchema sysMenuSchema) {
        return Result.success(sysMenuSchemaService.updateById(sysMenuSchema));
    }

    @DeleteMapping(value = "/removeById")
    @Operation(summary = "根据ID删除", description = "根据ID删除")
    public Result<Boolean> removeById(String id) {
        return Result.success(sysMenuSchemaService.removeById(id));
    }

    @DeleteMapping(value = "/removeBatchByIds")
    @Operation(summary = "根据ID批量删除", description = "根据ID批量删除")
    public Result<Boolean> removeBatchByIds(String ids) {
        return Result.success(sysMenuSchemaService.removeBatchByIds(Arrays.asList(ids.split(","))));
    }

    @GetMapping(value = "/getSchemaByPath")
    @Operation(summary = "根据路径查询页面配置", description = "根据路径查询页面配置")
    public Result<SysMenuSchema> getSchemaByPath(@RequestParam(value = "path") String path){
        return Result.success(sysMenuSchemaService.queryByPath(path));
    }
}
