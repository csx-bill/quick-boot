package com.quick.system.api.factory;

import com.quick.system.api.ISysOauthClientApi;
import com.quick.system.api.fallback.SysOauthClientApiFallback;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class SysOauthClientApiFactory implements FallbackFactory<ISysOauthClientApi> {
    @Override
    public ISysOauthClientApi create(Throwable cause) {
        SysOauthClientApiFallback fallback  = new SysOauthClientApiFallback();
        fallback.setCause(cause);
        return fallback ;
    }
}
