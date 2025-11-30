package com.quick.boot.modules.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.boot.modules.common.constant.CommonConstants;
import com.quick.boot.modules.common.project.ProjectContextHolder;
import com.quick.boot.modules.common.util.TreeUtil;
import com.quick.boot.modules.system.entity.SysMenus;
import com.quick.boot.modules.system.entity.SysPages;
import com.quick.boot.modules.system.entity.SysRoleMenu;
import com.quick.boot.modules.system.entity.SysUserRole;
import com.quick.boot.modules.system.mapper.*;
import com.quick.boot.modules.system.req.MenusTreeParams;
import com.quick.boot.modules.system.service.SysMenusService;
import com.quick.boot.modules.system.vo.SysMenusVO;
import com.quick.boot.modules.system.vo.SysProjectsVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SysMenusServiceImpl extends ServiceImpl<SysMenusMapper, SysMenus> implements SysMenusService {

    private final SysPagesMapper sysPagesMapper;
    private final SysProjectsMapper sysProjectsMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenusVO> treeMenu(MenusTreeParams params) {
        Long projectId = ProjectContextHolder.getProjectId();
        long userId = StpUtil.getLoginIdAsLong();

        // 获取项目用户信息
        SysProjectsVO projectInfo = sysProjectsMapper.selectProjectByUserIdAndProjectId(projectId, userId);
        if (projectInfo == null) {
            return Collections.emptyList();
        }

        List<SysMenus> menuList = null;


        Long parentId = params.getParentId();
        String name = params.getName();
        String menuType = params.getMenuType();

        Long parent = Objects.isNull(parentId) ? CommonConstants.MENU_TREE_ROOT_ID : parentId;

        // 管理员直接返回项目所有菜单
        if (CommonConstants.PROJECT_ADMIN.equals(projectInfo.getUserType())) {
            menuList = baseMapper
                    .selectList(Wrappers.<SysMenus>lambdaQuery()
                            .eq(SysMenus::getProjectId, projectId)
                            .like(StringUtils.hasText(name), SysMenus::getName, name)
                            .eq(StringUtils.hasText(menuType), SysMenus::getMenuType, menuType)
                            .orderByAsc(SysMenus::getSortOrder));
        }else {
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

            menuList = baseMapper
                    .selectList(Wrappers.<SysMenus>lambdaQuery()
                            .eq(SysMenus::getProjectId, projectId)
                            .like(StringUtils.hasText(name), SysMenus::getName, name)
                            .eq(StringUtils.hasText(menuType), SysMenus::getMenuType, menuType)
                            .in(SysMenus::getId,menuIds)
                            .orderByAsc(SysMenus::getSortOrder));
        }


        if(CollectionUtils.isEmpty(menuList)){
            return new ArrayList<>();
        }

        Set<Long> pageIds = menuList.stream()
                .map(SysMenus::getPageId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, String> pageNameMap = CollectionUtils.isEmpty(pageIds)
                ? new HashMap<>()
                : sysPagesMapper.selectByIds(pageIds).stream()
                .collect(Collectors.toMap(
                        SysPages::getId,
                        SysPages::getPageName,
                        (a, b) -> a
                ));


        List<SysMenusVO> menuVoList = menuList.stream().map(menu -> {
            SysMenusVO vo = new SysMenusVO();
            BeanUtils.copyProperties(menu, vo);
            Optional.ofNullable(menu.getPageId())
                    .map(pageNameMap::get)
                    .ifPresent(vo::setPageName);
            return vo;
        }).collect(Collectors.toList());

        // 模糊查询 不组装树结构 直接返回 表格方便编辑
        if (StringUtils.hasText(name)) {
            return menuVoList;
        }

        return TreeUtil.buildTree(
                menuVoList,
                SysMenusVO::getId,
                SysMenusVO::getParentId,
                SysMenusVO::setChildren,
                TreeUtil.createComparator(SysMenusVO::getSortOrder, true, true),
                Set.of(parent)  // 根节点
        );
    }
}
