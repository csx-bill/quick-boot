package com.quick.system.controller.api;

import cn.dev33.satoken.annotation.SaIgnore;
import com.quick.common.vo.Result;
import com.quick.system.service.ISysOauthClientApiService;
import com.quick.system.entity.SysOauthClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/SysOauthClient/Api")
@RequiredArgsConstructor
@Tag(name = "客户端信息API")
public class SysOauthClientApiController {

    private final ISysOauthClientApiService sysOauthClientApiService;

    @SaIgnore
    @GetMapping(value = "/findByClientId")
    @Operation(summary = "查询客户端", description = "根据应用ID查询客户端")
    public Result<SysOauthClient> findByClientId(@RequestParam(value = "clientId") String clientId) {
        return Result.success(sysOauthClientApiService.findByClientId(clientId));
    }

}
