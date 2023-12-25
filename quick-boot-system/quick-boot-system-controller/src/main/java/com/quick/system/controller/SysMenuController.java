package com.quick.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.constant.CommonConstant;
import com.quick.common.controller.SuperController;
import com.quick.common.vo.Result;
import com.quick.system.entity.SysMenu;
import com.quick.system.service.ISysMenuService;
import com.quick.system.vo.MenuSchemaVO;
import com.quick.system.vo.SysMenuTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
@Tag(name = "菜单信息")
@PreAuth(replace = "system:sys_menu:")
public class SysMenuController extends SuperController<ISysMenuService, SysMenu, String> {

    @GetMapping(value = "/getRoutes")
    @Operation(summary = "查询所有路由", description = "查询所有路由")
    public Result<List<SysMenuTreeVO>> getRoutes() {
        return Result.success(baseService.getRoutes());
    }

    @GetMapping(value = "/getUserMenuTree")
    @Operation(summary = "查询当前登录用户拥有的菜单树和按钮权限", description = "查询当前登录用户拥有的菜单树和按钮权限")
    public Result<List<SysMenuTreeVO>> getUserMenuTree() {
        String userId = StpUtil.getLoginId().toString();
        return Result.success(baseService.getUserMenuTree(userId));
    }

    @PostMapping(value = "/getSysMenuTreeSearch")
    @Operation(summary = "条件获取系统菜单树和按钮权限", description = "条件获取系统菜单树和按钮权限")
    public Result<List<SysMenuTreeVO>> getSysMenuTreeSearch(@RequestBody SysMenu entity) {
        List<SysMenu> sysMenuList = baseService.getSysMenu(entity);
        List<SysMenuTreeVO> sysMenuTree = baseService.getSysMenuTree(sysMenuList);
        return Result.success(sysMenuTree);
    }

    @GetMapping(value = "/getSysMenuTree")
    @Operation(summary = "获取系统菜单树和按钮权限", description = "获取系统菜单树和按钮权限")
    public Result<List<SysMenuTreeVO>> getSysMenuTree() {
        List<SysMenuTreeVO> sysMenuTree = baseService.getSysMenuTree(baseService.list());
        return Result.success(sysMenuTree);
    }

    @GetMapping(value = "/getSchemaById")
    @Operation(summary = "根据ID获取Schema")
    @Parameter(name = "id",required = true)
    public Result<MenuSchemaVO> getSchemaById(@RequestParam("id") String id) {
        SysMenu sysMenu = baseService.getById(id);
        return Result.success(MenuSchemaVO.builder().id(id).schema(sysMenu.getSchema()).build());
    }

    @PutMapping(value = "/updateSchemaById")
    @Operation(summary = "根据ID更新Schema")
    public Result<Boolean> updateSchemaById(@RequestBody MenuSchemaVO menuSchemaVO) {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setId(menuSchemaVO.getId());
        sysMenu.setSchema(menuSchemaVO.getSchema());
        return Result.success(baseService.updateById(sysMenu));
    }

    @GetMapping(value = "/getSchemaByPath")
    @Operation(summary = "根据path获取Schema")
    @Parameter(name = "path",required = true)
    public Result<JSONObject> getSchemaByPath(@RequestParam("path") String path) {
        SysMenu sysMenu = baseService.getOne(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getPath,path)
                .eq(SysMenu::getStatus, CommonConstant.A));
        if(sysMenu!=null){
            return Result.success(JSON.parseObject(sysMenu.getSchema()));
        }
        return Result.success(JSON.parseObject("{}"));
    }
}
