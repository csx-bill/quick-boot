package com.quick.modules.online.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.common.vo.Result;
import com.quick.modules.online.entity.Request;
import com.quick.modules.online.req.RequestPageParam;
import com.quick.modules.online.service.IRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/Request")
@RequiredArgsConstructor
@Tag(name = "请求参数校验配置")
public class RequestController {
    private final IRequestService requestService;


    @PostMapping(value = "/page")
    @Operation(summary = "分页查询", description = "分页查询")
    public Result<IPage<Request>> page(@RequestBody RequestPageParam requestPageParam) {
        Page page = requestPageParam.buildPage();
        Request request = new Request();
        BeanUtils.copyProperties(requestPageParam,request);
        LambdaQueryWrapper<Request> queryWrapper = new LambdaQueryWrapper<>(request);
        return Result.success(requestService.page(page,queryWrapper));
    }

    @PostMapping(value = "/save")
    @Operation(summary = "保存", description = "保存")
    public Result<Boolean> save(@RequestBody Request request) {
        return Result.success(requestService.save(request));
    }

    @GetMapping(value = "/getById")
    @Operation(summary = "根据ID获取", description = "根据ID获取")
    public Result<Request> getById(Long id) {
        return Result.success(requestService.getById(id));
    }

    @PutMapping(value = "/updateById")
    @Operation(summary = "根据ID更新", description = "根据ID更新")
    public Result<Boolean> updateById(@RequestBody Request request) {
        return Result.success(requestService.updateById(request));
    }

    @DeleteMapping(value = "/removeById")
    @Operation(summary = "根据ID删除", description = "根据ID删除")
    public Result<Boolean> removeById(Long id) {
        return Result.success(requestService.removeById(id));
    }

    @DeleteMapping(value = "/removeBatchByIds")
    @Operation(summary = "根据ID批量删除", description = "根据ID批量删除")
    public Result<Boolean> removeBatchByIds(String ids) {
        return Result.success(requestService.removeBatchByIds(Arrays.asList(ids.split(","))));
    }
}
