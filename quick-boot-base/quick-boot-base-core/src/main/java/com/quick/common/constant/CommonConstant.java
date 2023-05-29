package com.quick.common.constant;

public interface CommonConstant {
	/** {@code 500 Server Error} (HTTP/1.0 - RFC 1945) */
    Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /** {@code 200 OK} (HTTP/1.0 - RFC 1945) */
    Integer SUCCESS_CODE = 0;

    Integer FORBIDDEN = 403;
    String SUCCESS_MSG = "success";
    /**
     * 系统服务名
     */
    String SERVICE_SYSTEM = "quick-boot-system-biz";
    String SERVICE_OAUTH2 = "quick-boot-oauth2-biz";
    String SERVICE_GATEWAY = "quick-boot-gateway-biz";

}
