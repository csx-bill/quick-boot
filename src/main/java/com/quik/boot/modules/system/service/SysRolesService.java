package com.quik.boot.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quik.boot.modules.system.entity.SysRoles;
import com.quik.boot.modules.system.req.UpdateRoleMenusParams;
import com.quik.boot.modules.system.vo.GetRoleMenusVO;


public interface SysRolesService extends IService<SysRoles> {
    Boolean updateRoleMenus(UpdateRoleMenusParams params);
    GetRoleMenusVO getRoleMenus(Long roleId);

}
