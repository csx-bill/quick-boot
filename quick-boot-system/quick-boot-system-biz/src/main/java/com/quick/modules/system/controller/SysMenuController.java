package com.quick.modules.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.common.vo.Result;
import com.quick.modules.system.entity.SysMenu;
import com.quick.modules.system.service.ISysMenuService;
import com.quick.modules.system.vo.MenuSchemaVO;
import com.quick.modules.system.vo.SysMenuTreeVO;
import com.quick.modules.system.vo.UserMenuPermsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/SysMenu")
@RequiredArgsConstructor
@Tag(name = "菜单信息")
@PreAuth(replace = "SysMenu:")
public class SysMenuController extends SuperController<ISysMenuService, SysMenu, String> {

    @GetMapping(value = "/getUserMenu")
    @Operation(summary = "查询当前登录用户拥有的菜单权限和按钮权限", description = "查询当前登录用户拥有的菜单权限和按钮权限")
    public Result<UserMenuPermsVO> getUserMenu() {
        String userId = StpUtil.getLoginId().toString();
        return Result.success(baseService.getUserMenu(userId));
    }

    @GetMapping(value = "/getUserMenuTree")
    @Operation(summary = "查询当前登录用户拥有的菜单树", description = "查询当前登录用户拥有的菜单树")
    public Result<List<SysMenuTreeVO>> getUserMenuTree() {
        String userId = StpUtil.getLoginId().toString();
        return Result.success(baseService.getUserMenuTree(userId));
    }
    @GetMapping(value = "/getSchema")
    @Operation(summary = "获取Schema", description = "获取Schema")
    public Result<MenuSchemaVO> getSchema(String id) {
        MenuSchemaVO vo = new MenuSchemaVO();
        SysMenu sysMenu = baseService.getById(id);
        BeanUtils.copyProperties(sysMenu, vo);
        return Result.success(vo);
    }

    @PutMapping(value = "/updateSchemaById")
    @Operation(summary = "根据ID更新Schema", description = "根据ID更新Schema")
    public Result<Boolean> updateById(@RequestBody MenuSchemaVO vo) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(vo, sysMenu);
        return Result.success(baseService.updateById(sysMenu));
    }
}
