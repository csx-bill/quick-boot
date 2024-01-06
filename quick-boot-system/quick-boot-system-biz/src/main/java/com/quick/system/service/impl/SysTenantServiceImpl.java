package com.quick.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.constant.CommonConstant;
import com.quick.common.exception.BizException;
import com.quick.common.util.SuperAdminUtils;
import com.quick.system.entity.SysTenant;
import com.quick.system.mapper.SysTenantMapper;
import com.quick.system.service.ISysTenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {
    @Override
    public boolean removeById(Serializable id) {
        checkTenantAllowed(id.toString());
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<?> list) {
        for (Object id : list) {
            checkTenantAllowed(id.toString());
        }
        return super.removeByIds(list);
    }

    @Override
    public void checkTenantAllowed(String tenantId) {
        if (SuperAdminUtils.isSuperAdmin(tenantId)) {
            throw new BizException(CommonConstant.SC_INTERNAL_SERVER_ERROR_500,"不允许操作内置租户");
        }
    }

    @Override
    public boolean updateById(SysTenant entity) {
        if(entity.getStatus().equals("1")){
            checkTenantAllowed(entity.getId());
        }
        return super.updateById(entity);
    }
}
