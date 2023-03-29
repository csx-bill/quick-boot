package com.quick.modules.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.modules.system.entity.SysMenu;
import com.quick.modules.system.mapper.SysMenuMapper;
import com.quick.modules.system.service.ISysMenuService;
import com.quick.modules.system.vo.SysMenuTreeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
