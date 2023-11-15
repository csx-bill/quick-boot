package com.quick.system.service;

import com.quick.system.entity.SysOauthClient;

public interface ISysOauthClientApiService {
    /**
     * 根据应用ID查询客户端
     * @param clientId
     * @return
     */
    SysOauthClient findByClientId(String clientId);
}
