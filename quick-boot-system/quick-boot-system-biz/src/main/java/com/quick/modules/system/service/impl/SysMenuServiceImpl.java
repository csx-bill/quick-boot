package com.quick.modules.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.util.SuperAdminUtils;
import com.quick.common.vo.Result;
import com.quick.modules.system.entity.SysMenu;
import com.quick.modules.system.mapper.SysMenuMapper;
import com.quick.modules.system.service.ISysMenuService;
import com.quick.modules.system.vo.SysMenuTreeVO;
import com.quick.modules.system.vo.UserMenuPermsVO;
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
    public List<SysMenu> queryByUser(String userId) {
        return baseMapper.queryByUser(userId);
    }

    @Override
    public UserMenuPermsVO getUserMenu(String userId) {
        List<SysMenu> sysMenus = new ArrayList<>();
        // 超级管理员拥有所有权限
        if(SuperAdminUtils.isSuperAdmin(userId)){
            sysMenus = baseMapper.getSuperAdminMenus();
        }else {
           sysMenus = this.queryByUser(userId);
        }
        //过滤掉按钮权限
        List<SysMenu> menus = sysMenus.stream().filter(sysMenu -> !sysMenu.getMenuType().equals("button")).collect(Collectors.toList());
        // 组装菜单树
        List<SysMenuTreeVO> sysMenuTree = this.getSysMenuTree(menus);
        //按钮权限
        List<String> permsCode = sysMenus.stream().filter(m -> m.getMenuType().equals("button")).map(
                (m) -> {
                    return m.getPerms();
                }
        ).collect(Collectors.toList());

        return UserMenuPermsVO.builder().menu(sysMenuTree).permsCode(permsCode).build();
    }

    @Override
    public List<SysMenuTreeVO> getUserMenuTree(String userId) {
        List<SysMenu> sysMenus = new ArrayList<>();
        // 超级管理员拥有所有权限
        if(SuperAdminUtils.isSuperAdmin(userId)){
            sysMenus = baseMapper.getSuperAdminMenus();
        }else {
            sysMenus = this.queryByUser(userId);
        }
        // 组装菜单树
        return this.getSysMenuTree(sysMenus);
    }

    /**
     * 组合当前登录拥有的菜单权限
     *
     * @param sysMenuList
     * @return
     */
    @Override
    public List<SysMenuTreeVO> getSysMenuTree(List<SysMenu> sysMenuList) {
        //集合拷贝
        List<SysMenuTreeVO> list = JSON.parseArray(JSON.toJSONString(sysMenuList), SysMenuTreeVO.class);

        //获取父节点
        List<SysMenuTreeVO> treeVOList = list.stream().filter(m -> "0".equals(m.getParentId())).map(
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

}
