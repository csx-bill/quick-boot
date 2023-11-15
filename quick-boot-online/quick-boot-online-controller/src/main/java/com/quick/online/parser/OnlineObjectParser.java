package com.quick.online.parser;

import apijson.RequestMethod;
import apijson.orm.AbstractObjectParser;
import apijson.orm.Join;
import apijson.orm.SQLConfig;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.quick.online.config.OnlineSQLConfig;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 统一处理 创建人 创建时间 更新人 更新时间 自动填充
 */
public class OnlineObjectParser extends AbstractObjectParser {

    public OnlineObjectParser(JSONObject request, String parentPath, SQLConfig arrayConfig, boolean isSubquery, boolean isTable, boolean isArrayMainTable) throws Exception {
        super(request, parentPath, arrayConfig, isSubquery, isTable, isArrayMainTable);
    }

    @Override
    public SQLConfig newSQLConfig(RequestMethod method, String table, String alias, JSONObject request, List<Join> joinList, boolean isProcedure) throws Exception {
        if (request != null && method == RequestMethod.POST) {
            request.put("create_time", LocalDateTime.now());
            request.put("create_by", StpUtil.getLoginIdAsString());
        }
        return OnlineSQLConfig.newSQLConfig(method,table,alias,request,joinList,isProcedure);
    }
}
