package com.quick.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.constant.CommonConstant;
import com.quick.common.entity.BaseEntity;
import com.quick.common.util.SuperAdminUtils;
import com.quick.system.entity.SysMenu;
import com.quick.system.mapper.SysMenuMapper;
import com.quick.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public List<SysMenu> getRoutes() {
        List<SysMenu> sysMenus = list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus,CommonConstant.A).in(SysMenu::getMenuType,CommonConstant.MENU,CommonConstant.DIR,CommonConstant.ONLINE_FORM)
                .select(BaseEntity::getId,SysMenu::getParentId,SysMenu::getMenuType,SysMenu::getName,SysMenu::getPath,SysMenu::getPerms,SysMenu::getHideInMenu,SysMenu::getStatus,SysMenu::getComponent));
        // 组装菜单树
        return this.getSysMenuTree(sysMenus);
    }

    @Override
    public List<SysMenu> queryByUser(Long userId,List<String> menuTypes) {
        return baseMapper.queryByUser(userId,menuTypes);
    }


    /**
     * 查询用户拥有的菜单树和按钮权限
     * @param userId
     * @return
     */
    @Override
    public List<SysMenu> getUserMenuTree(Long userId) {
        List<SysMenu> sysMenus = getUserMenuButton(userId);
        // 组装菜单树
        return this.getSysMenuTree(sysMenus);
    }

    @Override
    public List<SysMenu> getSysMenu(SysMenu entity) {
        return list(new LambdaQueryWrapper(entity));
    }

    /**
     * 组合菜单树
     *
     * @param sysMenuList
     * @return
     */
    @Override
    public List<SysMenu> getSysMenuTree(List<SysMenu> sysMenuList) {
        //获取父节点
        List<SysMenu> treeVOList = sysMenuList.stream().filter(m -> CommonConstant.PARENTID.equals(m.getParentId())).map(
                (m) -> {
                    m.setChildren(getChildren(m, sysMenuList));
                    return m;
                }
        ).collect(Collectors.toList());

        return treeVOList;
    }

    /**
     * 递归查询子节点
     *
     * @param root        根节点
     * @param sysMenuList 所有节点
     * @return 根节点信息
     */
    @Override
    public List<SysMenu> getChildren(SysMenu root, List<SysMenu> sysMenuList) {
        List<SysMenu> children = sysMenuList.stream().filter(m -> {
            return Objects.equals(m.getParentId(), root.getId());
        }).map(
                (m) -> {
                    m.setChildren(getChildren(m, sysMenuList));
                    return m;
                }
        ).collect(Collectors.toList());
        return children;
    }

    /**
     * 查询用户拥有的菜单&按钮
     * @param userId
     * @return
     */
    @Override
    public List<SysMenu> getUserMenuButton(Long userId) {
        List<SysMenu> sysMenus = new ArrayList<>();
        // 超级管理员拥有所有权限
        if (SuperAdminUtils.isSuperAdmin(userId)) {
           sysMenus = list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus,CommonConstant.A)
                   .select(BaseEntity::getId,SysMenu::getParentId,SysMenu::getMenuType,SysMenu::getName,SysMenu::getPath,SysMenu::getPerms,SysMenu::getHideInMenu,SysMenu::getStatus));
        } else {
            sysMenus = this.queryByUser(userId,null);
        }
        return sysMenus;
    }

    /**
     * 获取用户的按钮权限
     * @param userId
     * @return
     */
    @Override
    public List<String> getUserButton(Long userId) {

        List<SysMenu> sysMenus = new ArrayList<>();
        // 超级管理员拥有所有权限
        if (SuperAdminUtils.isSuperAdmin(userId)) {
            sysMenus = list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus,CommonConstant.A)
                    .eq(SysMenu::getMenuType,CommonConstant.BUTTON)
                    .select(BaseEntity::getId,SysMenu::getParentId,SysMenu::getMenuType,SysMenu::getName,SysMenu::getPath,SysMenu::getPerms,SysMenu::getHideInMenu,SysMenu::getStatus));
        } else {
            sysMenus = this.queryByUser(userId,Arrays.asList(CommonConstant.BUTTON));
        }
        //按钮权限
        List<String> permsCode = sysMenus.stream().map(obj->obj.getPerms()).collect(Collectors.toList());

        return permsCode;
    }

    /**
     * 查询用户菜单(不包含按钮权限)
     * @param userId
     * @return
     */
    @Override
    public List<SysMenu> getUserMenu(Long userId) {
        List<SysMenu> sysMenus = new ArrayList<>();
        // 超级管理员拥有所有权限
        if (SuperAdminUtils.isSuperAdmin(userId)) {
            sysMenus = list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus,CommonConstant.A)
                    .in(SysMenu::getMenuType,CommonConstant.MENU,CommonConstant.DIR)
                    .select(BaseEntity::getId,SysMenu::getParentId,SysMenu::getMenuType,SysMenu::getName,SysMenu::getPath,SysMenu::getPerms,SysMenu::getHideInMenu,SysMenu::getStatus));
        } else {
            sysMenus = this.queryByUser(userId, Arrays.asList(CommonConstant.MENU,CommonConstant.DIR));
        }
        return getSysMenuTree(sysMenus);
    }
}
