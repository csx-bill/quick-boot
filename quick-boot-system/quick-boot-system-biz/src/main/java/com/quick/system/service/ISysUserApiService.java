package com.quick.system.service;

import com.quick.system.entity.SysUser;

import java.util.List;

public interface ISysUserApiService {
    SysUser findByUsername(String username);
    List<Long> getUserRole(Long userId);
    List<String> getUserRolePermission(Long roleId);
}
