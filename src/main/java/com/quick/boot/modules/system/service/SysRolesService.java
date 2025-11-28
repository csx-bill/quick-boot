package com.quick.boot.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.boot.modules.system.entity.SysRoles;
import com.quick.boot.modules.system.req.UpdateRoleMenusParams;
import com.quick.boot.modules.system.vo.GetRoleMenusVO;

import java.util.List;


public interface SysRolesService extends IService<SysRoles> {
    Boolean updateRoleMenus(UpdateRoleMenusParams params);
    GetRoleMenusVO getRoleMenus(Long roleId);
    List<SysRoles> getUserRoles();

}
