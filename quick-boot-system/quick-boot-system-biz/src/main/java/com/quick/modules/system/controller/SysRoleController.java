package com.quick.modules.system.controller;

import com.quick.modules.system.service.ISysRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/SysRole")
@RequiredArgsConstructor
@Tag(name = "角色信息")
public class SysRoleController {

    private final ISysRoleService sysMenuService;
}
