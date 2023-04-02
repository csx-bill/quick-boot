package com.quick.modules.online.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.common.vo.Result;
import com.quick.modules.online.entity.Access;
import com.quick.modules.online.service.IAccessService;
import com.quick.modules.online.req.AccessPageParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/Access")
@RequiredArgsConstructor
@Tag(name = "表权限配置")
public class AccessController {
    private final IAccessService accessService;


    @PostMapping(value = "/page")
    @Operation(summary = "分页查询", description = "分页查询")
    public Result<IPage<Access>> page(@RequestBody AccessPageParam accessPageParam) {
        Page page = accessPageParam.buildPage();
        Access access = new Access();
        BeanUtils.copyProperties(accessPageParam,access);
        LambdaQueryWrapper<Access> queryWrapper = new LambdaQueryWrapper<>(access);
        return Result.success(accessService.page(page,queryWrapper));
    }

    @PostMapping(value = "/save")
    @Operation(summary = "保存", description = "保存")
    public Result<Boolean> save(@RequestBody Access access) {
        return Result.success(accessService.save(access));
    }

    @GetMapping(value = "/getById")
    @Operation(summary = "根据ID获取", description = "根据ID获取")
    public Result<Access> getById(Long id) {
        return Result.success(accessService.getById(id));
    }

    @PutMapping(value = "/updateById")
    @Operation(summary = "根据ID更新", description = "根据ID更新")
    public Result<Boolean> updateById(@RequestBody Access access) {
        return Result.success(accessService.updateById(access));
    }

    @DeleteMapping(value = "/removeById")
    @Operation(summary = "根据ID删除", description = "根据ID删除")
    public Result<Boolean> removeById(Long id) {
        return Result.success(accessService.removeById(id));
    }

    @DeleteMapping(value = "/removeBatchByIds")
    @Operation(summary = "根据ID批量删除", description = "根据ID批量删除")
    public Result<Boolean> removeBatchByIds(String ids) {
        return Result.success(accessService.removeBatchByIds(Arrays.asList(ids.split(","))));
    }
}
