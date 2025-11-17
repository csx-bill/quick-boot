package com.quik.boot.modules.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quik.boot.modules.system.entity.SysRoleMenu;
import com.quik.boot.modules.system.entity.SysRoles;
import com.quik.boot.modules.system.mapper.SysRoleMenuMapper;
import com.quik.boot.modules.system.mapper.SysRolesMapper;
import com.quik.boot.modules.system.req.UpdateRoleMenusParams;
import com.quik.boot.modules.system.service.SysRolesService;
import com.quik.boot.modules.system.vo.GetRoleMenusVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SysRolesServiceImpl extends ServiceImpl<SysRolesMapper, SysRoles> implements SysRolesService {

    private final SysRoleMenuMapper sysRoleMenuMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRoleMenus(UpdateRoleMenusParams params) {
        sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getRoleId, params.getId()));
        if (!StringUtils.hasText(params.getMenuIds())) {
            return Boolean.TRUE;
        }
        String[] menuIds = params.getMenuIds().split(",");
        // 批量创建角色菜单关联对象
        List<SysRoleMenu> roleMenuList = Arrays.asList(menuIds).stream()
                .map(menuId -> {
                    SysRoleMenu rm = new SysRoleMenu();
                    rm.setRoleId(params.getId());
                    rm.setMenuId(Long.parseLong(menuId));
                    return rm;
                })
                .collect(Collectors.toList());

        sysRoleMenuMapper.insert(roleMenuList);

        return Boolean.TRUE;
    }

    @Override
    public GetRoleMenusVO getRoleMenus(Long roleId) {
        GetRoleMenusVO roleMenusVO = new GetRoleMenusVO();
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getRoleId, roleId));
        String menuIds = sysRoleMenus.stream()
                .map(roleMenu -> String.valueOf(roleMenu.getMenuId()))
                .collect(Collectors.joining(","));
        roleMenusVO.setMenuIds(menuIds);
        roleMenusVO.setId(roleId);
        return roleMenusVO;
    }
}
