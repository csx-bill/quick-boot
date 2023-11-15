package com.quick.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quick.system.entity.SysMenu;
import com.quick.system.mapper.SysMenuMapper;
import com.quick.system.service.ISysMenuApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuApiServiceImpl implements ISysMenuApiService {
    private final SysMenuMapper sysMenuMapper;

    @Override
    public List<String> getPermission() {
        List<SysMenu> permission = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getMenuType, "button"));
        return permission.stream().map(SysMenu::getPerms).collect(Collectors.toList());
    }
}
