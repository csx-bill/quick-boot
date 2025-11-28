package com.quick.boot.modules.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.boot.modules.common.constant.CommonConstants;
import com.quick.boot.modules.common.util.TreeUtil;
import com.quick.boot.modules.system.entity.SysMenus;
import com.quick.boot.modules.system.entity.SysPages;
import com.quick.boot.modules.system.mapper.SysMenusMapper;
import com.quick.boot.modules.system.mapper.SysPagesMapper;
import com.quick.boot.modules.system.req.MenusTreeParams;
import com.quick.boot.modules.system.service.SysMenusService;
import com.quick.boot.modules.system.vo.SysMenusVO;
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

    @Override
    public List<SysMenusVO> treeMenu(MenusTreeParams params) {
        Long parentId = params.getParentId();
        String name = params.getName();
        String menuType = params.getMenuType();

        Long parent = Objects.isNull(parentId) ? CommonConstants.MENU_TREE_ROOT_ID : parentId;

        List<SysMenus> menuList = baseMapper
                .selectList(Wrappers.<SysMenus>lambdaQuery()
                        .eq(SysMenus::getProjectId, params.getProjectId())
                        .like(StringUtils.hasText(name), SysMenus::getName, name)
                        .eq(StringUtils.hasText(menuType), SysMenus::getMenuType, menuType)
                        .orderByAsc(SysMenus::getSortOrder));

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
