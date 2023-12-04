package com.quick.online.parser;

import apijson.RequestMethod;
import apijson.framework.APIJSONObjectParser;
import apijson.orm.Join;
import apijson.orm.SQLConfig;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.quick.common.constant.CommonConstant;
import com.quick.common.util.SpringBeanUtils;
import com.quick.online.config.OnlineSQLConfig;
import com.quick.system.api.ISysDataRuleApi;
import com.quick.system.api.dto.SysDataRuleApiDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 统一处理 创建人 创建时间 更新人 更新时间 自动填充
 */
public class OnlineObjectParser extends APIJSONObjectParser {
    public OnlineObjectParser(HttpSession session, JSONObject request, String parentPath, SQLConfig arrayConfig, boolean isSubquery, boolean isTable, boolean isArrayMainTable) throws Exception {
        super(session, request, parentPath, arrayConfig, isSubquery, isTable, isArrayMainTable);
    }

    @Override
    public SQLConfig newSQLConfig(RequestMethod method, String table, String alias, JSONObject request, List<Join> joinList, boolean isProcedure) throws Exception {
        if (request != null && method == RequestMethod.POST) {
            request.put(CommonConstant.CREATE_TIME, LocalDateTime.now());
            request.put(CommonConstant.CREATE_BY, StpUtil.getLoginIdAsString());
        }
        if(request != null && method == RequestMethod.PUT){
            request.put(CommonConstant.UPDATE_TIME, LocalDateTime.now());
            request.put(CommonConstant.UPDATE_BY, StpUtil.getLoginIdAsString());
        }

        if(request != null && method == RequestMethod.GET){
            // 处理数据权限
            handleDataRule(request);
        }

        return OnlineSQLConfig.newSQLConfig(method,table,alias,request,joinList,isProcedure);
    }

    /**
     * 处理数据权限
     */
    private void handleDataRule(JSONObject request){
        // 获取当前请求的HttpServletRequest
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest httpRequest = attributes.getRequest();
            // 获取当前请求的URL地址
            String currentUrl ="%s%s".formatted(CommonConstant.ONLINE_PREFIX_API,httpRequest.getRequestURI());
            //通过当前url 匹配数据权限
            ISysDataRuleApi sysDataRuleApi = SpringBeanUtils.getBean(ISysDataRuleApi.class);
            List<SysDataRuleApiDTO> data = sysDataRuleApi.queryDataRuleByApiPath(currentUrl).getData();

            // 处理数据权限
            for (SysDataRuleApiDTO dataRule : data) {
                String ruleColumn = dataRule.getRuleColumn();
                // 要根据这个条件去实现 待处理
                //String ruleConditions = dataRule.getRuleConditions();
                String ruleValue = dataRule.getRuleValue();
                request.put(ruleColumn,ruleValue);
            }
        }

    }
}
