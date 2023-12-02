package com.quick.online.parser;

import apijson.RequestMethod;
import apijson.framework.APIJSONVerifier;
import apijson.orm.AbstractParser;
import apijson.orm.AbstractVerifier;
import apijson.orm.SQLConfig;
import apijson.orm.SQLCreator;
import com.alibaba.fastjson.JSONObject;
import com.quick.common.util.SuperAdminUtils;

import java.util.Map;

/**
 * 权限校验器
 */
public class OnlineVerifier extends APIJSONVerifier<String> {
    public static final String TAG = "OnlineVerifier";


    @Override
    public boolean verifyAccess(SQLConfig config) throws Exception {
        // 启动加载
        if (config.getMethod() == RequestMethod.GET
                && (config.getTable().equals("Script") || config.getTable().equals("Function")
                || config.getTable().equals("Access") || config.getTable().equals("Request")
                || config.getTable().equals("Document"))) {
            return true;
        }
        return super.verifyAccess(config);
    }

    @Override
    public void verifyLogin() throws Exception {

    }

    @Override
    public void verifyAdmin() throws Exception {

    }

    /**
     * 批量更新
     * @param method
     * @param name
     * @param target
     * @param request
     * @param maxUpdateCount  此处 必须要重写 否则批量更新不了
     * @param database
     * @param schema
     * @param creator
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject verifyRequest(RequestMethod method, String name, JSONObject target, JSONObject request, int maxUpdateCount, String database, String schema, SQLCreator creator) throws Exception {
        return super.verifyRequest(method, name, target, request, AbstractParser.MAX_UPDATE_COUNT, database, schema, creator);
    }
}
