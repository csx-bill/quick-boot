package com.quick.online.controller;


import com.alibaba.fastjson2.JSONObject;
import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.common.vo.Result;

import com.quick.online.dto.AccessVO;
import com.quick.online.dto.SyncDTO;
import com.quick.online.entity.Access;
import com.quick.online.service.IAccessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/access")
@RequiredArgsConstructor
@Tag(name = "权限配置")
@PreAuth(replace = "online:access:")
public class AccessController extends SuperController<IAccessService, Access, String> {


    @GetMapping(value = "/list")
    @Operation(summary = "获取list", description = "获取list")
    public Result<List<Access>> list() {
        return Result.success(baseService.list());
    }

    @Operation(summary = "同步数据库表信息", description = "同步数据库表信息")
    @PostMapping(value = "/sync")
    public Result<Boolean> sync(@RequestBody SyncDTO syncDTO) throws IOException {
        return Result.success(baseService.sync(Arrays.asList(syncDTO.getTableNames().split(","))));
    }

    @GetMapping(value = "/tables")
    @Operation(summary = "获取tables", description = "获取tables")
    public Result<List<Map<String,String>>> tables(@RequestParam(value = "tableName",required = false) String tableName) {
        return Result.success(baseService.listByTableName(null,tableName));
    }

    @GetMapping(value = "/getAccessColumnsById")
    @Operation(summary = "获取Access字段信息", description = "获取Access字段信息")
    public Result<AccessVO> getAccessColumnsById(@RequestParam(value = "id") String id) {
        return Result.success(baseService.getAccessColumnsById(id));
    }

    @PutMapping(value = "/updateAccessColumnsById")
    @Operation(summary = "更新Access字段信息", description = "更新Access字段信息")
    public Result<Boolean> updateAccessColumnsById(@RequestBody AccessVO entity) throws IOException {
        return Result.success(baseService.updateAccessColumnsById(entity));
    }


    @GetMapping(value = "/getAccessSchemaById")
    @Operation(summary = "根据ID查询表单schema", description = "根据ID查询表单schema")
    public Result<JSONObject> getAccessSchemaById(@RequestParam(value = "id") String id){
        return Result.success(baseService.getAccessSchemaById(id));
    }

    @PutMapping(value = "/refactoringCRUDById")
    @Operation(summary = "重构CRUD接口")
    public Result<Boolean> refactoringCRUDById(@RequestParam(value = "id") String id) {
        return Result.success(baseService.refactoringCRUDById(id));
    }

}
