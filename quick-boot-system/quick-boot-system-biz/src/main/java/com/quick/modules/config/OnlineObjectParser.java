package com.quick.modules.config;

import apijson.orm.AbstractObjectParser;
import com.alibaba.fastjson.JSONObject;

import java.util.List;


import apijson.NotNull;
import apijson.RequestMethod;
import apijson.orm.Join;
import apijson.orm.SQLConfig;
import jakarta.servlet.http.HttpSession;
/**对象解析器，用来简化 Parser
 * @author Lemon
 */
public class OnlineObjectParser extends AbstractObjectParser {

    public OnlineObjectParser(HttpSession session, @NotNull JSONObject request, String parentPath, SQLConfig arrayConfig
            , boolean isSubquery, boolean isTable, boolean isArrayMainTable) throws Exception {
        super(request, parentPath, arrayConfig, isSubquery, isTable, isArrayMainTable);
    }

    @Override
    public SQLConfig newSQLConfig(RequestMethod method, String table, String alias, JSONObject request, List<Join> joinList, boolean isProcedure) throws Exception {
        return OnlineSQLConfig.newSQLConfig(method, table, alias, request, joinList, isProcedure);
    }
}
