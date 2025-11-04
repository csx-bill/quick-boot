package com.quik.boot.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quik.boot.modules.system.entity.SysMenus;
import com.quik.boot.modules.system.req.MenusTreeParams;
import com.quik.boot.modules.system.vo.SysMenusVO;

import java.util.List;

public interface SysMenusService extends IService<SysMenus> {
    /**
     * 构建树
     * @return
     */
    List<SysMenusVO> treeMenu(MenusTreeParams params);

}
