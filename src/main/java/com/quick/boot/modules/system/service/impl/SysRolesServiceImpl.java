package com.quick.boot.modules.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.boot.modules.common.constant.CommonConstants;
import com.quick.boot.modules.common.project.ProjectContextHolder;
import com.quick.boot.modules.system.entity.SysMenus;
import com.quick.boot.modules.system.entity.SysRoleMenu;
import com.quick.boot.modules.system.entity.SysRoles;
import com.quick.boot.modules.system.entity.SysUserRole;
import com.quick.boot.modules.system.mapper.*;
import com.quick.boot.modules.system.req.UpdateRoleMenusParams;
import com.quick.boot.modules.system.service.SysRolesService;
import com.quick.boot.modules.system.vo.GetRoleMenusVO;
import com.quick.boot.modules.system.vo.SysProjectsVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SysRolesServiceImpl extends ServiceImpl<SysRolesMapper, SysRoles> implements SysRolesService {

    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysProjectsMapper sysProjectsMapper;
    private final SysMenusMapper sysMenusMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRoleMenus(UpdateRoleMenusParams params) {
        Long projectId = ProjectContextHolder.getProjectId();
        sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getRoleId, params.getId()).eq(SysRoleMenu::getProjectId,projectId));
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
                    rm.setProjectId(projectId);
                    return rm;
                })
                .collect(Collectors.toList());

        sysRoleMenuMapper.insert(roleMenuList);

        return Boolean.TRUE;
    }

    @Override
    public GetRoleMenusVO getRoleMenus(Long roleId) {
        Long projectId = ProjectContextHolder.getProjectId();
        GetRoleMenusVO roleMenusVO = new GetRoleMenusVO();
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getProjectId,projectId).eq(SysRoleMenu::getRoleId, roleId));
        String menuIds = sysRoleMenus.stream()
                .map(roleMenu -> String.valueOf(roleMenu.getMenuId()))
                .collect(Collectors.joining(","));
        roleMenusVO.setMenuIds(menuIds);
        roleMenusVO.setId(roleId);
        return roleMenusVO;
    }

    /**
     * 当前用户的项目角色
     * @return
     */
    @Override
    public List<String> getUserRoles() {
        Long projectId = ProjectContextHolder.getProjectId();
        long userId = StpUtil.getLoginIdAsLong();
        SysProjectsVO sysProjectsVO = sysProjectsMapper.selectProjectByUserIdAndProjectId(projectId, userId);
        // 返回管理员角色编码
        if(sysProjectsVO.getUserType().equals(CommonConstants.PROJECT_ADMIN)){
            return Arrays.asList(CommonConstants.PROJECT_ADMIN);
        }
        // 返回普通用户角色编码
        return Arrays.asList(sysProjectsVO.getUserType());
    }

    /**
     * 当前用户的项目角色权限码集合
     * @return
     */
    @Override
    public List<String> getRolesPermissions() {
        Long projectId = ProjectContextHolder.getProjectId();
        long userId = StpUtil.getLoginIdAsLong();

        // 获取项目用户信息
        SysProjectsVO projectInfo = sysProjectsMapper.selectProjectByUserIdAndProjectId(projectId, userId);
        if (projectInfo == null) {
            return Collections.emptyList();
        }

        // 管理员直接返回项目所有权限
        if (CommonConstants.PROJECT_ADMIN.equals(projectInfo.getUserType())) {
            List<SysMenus> adminMenus = sysMenusMapper.selectList(
                    Wrappers.<SysMenus>lambdaQuery()
                            .eq(SysMenus::getProjectId, projectId)
                            .select(SysMenus::getPermission)
            );
            return adminMenus.stream()
                    .map(SysMenus::getPermission)
                    .collect(Collectors.toList());
        }

        // 普通用户根据角色查询权限
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(
                Wrappers.<SysUserRole>lambdaQuery()
                        .eq(SysUserRole::getProjectId, projectId)
                        .eq(SysUserRole::getUserId, userId)
                        .select(SysUserRole::getRoleId)
        );

        if (CollectionUtils.isEmpty(userRoles)) {
            return Collections.emptyList();
        }

        List<Long> roleIds = userRoles.stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());

        List<SysRoleMenu> roleMenus = sysRoleMenuMapper.selectList(
                Wrappers.<SysRoleMenu>lambdaQuery()
                        .eq(SysRoleMenu::getProjectId, projectId)
                        .in(SysRoleMenu::getRoleId, roleIds)
                        .select(SysRoleMenu::getMenuId)
        );

        if (CollectionUtils.isEmpty(roleMenus)) {
            return Collections.emptyList();
        }

        List<Long> menuIds = roleMenus.stream()
                .map(SysRoleMenu::getMenuId)
                .distinct() // 添加去重，避免重复查询
                .collect(Collectors.toList());

        List<SysMenus> userMenus = sysMenusMapper.selectByIds(menuIds);
        return userMenus.stream()
                .map(SysMenus::getPermission)
                .collect(Collectors.toList());
    }
}
