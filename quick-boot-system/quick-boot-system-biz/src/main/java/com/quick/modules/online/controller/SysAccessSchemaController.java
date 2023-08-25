package com.quick.modules.online.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.common.vo.Result;
import com.quick.modules.online.entity.SysAccessSchema;
import com.quick.modules.online.service.ISysAccessSchemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/SysAccessSchema")
@RequiredArgsConstructor
@Tag(name = "表权限配置")
@PreAuth(replace = "Access:")
public class SysAccessSchemaController extends SuperController<ISysAccessSchemaService, SysAccessSchema, String> {

    @GetMapping(value = "/getSchema")
    @Operation(summary = "获取Schema", description = "获取Schema")
    public Result<SysAccessSchema> getSchema(String accessId) {
        return Result.success(baseService.getSchema(accessId));
    }

    @PutMapping(value = "/updateSchemaByAccessId")
    @Operation(summary = "根据AccessId更新Schema", description = "根据AccessId更新Schema")
    public Result<Boolean> updateSchemaByAccessId(@RequestBody SysAccessSchema entity) {
        return Result.success(baseService.update(entity, new LambdaUpdateWrapper<SysAccessSchema>()
                .eq(SysAccessSchema::getAccessId, entity.getAccessId())));
    }

}
