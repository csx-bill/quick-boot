package com.quick.modules.system.controller;

import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.common.vo.Result;
import com.quick.modules.system.entity.SysMenuSchema;
import com.quick.modules.system.service.ISysMenuSchemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/SysMenuSchema")
@RequiredArgsConstructor
@Tag(name = "页面配置信息")
@PreAuth(replace = "SysMenuSchema:")
public class SysMenuSchemaController extends SuperController<ISysMenuSchemaService, SysMenuSchema, String> {

    @GetMapping(value = "/getSchemaByPath")
    @Operation(summary = "根据路径查询页面配置", description = "根据路径查询页面配置")
    public Result<SysMenuSchema> getSchemaByPath(@RequestParam(value = "path") String path) {
        return Result.success(baseService.queryByPath(path));
    }
}
