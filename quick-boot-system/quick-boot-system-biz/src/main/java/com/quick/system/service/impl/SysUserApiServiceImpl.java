package com.quick.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quick.common.constant.CacheConstant;
import com.quick.common.util.SuperAdminUtils;
import com.quick.system.entity.SysMenu;
import com.quick.system.entity.SysUser;
import com.quick.system.entity.SysUserRole;
import com.quick.system.mapper.SysMenuMapper;
import com.quick.system.mapper.SysUserMapper;
import com.quick.system.mapper.SysUserRoleMapper;
import com.quick.system.service.ISysUserApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value = CacheConstant.SYS_USER_ROLE_CACHE,key = "#p0", unless = "#result == null ")
    @Override
    public List<Long> getUserRole(Long userId) {
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
        return sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
    }

    @Cacheable(value = CacheConstant.SYS_ROLE_PERMISSION_CACHE,key = "#p0", unless = "#result == null ")
    @Override
    public List<String> getUserRolePermission(Long roleId) {
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
