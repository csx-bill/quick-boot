package com.quick.system.api.fallback;

import com.quick.common.vo.Result;
import com.quick.system.api.ISysDataRuleApi;
import com.quick.system.api.dto.SysDataRuleApiDTO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SysDataRuleApiFallback implements ISysDataRuleApi {
    @Setter
    private Throwable cause;

    @Override
    public Result<List<SysDataRuleApiDTO>> queryDataRuleByPath(String menuPath,String apiPath) {
        log.error("请求失败 {}", cause);
        return null;
    }
}
