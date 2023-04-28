package com.quick.modules.system.service;

import com.quick.modules.system.entity.SysMenu;
import com.quick.modules.system.entity.SysUser;
import com.quick.modules.system.entity.SysUserRole;

import java.util.List;

public interface ISysUserApiService {
    SysUser findByUsername(String username);
    List<SysUserRole> getUserRole(String userId);
    List<String> getRolePermission(String roleId);
}
