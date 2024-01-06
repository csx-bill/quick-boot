package com.quick.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.system.entity.SysTenant;

public interface ISysTenantService extends IService<SysTenant> {
    void checkTenantAllowed(String tenantId);
}
