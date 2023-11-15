package com.quick.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.system.entity.SysMenu;
import com.quick.system.vo.SysMenuTreeVO;

import java.util.List;

public interface ISysMenuService extends IService<SysMenu> {
    List<SysMenuTreeVO> getRoutes();
    List<SysMenu> queryByUser(String userId);
    List<SysMenuTreeVO> getUserMenuTree(String userId);
    List<SysMenu> getSysMenu(SysMenu entity);
    List<SysMenuTreeVO> getSysMenuTree(List<SysMenu> sysMenuList);
    List<SysMenuTreeVO> getChildren(SysMenuTreeVO root,List<SysMenuTreeVO> sysMenuList);
    List<SysMenu> getUserMenu(String userId);
    List<String> getUserButton(String userId);
}
