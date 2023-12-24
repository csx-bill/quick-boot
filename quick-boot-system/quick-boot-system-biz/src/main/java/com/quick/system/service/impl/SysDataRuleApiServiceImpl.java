package com.quick.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quick.common.constant.CommonConstant;
import com.quick.common.entity.BaseEntity;
import com.quick.system.entity.SysDataRule;
import com.quick.system.entity.SysMenu;
import com.quick.system.mapper.SysDataRuleMapper;
import com.quick.system.mapper.SysMenuMapper;
import com.quick.system.service.ISysDataRuleApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class SysDataRuleApiServiceImpl  implements ISysDataRuleApiService {
    private final SysDataRuleMapper sysDataRuleMapper;
    private final SysMenuMapper sysMenuMapper;

    /**
     * 根据前端访问地址查询 数据权限规则
     * @param apiPath
     * @return
     */

    @Override
    public List<SysDataRule> queryDataRuleByPath(String menuPath,String apiPath) {
        // 查询当前访问菜单id
        SysMenu sysMenu = sysMenuMapper.selectOne(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getPath, menuPath));
        if(sysMenu!=null){
            String parentId = sysMenu.getId();
            // 查询当前菜单下 为 数据权限 并且 接口地址为 apiPath
            List<SysMenu> sysMenus = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getMenuType, CommonConstant.DATA_RULE)
                    .eq(SysMenu::getParentId, parentId)
                    .eq(SysMenu::getPath,apiPath)
            );

            List<String> menuIds = sysMenus.stream().map(BaseEntity::getId).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(menuIds)){
                return sysDataRuleMapper.selectList(new LambdaQueryWrapper<SysDataRule>()
                        .in(SysDataRule::getMenuId,menuIds)
                        .eq(SysDataRule::getStatus, CommonConstant.A));
            }
        }
        return null;
    }
}
