package com.quick.flow.handler;

import com.quick.common.tenant.TenantContext;
import com.warm.flow.core.handler.TenantHandler;

/**
 * 全局租户处理器
 */
public class FlowTenantHandler implements TenantHandler {
    @Override
    public String getTenantId() {
        return TenantContext.getTenantId().toString();
    }
}
