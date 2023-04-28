package com.quick.system.api.factory;

import com.quick.system.api.ISysDictApi;
import com.quick.system.api.fallback.SysDictApiFallback;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class SysDictApiFactory implements FallbackFactory<ISysDictApi> {
    @Override
    public ISysDictApi create(Throwable cause) {
        SysDictApiFallback fallback  = new SysDictApiFallback();
        fallback.setCause(cause);
        return fallback ;
    }
}
