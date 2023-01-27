package com.quick.system.api;

import com.quick.common.vo.Result;
import com.quick.system.api.dto.SysUserApiDTO;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface ISysUserApi {
    @GetExchange("/SysUser/Api/findByUsername")
    Result<SysUserApiDTO> findByUsername(@RequestParam(value = "username") String username);
}
