package com.quick.common.tenant;

import com.quick.common.constant.CommonConstant;
import com.quick.common.util.SpringBeanUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class TenantContext implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        TenantContext.applicationContext = applicationContext;
    }

    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }
        return null;
    }

    /**
     * 获取请求头中的租户ID
     * @return
     */
    public static Long getTenantId() {
        Long headerTenantId = null;
        HttpServletRequest request = SpringBeanUtils.getHttpServletRequest();
        if (request != null) {
            headerTenantId = Long.parseLong(request.getHeader(CommonConstant.X_TENANT_ID));
        }
        return headerTenantId;
    }
}
