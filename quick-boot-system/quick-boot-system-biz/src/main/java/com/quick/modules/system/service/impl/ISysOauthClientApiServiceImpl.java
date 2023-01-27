package com.quick.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quick.modules.system.entity.SysOauthClient;
import com.quick.modules.system.mapper.SysOauthClientMapper;
import com.quick.modules.system.service.ISysOauthClientApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ISysOauthClientApiServiceImpl implements ISysOauthClientApiService {
    private final SysOauthClientMapper sysOauthClientMapper;
    @Override
    public SysOauthClient findByClientId(String clientId) {
        return sysOauthClientMapper.selectOne(new LambdaQueryWrapper<SysOauthClient>().eq(SysOauthClient::getClientId,clientId));
    }
}
