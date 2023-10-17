package com.quick.common.constant;

public interface CommonConstant {
	/** {@code 500 Server Error} (HTTP/1.0 - RFC 1945) */
    Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /** {@code 200 OK} (HTTP/1.0 - RFC 1945) */
    Integer SUCCESS_CODE = 0;

    Integer FORBIDDEN = 403;
    String SUCCESS_MSG = "success";

    String Y = "Y";

    String N = "N";

    /**
     * 菜单类型
     */
    // 菜单
    String MENU = "MENU";
    // 目录
    String DIR = "DIR";
    // 按钮
    String BUTTON = "BUTTON";

    String PARENTID = "0";



    String COLUMN_QUERY_TYPE = "COLUMN_QUERY_TYPE";
    String COLUMN_SHOW_TYPE = "COLUMN_SHOW_TYPE";
    String COLUMN_TABLE_JOIN = "COLUMN_TABLE_JOIN";

    /**
     * 系统服务名
     */
    String SERVICE_SYSTEM = "quick-boot-system-biz";
    String SERVICE_OAUTH2 = "quick-boot-oauth2-biz";
    String SERVICE_GATEWAY = "quick-boot-gateway-biz";

}
