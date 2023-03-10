package com.quick.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.modules.system.entity.SysMenu;
import com.quick.modules.system.vo.SysMenuTreeVO;

import java.util.List;

public interface ISysMenuService extends IService<SysMenu> {
    List<SysMenu> queryByUser(String userId);

    List<SysMenuTreeVO> getSysMenuTree(List<SysMenu> sysMenuList);
    List<SysMenuTreeVO> getChildren(SysMenuTreeVO root,List<SysMenuTreeVO> sysMenuList);
}
