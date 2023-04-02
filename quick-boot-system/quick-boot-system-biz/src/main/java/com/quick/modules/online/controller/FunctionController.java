package com.quick.modules.online.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.common.vo.Result;
import com.quick.modules.online.entity.Function;
import com.quick.modules.online.req.FunctionPageParam;
import com.quick.modules.online.service.IFunctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/Function")
@RequiredArgsConstructor
@Tag(name = "远程函数")
public class FunctionController {
    private final IFunctionService functionService;


    @PostMapping(value = "/page")
    @Operation(summary = "分页查询", description = "分页查询")
    public Result<IPage<Function>> page(@RequestBody FunctionPageParam functionPageParam) {
        Page page = functionPageParam.buildPage();
        Function function = new Function();
        BeanUtils.copyProperties(functionPageParam,function);
        LambdaQueryWrapper<Function> queryWrapper = new LambdaQueryWrapper<>(function);
        return Result.success(functionService.page(page,queryWrapper));
    }

    @PostMapping(value = "/save")
    @Operation(summary = "保存", description = "保存")
    public Result<Boolean> save(@RequestBody Function function) {
        return Result.success(functionService.save(function));
    }

    @GetMapping(value = "/getById")
    @Operation(summary = "根据ID获取", description = "根据ID获取")
    public Result<Function> getById(Long id) {
        return Result.success(functionService.getById(id));
    }

    @PutMapping(value = "/updateById")
    @Operation(summary = "根据ID更新", description = "根据ID更新")
    public Result<Boolean> updateById(@RequestBody Function function) {
        return Result.success(functionService.updateById(function));
    }

    @DeleteMapping(value = "/removeById")
    @Operation(summary = "根据ID删除", description = "根据ID删除")
    public Result<Boolean> removeById(Long id) {
        return Result.success(functionService.removeById(id));
    }

    @DeleteMapping(value = "/removeBatchByIds")
    @Operation(summary = "根据ID批量删除", description = "根据ID批量删除")
    public Result<Boolean> removeBatchByIds(String ids) {
        return Result.success(functionService.removeBatchByIds(Arrays.asList(ids.split(","))));
    }
}
