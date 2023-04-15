package com.quick.modules.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.common.vo.Result;
import com.quick.modules.system.entity.SysDictData;
import com.quick.modules.system.req.SysDictDataPageParam;
import com.quick.modules.system.req.SysDictPageParam;
import com.quick.modules.system.service.ISysDictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/SysDictData")
@RequiredArgsConstructor
@Tag(name = "字典数据信息")
public class SysDictDataController {
    private final ISysDictDataService sysDictDataService;


    @PostMapping(value = "/page")
    @Operation(summary = "分页查询字典数据", description = "分页查询字典数据")
    public Result<IPage<SysDictData>> page(@RequestBody SysDictDataPageParam sysDictDataPageParam) {
        Page page = sysDictDataPageParam.buildPage();
        SysDictData sysDictData = new SysDictData();
        BeanUtils.copyProperties(sysDictDataPageParam,sysDictData);
        LambdaQueryWrapper<SysDictData> queryWrapper = new LambdaQueryWrapper<>(sysDictData);
        return Result.success(sysDictDataService.page(page,queryWrapper));
    }

    @PostMapping(value = "/save")
    @Operation(summary = "保存字典数据", description = "保存字典数据")
    public Result<Boolean> save(@RequestBody SysDictData sysDictData) {
        return Result.success(sysDictDataService.save(sysDictData));
    }

    @GetMapping(value = "/getById")
    @Operation(summary = "根据ID获取字典数据", description = "根据ID获取字典数据")
    public Result<SysDictData> getById(String id) {
        return Result.success(sysDictDataService.getById(id));
    }

    @PutMapping(value = "/updateById")
    @Operation(summary = "根据ID更新字典数据", description = "根据ID更新字典数据")
    public Result<Boolean> updateById(@RequestBody SysDictData sysDictData) {
        return Result.success(sysDictDataService.updateById(sysDictData));
    }

    @DeleteMapping(value = "/removeById")
    @Operation(summary = "根据ID删除字典数据", description = "根据ID删除字典数据")
    public Result<Boolean> removeById(String id) {
        return Result.success(sysDictDataService.removeById(id));
    }

    @DeleteMapping(value = "/removeBatchByIds")
    @Operation(summary = "根据ID批量删除字典数据", description = "根据ID批量删除字典数据")
    public Result<Boolean> removeBatchByIds(String ids) {
        return Result.success(sysDictDataService.removeBatchByIds(Arrays.asList(ids.split(","))));
    }

    @GetMapping(value = "/queryDictDataByDictCode")
    @Operation(summary = "通过字典code获取字典数据", description = "通过字典code获取字典数据")
    public Result<List<SysDictData>> queryDictDataByDictCode(@RequestParam("dictCode") String dictCode) {
        return Result.success(sysDictDataService.queryDictDataByDictCode(dictCode));
    }
}
