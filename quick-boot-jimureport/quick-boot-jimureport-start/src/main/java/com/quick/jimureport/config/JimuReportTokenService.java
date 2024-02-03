package com.quick.jimureport.config;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 自定义积木报表鉴权(如果不进行自定义，则所有请求不做权限控制)
 * 1.自定义获取登录token
 * 2.自定义获取登录用户
 */
@Component
public class JimuReportTokenService implements JmReportTokenServiceI {

    /**
     * 通过请求获取Token
     * @param request
     * @return
     */
    @Override
    public String getToken(HttpServletRequest request) {
        return StpUtil.getTokenValue();
    }

    /**
     * 自定义获取租户
     *
     * @return
     */
    @Override
    public String getTenantId() {
//        String headerTenantId = null;
//        HttpServletRequest request = JimuSpringContextUtils.getHttpServletRequest();
//        if (request != null) {
//            headerTenantId = request.getHeader(JmConst.HEADER_TENANT_ID);
//            if(OkConvertUtils.isEmpty(headerTenantId)){
//                headerTenantId = request.getParameter(JmConst.TENANT_ID);
//            }
//        }
//        return headerTenantId;
        return "1";
    }


    /**
     * 通过Token获取登录人用户名
     * @param token
     * @return
     */
    @Override
    public String getUsername(String token) {
         //return StpUtil.getLoginIdByToken(token);;
        return "admin";
    }

    /**
     * 自定义用户拥有的角色
     *
     * @param token
     * @return
     */
    @Override
    public String[] getRoles(String token) {
        return new String[]{"admin"};
    }

    /**
     * Token校验
     * @param token
     * @return
     */
    @Override
    public Boolean verifyToken(String token) {
        System.out.println("---------verify-----Token---------------");
        //return TokenUtils.verifyToken(token, sysBaseAPI, redisUtil);
        return true;
    }

    /**
     *  自定义请求头
     * @return
     */
//    @Override
//    public HttpHeaders customApiHeader() {
//        HttpHeaders header = new HttpHeaders();
//        header.add("custom-header1", "Please set a custom value 1");
//        header.add("token", "token value 2");
//        return header;
//    }
}
