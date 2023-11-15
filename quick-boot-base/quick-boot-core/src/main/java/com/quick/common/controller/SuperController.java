package com.quick.common.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.util.PageParam;
import com.quick.common.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Arrays;


/**
 * 抽象类
 */
public abstract class SuperController<S extends IService<Entity>, Entity, Id extends Serializable> {

    @Autowired
    protected S baseService;

    @PostMapping(value = "/page")
    @Operation(summary = "分页查询", description = "分页查询")
    public Result<IPage<Entity>> page(@RequestBody PageParam<Entity> pageParam) {
        Page<Entity> page = pageParam.buildPage();
        Entity entity = pageParam.getModel();
        LambdaQueryWrapper<Entity> queryWrapper = new LambdaQueryWrapper(entity);
        return Result.success(baseService.page(page, queryWrapper));
    }

    @PreAuth("{}add")
    @PostMapping(value = "/save")
    @Operation(summary = "保存", description = "保存")
    public Result<Boolean> save(@RequestBody Entity entity) {
        return Result.success(baseService.save(entity));
    }

    @GetMapping(value = "/getById")
    @Operation(summary = "根据ID获取", description = "根据ID获取")
    public Result<Entity> getById(Id id) {
        return Result.success(baseService.getById(id));
    }

    @PreAuth("{}update")
    @PutMapping(value = "/updateById")
    @Operation(summary = "根据ID更新", description = "根据ID更新")
    public Result<Boolean> updateById(@RequestBody Entity entity) {
        return Result.success(baseService.updateById(entity));
    }

    @PreAuth("{}delete")
    @DeleteMapping(value = "/removeById")
    @Operation(summary = "根据ID删除", description = "根据ID删除")
    public Result<Boolean> removeById(Id id) {
        return Result.success(baseService.removeById(id));
    }

    @PreAuth("{}delete")
    @DeleteMapping(value = "/removeBatchByIds")
    @Operation(summary = "根据ID批量删除", description = "根据ID批量删除")
    public Result<Boolean> removeBatchByIds(String ids) {
        return Result.success(baseService.removeBatchByIds(Arrays.asList(ids.split(","))));
    }
}
