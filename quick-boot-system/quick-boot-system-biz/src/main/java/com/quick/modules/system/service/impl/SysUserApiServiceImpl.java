package com.quick.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.quick.modules.system.entity.SysUser;
import com.quick.modules.system.mapper.SysUserMapper;
import com.quick.modules.system.service.ISysUserApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserApiServiceImpl implements ISysUserApiService {
    private final SysUserMapper sysUserMapper;
    @Override
    public SysUser findByUsername(String username) {
        return sysUserMapper.selectOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername, username));
    }
}
