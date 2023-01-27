package com.quick.modules.system.controller;

import com.quick.common.vo.Result;
import com.quick.common.vo.SysOauthClientVo;
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
@RequestMapping("/SysOauthClient")
@RequiredArgsConstructor
@Tag(name = "客户端信息")
public class SysOauthClientController {
/*    @GetMapping(value = "/findByClientId")
    @Operation(summary = "查询客户端", description = "根据应用ID查询客户端")
    public Result<SysOauthClientVo> findByClientId(@RequestParam(value = "clientId") String clientId) {
        return Result.success(sysUserService.findByUsername(username));
    }*/
}
