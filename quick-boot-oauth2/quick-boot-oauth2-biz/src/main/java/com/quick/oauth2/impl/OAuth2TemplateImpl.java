package com.quick.oauth2.impl;

import cn.dev33.satoken.oauth2.logic.SaOAuth2Template;
import cn.dev33.satoken.oauth2.model.SaClientModel;
import cn.dev33.satoken.stp.StpUtil;
import com.quick.common.exception.OAuth2Exception;
import com.quick.common.exception.code.ExceptionCode;
import com.quick.common.vo.Result;
import com.quick.system.api.ISysOauthClientApi;
import com.quick.system.api.dto.SysOauthClientApiDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class OAuth2TemplateImpl extends SaOAuth2Template {

    @Lazy
    @Autowired
    private ISysOauthClientApi sysOauthClientApi;

    @Override
    public SaClientModel getClientModel(String clientId) {
        Result<SysOauthClientApiDTO> result = sysOauthClientApi.findByClientId(clientId);
        SysOauthClientApiDTO sysOauthClientApiDTO = result.getData();
        if (sysOauthClientApiDTO == null) {
            throw new OAuth2Exception(ExceptionCode.OAUTH2_CLIENT.getCode(),ExceptionCode.OAUTH2_CLIENT.getMsg());
        }
        SaClientModel saClientModel = new SaClientModel();
        BeanUtils.copyProperties(sysOauthClientApiDTO,saClientModel);
        return saClientModel;
    }

    @Override
    public String getOpenid(String clientId, Object loginId) {
        return super.getOpenid(clientId, loginId);
    }

    /**
     * OAuth2-Server 端数据互通
     * @param clientId
     * @param loginId
     * @param scope
     * @return
     */
    @Override
    public String randomAccessToken(String clientId, Object loginId, String scope) {
        return StpUtil.createLoginSession(loginId);
    }
}
