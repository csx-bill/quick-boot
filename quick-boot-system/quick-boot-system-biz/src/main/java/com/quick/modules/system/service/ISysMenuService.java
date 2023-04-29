package com.quick.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.modules.system.entity.SysMenu;
import com.quick.modules.system.vo.SysMenuTreeVO;
import com.quick.modules.system.vo.UserMenuPermsVO;

import java.util.List;

public interface ISysMenuService extends IService<SysMenu> {
    List<SysMenu> queryByUser(String userId);
    UserMenuPermsVO getUserMenu(String userId);
    List<SysMenuTreeVO> getUserMenuTree(String userId);
    List<SysMenuTreeVO> getSysMenuTree(List<SysMenu> sysMenuList);
    List<SysMenuTreeVO> getChildren(SysMenuTreeVO root,List<SysMenuTreeVO> sysMenuList);
}
