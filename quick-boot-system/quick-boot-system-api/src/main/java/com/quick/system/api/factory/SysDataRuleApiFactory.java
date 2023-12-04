package com.quick.system.api.factory;

import com.quick.system.api.ISysDataRuleApi;
import com.quick.system.api.fallback.SysDataRuleApiFallback;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class SysDataRuleApiFactory implements FallbackFactory<ISysDataRuleApi> {
    @Override
    public ISysDataRuleApi create(Throwable cause) {
        SysDataRuleApiFallback fallback  = new SysDataRuleApiFallback();
        fallback.setCause(cause);
        return fallback ;
    }
}
