package com.quick.system.api;

import com.quick.common.vo.Result;
import com.quick.system.api.dto.SysOauthClientApiDTO;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(contentType = "application/json")
public interface ISysOauthClientApi {
    /**
     * 根据应用ID查询客户端
     *
     * @param clientId
     * @return
     */
    @GetExchange("/SysOauthClient/Api/findByClientId")
    Result<SysOauthClientApiDTO> findByClientId(@RequestParam(value = "clientId") String clientId);
}
