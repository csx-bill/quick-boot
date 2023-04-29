package com.quick.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quick.common.util.SuperAdminUtils;
import com.quick.modules.system.entity.SysMenu;
import com.quick.modules.system.entity.SysUser;
import com.quick.modules.system.entity.SysUserRole;
import com.quick.modules.system.mapper.SysMenuMapper;
import com.quick.modules.system.mapper.SysUserMapper;
import com.quick.modules.system.mapper.SysUserRoleMapper;
import com.quick.modules.system.service.ISysUserApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserApiServiceImpl implements ISysUserApiService {
    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysMenuMapper sysMenuMapper;
    @Override
    public SysUser findByUsername(String username) {
        return sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
    }

    @Override
    public List<String> getUserRole(String userId) {
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
        return sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
    }

    @Override
    public List<String> getUserRolePermission(String roleId) {
        List<SysMenu> sysRoleMenus = new ArrayList<>();
        // 超级管理员角色拥有所有权限
        if(SuperAdminUtils.isSuperAdmin(roleId)){
            return Arrays.asList("*");
        }else {
            sysRoleMenus = sysMenuMapper.getUserRolePermission(roleId);
        }
        return sysRoleMenus.stream().map(SysMenu::getPerms).collect(Collectors.toList());
    }
}
