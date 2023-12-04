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
     * 根据接口地址查询数据权限规则
     * @param apiPath
     * @return
     */
    @GetMapping(value = "/SysDataRule/Api/queryDataRuleByApiPath")
    Result<List<SysDataRuleApiDTO>> queryDataRuleByApiPath(@RequestParam("apiPath") String apiPath);
}
