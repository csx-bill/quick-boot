package com.quick.common.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.constant.CommonConstant;
import com.quick.common.util.PageParam;
import com.quick.common.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;


/**
 * 抽象类
 */
public abstract class SuperController<S extends IService<Entity>,Id extends Serializable,Entity,PageQuery,SaveDTO, UpdateDTO>{

    @Autowired
    protected S baseService;
    Class<Entity> entityClass = null;

    @PostMapping(value = "/page")
    @Operation(summary = CommonConstant.PAGE_MSG)
    public Result<IPage<Entity>> page(@RequestBody PageParam<PageQuery> pageParam) {
        Page<Entity> page = pageParam.buildPage();
        Entity entity = BeanUtil.toBean(pageParam.getModel(), getEntityClass());
        LambdaQueryWrapper<Entity> queryWrapper = new LambdaQueryWrapper(entity);
        return Result.success(baseService.page(page, queryWrapper));
    }

    @PreAuth("{}"+ CommonConstant.ADD)
    @PostMapping(value = "/save")
    @Operation(summary = CommonConstant.SAVE_MSG)
    public Result<Entity> save(@RequestBody SaveDTO saveDTO) {
        Entity entity = BeanUtil.toBean(saveDTO, getEntityClass());
        baseService.save(entity);
        return Result.success(entity);
    }

    //@PreAuth("{}"+ CommonConstant.VIEW)
    @GetMapping(value = "/getById")
    @Operation(summary = CommonConstant.GET_BY_ID_MSG)
    @Parameter(name = "id",required = true)
    public Result<Entity> getById(@RequestParam("id") Id id) {
        return Result.success(baseService.getById(id));
    }

    @PreAuth("{}"+ CommonConstant.UPDATE)
    @PutMapping(value = "/updateById")
    @Operation(summary = CommonConstant.UPDATE_BY_ID_MSG)
    public Result<Boolean> updateById(@RequestBody UpdateDTO updateDTO) {
        Entity entity = BeanUtil.toBean(updateDTO, getEntityClass());
        return Result.success(baseService.updateById(entity));
    }

    @PreAuth("{}"+ CommonConstant.BATCHUPDATE)
    @PutMapping(value = "/updateBatchById")
    @Operation(summary = CommonConstant.UPDATE_BATCH_BY_ID_MSG)
    public Result<Boolean> updateBatchById(@RequestBody List<UpdateDTO> updateDTO) {
        List<Entity> entitys = BeanUtil.copyToList(updateDTO, getEntityClass());
        return Result.success(baseService.updateBatchById(entitys));
    }

    @PreAuth("{}"+ CommonConstant.DELETE)
    @DeleteMapping(value = "/removeById")
    @Operation(summary = CommonConstant.REMOVE_BY_ID_MSG)
    @Parameter(name = "id",required = true)
    public Result<Boolean> removeById(@RequestParam("id") Id id) {
        return Result.success(baseService.removeById(id));
    }

    @PreAuth("{}"+ CommonConstant.BATCHDELETE)
    @DeleteMapping(value = "/removeBatchByIds")
    @Operation(summary = CommonConstant.REMOVE_BATCH_BY_IDS_MSG)
    @Parameter(name = "ids",required = true)
    public Result<Boolean> removeBatchByIds(@RequestParam("ids") String ids) {
        return Result.success(baseService.removeBatchByIds(Arrays.asList(ids.split(","))));
    }

    public Class<Entity> getEntityClass() {
        if (entityClass == null) {
            this.entityClass = (Class<Entity>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[2];
        }
        return this.entityClass;
    }
}
