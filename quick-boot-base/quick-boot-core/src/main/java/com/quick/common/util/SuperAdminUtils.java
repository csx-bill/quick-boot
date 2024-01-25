package com.quick.common.util;

/**
 * 超级管理员工具类
 */
public class SuperAdminUtils {
    /**
     * 判断是否是超级管理员
     * @param userId
     * @return
     */
    public static boolean isSuperAdmin(Long userId){
        return userId.equals(1L);
    }

    /**
     * 超级租户
     * @param tenantId
     * @return
     */
    public static boolean isSuperTenant(Long tenantId){
        return tenantId.equals(1L);
    }

}
