package com.quick.modules.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.common.vo.Result;
import com.quick.modules.system.entity.SysDict;
import com.quick.modules.system.req.SysDictPageParam;
import com.quick.modules.system.service.ISysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;


@Slf4j
@RestController
@RequestMapping("/SysDict")
@RequiredArgsConstructor
@Tag(name = "字典信息")
public class SysDictController {
    private final ISysDictService sysDictService;


    @PostMapping(value = "/page")
    @Operation(summary = "分页查询字典", description = "分页查询字典")
    public Result<IPage<SysDict>> page(@RequestBody SysDictPageParam sysDictPageParam) {
        Page page = sysDictPageParam.buildPage();
        SysDict sysDict = new SysDict();
        BeanUtils.copyProperties(sysDictPageParam,sysDict);
        LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<>(sysDict);
        return Result.success(sysDictService.page(page,queryWrapper));
    }

    @PostMapping(value = "/save")
    @Operation(summary = "保存字典", description = "保存字典")
    public Result<Boolean> save(@RequestBody SysDict sysDict) {
        return Result.success(sysDictService.save(sysDict));
    }

    @GetMapping(value = "/getById")
    @Operation(summary = "根据ID获取字典", description = "根据ID获取字典")
    public Result<SysDict> getById(String id) {
        return Result.success(sysDictService.getById(id));
    }

    @PutMapping(value = "/updateById")
    @Operation(summary = "根据ID更新字典", description = "根据ID更新字典")
    public Result<Boolean> updateById(@RequestBody SysDict sysDict) {
        return Result.success(sysDictService.updateById(sysDict));
    }

    @DeleteMapping(value = "/removeById")
    @Operation(summary = "根据ID删除字典", description = "根据ID删除字典")
    public Result<Boolean> removeById(String id) {
        return Result.success(sysDictService.removeById(id));
    }

    @DeleteMapping(value = "/removeBatchByIds")
    @Operation(summary = "根据ID批量删除字典", description = "根据ID批量删除字典")
    public Result<Boolean> removeBatchByIds(String ids) {
        return Result.success(sysDictService.removeBatchByIds(Arrays.asList(ids.split(","))));
    }
}
