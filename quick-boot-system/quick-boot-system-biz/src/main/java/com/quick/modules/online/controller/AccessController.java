package com.quick.modules.online.controller;

import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.common.vo.Result;
import com.quick.modules.online.entity.Access;
import com.quick.modules.online.service.IAccessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
