package com.quick.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.modules.system.entity.SysRole;
import com.quick.modules.system.entity.SysRoleMenu;
import com.quick.modules.system.mapper.SysRoleMapper;
import com.quick.modules.system.service.ISysRoleMenuService;
import com.quick.modules.system.service.ISysRoleService;
import com.quick.modules.system.vo.RolePermissionsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    private final ISysRoleMenuService sysRoleMenuService;
    @Override
    public RolePermissionsVO getRolePermissions(String id) {
        List<SysRoleMenu> list = sysRoleMenuService.list(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id));
        String permissions = list.stream()
                .map(SysRoleMenu::getMenuId)
                .collect(Collectors.joining(","));
        return RolePermissionsVO.builder().roleId(id).permissions(permissions).build();
    }

    @Override
    public Boolean saveRolePermissions(RolePermissionsVO vo) {
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
}
