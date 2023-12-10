package com.quick.system.controller.api;

import com.quick.common.vo.Result;
import com.quick.system.entity.SysDataRule;
import com.quick.system.service.ISysDataRuleApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/dataRule/api")
@RequiredArgsConstructor
@Tag(name = "数据权限API")
public class SysDataRuleApiController {

    private final ISysDataRuleApiService sysDataRuleApiService;

    @GetMapping(value = "/queryDataRuleByPath")
    @Operation(summary = "根据前端访问地址查询数据权限规则")
    public Result<List<SysDataRule>> queryDataRuleByPath(@RequestParam("menuPath") String menuPath,@RequestParam("apiPath") String apiPath){
        return Result.success(sysDataRuleApiService.queryDataRuleByPath(menuPath,apiPath));
    }

}
