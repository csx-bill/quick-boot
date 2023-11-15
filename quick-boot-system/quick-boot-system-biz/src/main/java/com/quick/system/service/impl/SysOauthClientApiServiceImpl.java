package com.quick.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quick.system.entity.SysOauthClient;
import com.quick.system.mapper.SysOauthClientMapper;
import com.quick.system.service.ISysOauthClientApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysOauthClientApiServiceImpl implements ISysOauthClientApiService {
    private final SysOauthClientMapper sysOauthClientMapper;
    @Override
    public SysOauthClient findByClientId(String clientId) {
        return sysOauthClientMapper.selectOne(new LambdaQueryWrapper<SysOauthClient>().eq(SysOauthClient::getClientId,clientId));
    }
}
