package com.quick.system.api.factory;

import com.quick.system.api.ISysMenuApi;
import com.quick.system.api.fallback.SysMenuApiFallback;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class SysMenuApiFactory implements FallbackFactory<ISysMenuApi> {
    @Override
    public ISysMenuApi create(Throwable cause) {
        SysMenuApiFallback fallback  = new SysMenuApiFallback();
        fallback.setCause(cause);
        return fallback ;
    }
}
