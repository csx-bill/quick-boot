package com.quick.modules.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.common.vo.Result;
import com.quick.modules.system.entity.SysMenu;
import com.quick.modules.system.req.SysMenuPageParam;
import com.quick.modules.system.service.ISysMenuService;
import com.quick.modules.system.vo.SysMenuTreeVO;
import com.quick.modules.system.vo.UserMenuPermsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/SysMenu")
@RequiredArgsConstructor
@Tag(name = "菜单信息")
public class SysMenuController {

    private final ISysMenuService sysMenuService;

    @GetMapping(value = "/getUserMenu")
    @Operation(summary = "查询当前登录用户拥有的菜单权限和按钮权限", description = "查询当前登录用户拥有的菜单权限和按钮权限")
    public Result<UserMenuPermsVO> getUserMenu() {
        String userId = StpUtil.getLoginId().toString();
        return Result.success(sysMenuService.getUserMenu(userId));
    }

    @GetMapping(value = "/getUserMenuTree")
    @Operation(summary = "查询当前登录用户拥有的菜单树", description = "查询当前登录用户拥有的菜单树")
    public Result<List<SysMenuTreeVO>> getUserMenuTree() {
        String userId = StpUtil.getLoginId().toString();
        return Result.success(sysMenuService.getUserMenuTree(userId));
    }

    @PostMapping(value = "/page")
    @Operation(summary = "分页查询菜单", description = "分页查询菜单")
    public Result<IPage<SysMenu>> page(@RequestBody SysMenuPageParam sysMenuPageParam) {
        Page page = sysMenuPageParam.buildPage();
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(sysMenuPageParam,sysMenu);
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>(sysMenu);
        return Result.success(sysMenuService.page(page,queryWrapper));
    }

    @SaCheckPermission("SysMenu:add")
    @PostMapping(value = "/save")
    @Operation(summary = "保存菜单", description = "保存菜单")
    public Result<Boolean> save(@RequestBody SysMenu sysMenu) {
        return Result.success(sysMenuService.save(sysMenu));
    }

    @GetMapping(value = "/getById")
    @Operation(summary = "根据ID获取菜单", description = "根据ID获取菜单")
    public Result<SysMenu> getById(String id) {
        return Result.success(sysMenuService.getById(id));
    }

    @SaCheckPermission("SysMenu:update")
    @PutMapping(value = "/updateById")
    @Operation(summary = "根据ID更新菜单", description = "根据ID更新菜单")
    public Result<Boolean> updateById(@RequestBody SysMenu sysMenu) {
        return Result.success(sysMenuService.updateById(sysMenu));
    }

    @SaCheckPermission("SysMenu:delete")
    @DeleteMapping(value = "/removeById")
    @Operation(summary = "根据ID删除菜单", description = "根据ID删除菜单")
    public Result<Boolean> removeById(String id) {
        return Result.success(sysMenuService.removeById(id));
    }

    @SaCheckPermission("SysMenu:delete")
    @DeleteMapping(value = "/removeBatchByIds")
    @Operation(summary = "根据ID批量删除菜单", description = "根据ID批量删除菜单")
    public Result<Boolean> removeBatchByIds(String ids) {
        return Result.success(sysMenuService.removeBatchByIds(Arrays.asList(ids.split(","))));
    }
}
