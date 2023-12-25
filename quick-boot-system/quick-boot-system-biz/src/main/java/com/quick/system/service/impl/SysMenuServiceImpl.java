package com.quick.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.constant.CommonConstant;
import com.quick.common.entity.BaseEntity;
import com.quick.common.util.SuperAdminUtils;
import com.quick.system.entity.SysMenu;
import com.quick.system.mapper.SysMenuMapper;
import com.quick.system.service.ISysMenuService;
import com.quick.system.vo.SysMenuTreeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public List<SysMenuTreeVO> getRoutes() {
        List<SysMenu> sysMenus = list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus,CommonConstant.A).in(SysMenu::getMenuType,CommonConstant.MENU,CommonConstant.DIR)
                .select(BaseEntity::getId,SysMenu::getParentId,SysMenu::getMenuType,SysMenu::getName,SysMenu::getPath,SysMenu::getPerms,SysMenu::getHideInMenu,SysMenu::getStatus));
        // 组装菜单树
        return this.getSysMenuTree(sysMenus);
    }

    @Override
    public List<SysMenu> queryByUser(String userId) {
        return baseMapper.queryByUser(userId);
    }


    /**
     * 查询用户拥有的菜单树和按钮权限
     * @param userId
     * @return
     */
    @Override
    public List<SysMenuTreeVO> getUserMenuTree(String userId) {
        List<SysMenu> sysMenus = getUserMenu(userId);
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
    public List<SysMenuTreeVO> getSysMenuTree(List<SysMenu> sysMenuList) {
        //集合拷贝
        List<SysMenuTreeVO> list = JSON.parseArray(JSON.toJSONString(sysMenuList), SysMenuTreeVO.class);

        //获取父节点
        List<SysMenuTreeVO> treeVOList = list.stream().filter(m -> CommonConstant.PARENTID.equals(m.getParentId())).map(
                (m) -> {
                    m.setChildren(getChildren(m, list));
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
    public List<SysMenuTreeVO> getChildren(SysMenuTreeVO root, List<SysMenuTreeVO> sysMenuList) {
        List<SysMenuTreeVO> children = sysMenuList.stream().filter(m -> {
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
    public List<SysMenu> getUserMenu(String userId) {
        List<SysMenu> sysMenus = new ArrayList<>();
        // 超级管理员拥有所有权限
        if (SuperAdminUtils.isSuperAdmin(userId)) {
           sysMenus = list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus,CommonConstant.A)
                   .select(BaseEntity::getId,SysMenu::getParentId,SysMenu::getMenuType,SysMenu::getName,SysMenu::getPath,SysMenu::getPerms,SysMenu::getHideInMenu,SysMenu::getStatus));
        } else {
            sysMenus = this.queryByUser(userId);
        }
        return sysMenus;
    }

    /**
     * 获取用户的按钮权限
     * @param userId
     * @return
     */
    @Override
    public List<String> getUserButton(String userId) {
        List<SysMenu> sysMenus = getUserMenu(userId);
        //按钮权限
        List<String> permsCode = sysMenus.stream().filter(m -> m.getMenuType().equals(CommonConstant.BUTTON)).map(
                (m) -> {
                    return m.getPerms();
                }
        ).collect(Collectors.toList());
        return permsCode;
    }

}
