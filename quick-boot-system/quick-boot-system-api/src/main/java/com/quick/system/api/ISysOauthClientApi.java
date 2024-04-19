package com.quick.system.api;

import com.quick.common.constant.CommonConstant;
import com.quick.common.vo.Result;
import com.quick.system.api.dto.SysOauthClientApiDTO;
import com.quick.system.api.fallback.SysOauthClientApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(contextId = "IoauthClientApi", value = CommonConstant.SERVICE_SYSTEM, fallbackFactory = SysOauthClientApiFallback.class)
public interface ISysOauthClientApi {
    /**
     * 根据应用ID查询客户端
     *
     * @param clientId
     * @return
     */
    @GetMapping("/system/oauthClient/api/findByClientId")
    Result<SysOauthClientApiDTO> findByClientId(@RequestParam(value = "clientId") String clientId);
}
