package com.quick.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.constant.CacheConstant;
import com.quick.common.constant.CommonConstant;
import com.quick.common.exception.BizException;
import com.quick.common.util.SuperAdminUtils;
import com.quick.modules.system.entity.SysMenu;
import com.quick.modules.system.entity.SysRole;
import com.quick.modules.system.entity.SysRoleMenu;
import com.quick.modules.system.mapper.SysRoleMapper;
import com.quick.modules.system.service.ISysMenuService;
import com.quick.modules.system.service.ISysRoleMenuService;
import com.quick.modules.system.service.ISysRoleService;
import com.quick.modules.system.vo.RolePermissionsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    private final ISysRoleMenuService sysRoleMenuService;
    private final ISysMenuService sysMenuService;

    @CacheEvict(value={CacheConstant.SYS_ROLE_PERMISSION_CACHE}, allEntries=true)
    @Override
    public boolean removeById(Serializable id) {
        checkRoleAllowed(id.toString());
        return super.removeById(id);
    }

    @CacheEvict(value={CacheConstant.SYS_ROLE_PERMISSION_CACHE}, allEntries=true)
    @Override
    public boolean removeByIds(Collection<?> list) {
        for (Object id : list) {
            checkRoleAllowed(id.toString());
        }
        return super.removeByIds(list);
    }

    @Override
    public boolean updateById(SysRole entity) {
        checkRoleAllowed(entity.getId());
        return super.updateById(entity);
    }

    @Override
    public RolePermissionsVO getRolePermissions(String id) {
        String permissions = "";
        // 超级管理员角色拥有所有权限
        if(SuperAdminUtils.isSuperAdmin(id)){
            List<SysMenu> list = sysMenuService.list();
            permissions = list.stream()
                    .map(SysMenu::getId)
                    .collect(Collectors.joining(","));
        }else {
            List<SysRoleMenu> list = sysRoleMenuService.list(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id));
            permissions = list.stream()
                    .map(SysRoleMenu::getMenuId)
                    .collect(Collectors.joining(","));
        }
        return RolePermissionsVO.builder().roleId(id).permissions(permissions).build();
    }

    @Override
    public Boolean saveRolePermissions(RolePermissionsVO vo) {
        // 超级管理员角色拥有所有权限
        checkRoleAllowed(vo.getRoleId());

        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, vo.getRoleId()));
        String[] split = vo.getPermissions().split(",");
        List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
        for (String menuId : split) {
            sysRoleMenus.add(SysRoleMenu.builder().roleId(vo.getRoleId()).menuId(menuId).build());
        }
        if(!sysRoleMenus.isEmpty()){
            sysRoleMenuService.saveBatch(sysRoleMenus);
        }
        return true;
    }

    @Override
    public void checkRoleAllowed(String roleId) {
        if (SuperAdminUtils.isSuperAdmin(roleId)) {
            throw new BizException(CommonConstant.SC_INTERNAL_SERVER_ERROR_500,"不允许操作超级管理员角色");
        }
    }
}
