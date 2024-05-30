package com.quick.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.system.entity.SysMenu;

import java.util.List;

public interface ISysMenuService extends IService<SysMenu> {
    List<SysMenu> getRoutes();
    List<SysMenu> queryByUser(Long userId,List<String> menuTypes);
    List<SysMenu> getUserMenuTree(Long userId);
    List<SysMenu> getSysMenu(SysMenu entity);
    List<SysMenu> getSysMenuTree(List<SysMenu> sysMenuList);
    List<SysMenu> getChildren(SysMenu root,List<SysMenu> sysMenuList);
    List<SysMenu> getUserMenuButton(Long userId);
    List<String> getUserButton(Long userId);
    List<SysMenu> getUserMenu(Long userId);
}
