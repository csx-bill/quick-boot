package com.quick.common.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.constant.CommonConstant;
import com.quick.common.util.PageParam;
import com.quick.common.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


/**
 * 抽象类
 */
public abstract class SuperController<S extends IService<Entity>, Entity, Id extends Serializable> {

    @Autowired
    protected S baseService;

    @PostMapping(value = "/page")
    @Operation(summary = CommonConstant.PAGE_MSG)
    public Result<IPage<Entity>> page(@RequestBody PageParam<Entity> pageParam) {
        Page<Entity> page = pageParam.buildPage();
        Entity entity = pageParam.getModel();
        LambdaQueryWrapper<Entity> queryWrapper = new LambdaQueryWrapper(entity);
        return Result.success(baseService.page(page, queryWrapper));
    }

    @PreAuth("{}"+ CommonConstant.ADD)
    @PostMapping(value = "/save")
    @Operation(summary = CommonConstant.SAVE_MSG)
    public Result<Boolean> save(@RequestBody Entity entity) {
        return Result.success(baseService.save(entity));
    }

    @PreAuth("{}"+ CommonConstant.VIEW)
    @GetMapping(value = "/getById")
    @Operation(summary = CommonConstant.GET_BY_ID_MSG)
    public Result<Entity> getById(Id id) {
        return Result.success(baseService.getById(id));
    }

    @PreAuth("{}"+ CommonConstant.UPDATE)
    @PutMapping(value = "/updateById")
    @Operation(summary = CommonConstant.UPDATE_BY_ID_MSG)
    public Result<Boolean> updateById(@RequestBody Entity entity) {
        return Result.success(baseService.updateById(entity));
    }

    @PreAuth("{}"+ CommonConstant.BATCHUPDATE)
    @PutMapping(value = "/updateBatchById")
    @Operation(summary = CommonConstant.UPDATE_BATCH_BY_ID_MSG)
    public Result<Boolean> updateBatchById(@RequestBody List<Entity> entity) {
        return Result.success(baseService.updateBatchById(entity));
    }

    @PreAuth("{}"+ CommonConstant.DELETE)
    @DeleteMapping(value = "/removeById")
    @Operation(summary = CommonConstant.REMOVE_BY_ID_MSG)
    public Result<Boolean> removeById(Id id) {
        return Result.success(baseService.removeById(id));
    }

    @PreAuth("{}"+ CommonConstant.BATCHDELETE)
    @DeleteMapping(value = "/removeBatchByIds")
    @Operation(summary = CommonConstant.REMOVE_BATCH_BY_IDS_MSG)
    public Result<Boolean> removeBatchByIds(String ids) {
        return Result.success(baseService.removeBatchByIds(Arrays.asList(ids.split(","))));
    }
}
