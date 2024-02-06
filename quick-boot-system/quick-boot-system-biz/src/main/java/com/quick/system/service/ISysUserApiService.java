package com.quick.system.service;

import com.quick.system.entity.SysUser;

import java.util.List;

public interface ISysUserApiService {
    SysUser findByUserId(Long userId);
    SysUser findByUsername(String username);
    List<Long> getUserRole(Long userId);
    List<String> getUserRolePermission(Long roleId);
    List<String> getUserRoleCode(Long userId);
}
