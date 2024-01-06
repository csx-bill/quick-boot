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
    public static boolean isSuperAdmin(String userId){
        return userId.equals("1");
    }

    /**
     * 超级租户
     * @param tenantId
     * @return
     */
    public static boolean isSuperTenant(String tenantId){
        return tenantId.equals("1");
    }

}
