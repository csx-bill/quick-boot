package com.quick.common.feign;

import cn.dev33.satoken.stp.StpUtil;
import com.quick.common.constant.CommonConstant;
import com.quick.common.tenant.TenantContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;

/**
 * feign拦截器功能, 解决header丢失问题
 */
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        template.header(CommonConstant.TENANT_ID, TenantContext.getTenantId().toString());
        template.header(HttpHeaders.AUTHORIZATION, StpUtil.getTokenValue());
    }
}
