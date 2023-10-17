package com.quick.modules.online.controller;

import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.common.vo.Result;
import com.quick.modules.online.entity.Access;
import com.quick.modules.online.req.SyncReq;
import com.quick.modules.online.service.IAccessService;
import com.quick.modules.online.vo.AccessVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/Access")
@RequiredArgsConstructor
@Tag(name = "表权限配置")
@PreAuth(replace = "Access:")
public class AccessController extends SuperController<IAccessService, Access, String> {

    @GetMapping(value = "/list")
    @Operation(summary = "获取list", description = "获取list")
    public Result<List<Access>> list() {
        return Result.success(baseService.list());
    }

    @Operation(summary = "同步数据库表信息", description = "同步数据库表信息")
    @PostMapping(value = "/sync")
    public Result<Boolean> sync(@RequestBody SyncReq req) {
        return Result.success(baseService.sync(Arrays.asList(req.getTableNames().split(","))));
    }

    @GetMapping(value = "/tables")
    @Operation(summary = "获取tables", description = "获取tables")
    public Result<List<Map<String,String>>> tables(@RequestParam(value = "tableName",required = false) String tableName) {
        return Result.success(baseService.listByTableName(null,tableName));
    }

    @GetMapping(value = "/getAccessColumnsById")
    @Operation(summary = "根据ID获取Access与字段信息", description = "根据ID获取Access与字段信息")
    public Result<AccessVO> getAccessColumnsById(String id) {
        return Result.success(baseService.getAccessColumnsById(id));
    }

    @PreAuth("{}update")
    @PutMapping(value = "/updateAccessColumnsById")
    @Operation(summary = "根据ID更新Access与字段信息", description = "根据ID更新Access与字段信息")
    public Result<Boolean> updateAccessColumnsById(@RequestBody AccessVO entity) {
        return Result.success(baseService.updateAccessColumnsById(entity));
    }

}
