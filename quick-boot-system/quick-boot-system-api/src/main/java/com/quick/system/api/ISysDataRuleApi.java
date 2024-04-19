package com.quick.system.api;

import com.quick.common.constant.CommonConstant;
import com.quick.common.vo.Result;
import com.quick.system.api.dto.SysDataRuleApiDTO;
import com.quick.system.api.factory.SysDictApiFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 数据权限 api
 */
@Component
@FeignClient(contextId = "ISysDataRuleApi", value = CommonConstant.SERVICE_SYSTEM, fallbackFactory = SysDictApiFactory.class)
public interface ISysDataRuleApi {

    /**
     * 根据前端访问地址查询 数据权限规则
     * @param apiPath
     * @return
     */
    @GetMapping(value = "/system/dataRule/api/queryDataRuleByPath")
    Result<List<SysDataRuleApiDTO>> queryDataRuleByPath(@RequestParam("menuPath") String menuPath,@RequestParam("apiPath") String apiPath);
}
