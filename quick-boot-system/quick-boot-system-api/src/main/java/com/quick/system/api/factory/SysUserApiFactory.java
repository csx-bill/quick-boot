package com.quick.system.api.factory;

import com.quick.system.api.ISysUserApi;
import com.quick.system.api.fallback.SysUserApiFallback;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class SysUserApiFactory implements FallbackFactory<ISysUserApi> {
    @Override
    public ISysUserApi create(Throwable cause) {
        SysUserApiFallback fallback  = new SysUserApiFallback();
        fallback.setCause(cause);
        return fallback ;
    }
}
