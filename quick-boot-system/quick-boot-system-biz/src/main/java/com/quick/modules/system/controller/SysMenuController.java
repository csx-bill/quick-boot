package com.quick.modules.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.quick.common.vo.Result;
import com.quick.modules.system.entity.SysMenu;
import com.quick.modules.system.service.ISysMenuService;
import com.quick.modules.system.vo.SysMenuTreeVO;
import com.quick.modules.system.vo.UserMenuPermsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
        //菜单权限和按钮权限
        List<SysMenu> sysMenus = sysMenuService.queryByUser(userId);
        //过滤掉按钮权限
        List<SysMenu> menus = sysMenus.stream().filter(sysMenu -> !sysMenu.getMenuType().equals(2)).collect(Collectors.toList());
        // 组装菜单树
        List<SysMenuTreeVO> sysMenuTree = sysMenuService.getSysMenuTree(menus);
        //按钮权限
        List<String> permsCode = sysMenus.stream().filter(m -> m.getMenuType().equals(2)).map(
                (m) -> {
                    return m.getPerms();
                }
        ).collect(Collectors.toList());

        return Result.success(UserMenuPermsVO.builder().menu(sysMenuTree).permsCode(permsCode).build());
    }

}
