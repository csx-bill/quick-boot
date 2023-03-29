package com.quick.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.common.vo.Result;
import com.quick.modules.system.entity.SysRole;
import com.quick.modules.system.service.ISysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/SysRole")
@RequiredArgsConstructor
@Tag(name = "角色信息")
public class SysRoleController {

    private final ISysRoleService sysRoleService;
    @GetMapping(value = "/page")
    @Operation(summary = "分页查询角色", description = "分页查询角色")
    public Result<IPage<SysRole>> page(Page<SysRole> page) {
        return Result.success(sysRoleService.page(page));
    }

}
