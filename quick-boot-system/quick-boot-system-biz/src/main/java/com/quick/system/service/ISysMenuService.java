package com.quick.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.system.entity.SysMenu;

import java.util.List;

public interface ISysMenuService extends IService<SysMenu> {
    List<SysMenu> getRoutes();
    List<SysMenu> queryByUser(String userId);
    List<SysMenu> getUserMenuTree(String userId);
    List<SysMenu> getSysMenu(SysMenu entity);
    List<SysMenu> getSysMenuTree(List<SysMenu> sysMenuList);
    List<SysMenu> getChildren(SysMenu root,List<SysMenu> sysMenuList);
    List<SysMenu> getUserMenu(String userId);
    List<String> getUserButton(String userId);
}
