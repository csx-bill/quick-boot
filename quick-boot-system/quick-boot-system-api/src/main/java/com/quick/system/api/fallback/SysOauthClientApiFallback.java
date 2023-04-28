package com.quick.system.api.fallback;

import com.quick.common.vo.Result;
import com.quick.system.api.ISysOauthClientApi;
import com.quick.system.api.dto.SysOauthClientApiDTO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SysOauthClientApiFallback implements ISysOauthClientApi {
    @Setter
    private Throwable cause;

    @Override
    public Result<SysOauthClientApiDTO> findByClientId(String clientId) {
        log.error("请求失败 {}", cause);
        return null;
    }
}
