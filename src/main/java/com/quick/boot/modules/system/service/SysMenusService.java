package com.quick.boot.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.boot.modules.system.entity.SysMenus;
import com.quick.boot.modules.system.req.MenusTreeParams;
import com.quick.boot.modules.system.vo.SysMenusVO;

import java.util.List;

public interface SysMenusService extends IService<SysMenus> {
    /**
     * 构建树
     * @return
     */
    List<SysMenusVO> treeMenu(MenusTreeParams params);

}
