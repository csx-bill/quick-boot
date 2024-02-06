package com.quick.jimureport.config;

import cn.dev33.satoken.stp.StpUtil;
import com.quick.common.constant.CommonConstant;
import com.quick.common.util.SpringBeanUtils;
import com.quick.system.api.ISysUserApi;
import com.quick.system.api.dto.SysUserApiDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 自定义积木报表鉴权(如果不进行自定义，则所有请求不做权限控制)
 * 1.自定义获取登录token
 * 2.自定义获取登录用户
 */
@Component
public class JimuReportTokenService implements JmReportTokenServiceI {
    @Autowired
    private ISysUserApi sysUserApi;

    /**
     * 通过请求获取Token
     * @param request
     * @return
     */
    @Override
    public String getToken(HttpServletRequest request) {
        return request.getHeader(CommonConstant.X_ACCESS_TOKEN);
    }

    /**
     * 自定义获取租户
     *
     * @return
     */
    @Override
    public String getTenantId() {
        HttpServletRequest request = SpringBeanUtils.getHttpServletRequest();
        return request.getHeader(CommonConstant.X_TENANT_ID);
    }


    /**
     * 通过Token获取登录人用户名
     * @param token
     * @return
     */
    @Override
    public String getUsername(String token) {
        Object userId = StpUtil.getLoginIdByToken(token);
        SysUserApiDTO data = sysUserApi.findByUserId(Long.parseLong(userId.toString())).getData();
        return data.getUsername();
    }

    /**
     * 自定义用户拥有的角色
     *
     * @param token
     * @return
     */
    @Override
    public String[] getRoles(String token) {
        Object userId = StpUtil.getLoginIdByToken(token);
        List<String> userRoleCode = sysUserApi.getUserRoleCode(Long.parseLong(userId.toString())).getData();
        return userRoleCode.toArray(new String[0]);
    }

    /**
     * Token校验
     * @param token
     * @return
     */
    @Override
    public Boolean verifyToken(String token) {
        return true;
    }

    /**
     *  自定义请求头
     * @return
     */
//    @Override
//    public HttpHeaders customApiHeader() {
//        HttpServletRequest request = SpringBeanUtils.getHttpServletRequest();
//
//        HttpHeaders header = new HttpHeaders();
//        header.add(CommonConstant.X_TENANT_ID, request.getHeader(CommonConstant.X_TENANT_ID));
//        header.add(CommonConstant.X_ACCESS_TOKEN, request.getHeader(CommonConstant.X_ACCESS_TOKEN));
//        return header;
//    }
}
