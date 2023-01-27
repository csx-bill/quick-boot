package com.quick.modules.system.service;

import com.quick.modules.system.entity.SysOauthClient;

public interface ISysOauthClientApiService {
    /**
     * 根据应用ID查询客户端
     * @param clientId
     * @return
     */
    SysOauthClient findByClientId(String clientId);
}
