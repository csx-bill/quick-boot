package com.quick.online.parser;

import apijson.RequestMethod;
import apijson.framework.APIJSONObjectParser;
import apijson.framework.APIJSONParser;
import apijson.orm.SQLConfig;
import com.alibaba.fastjson.JSONObject;

public class OnlineParser extends APIJSONParser<String> {
    public OnlineParser() {
        super();
    }

    public OnlineParser(RequestMethod method) {
        super(method);
    }

    public OnlineParser(RequestMethod method, boolean needVerify) {
        super(method, needVerify);
    }

    // 可重写来设置最大查询数量
    @Override
    public int getMaxQueryDepth() {
        return 6;
    }

    @Override
    public APIJSONObjectParser<String> createObjectParser(JSONObject request, String parentPath, SQLConfig arrayConfig, boolean isSubquery, boolean isTable, boolean isArrayMainTable) throws Exception {
        return new OnlineObjectParser(getSession(),request, parentPath, arrayConfig, isSubquery, isTable, isArrayMainTable).setMethod(getMethod()).setParser(this);
    }

    @Override
    public boolean isNeedVerifyLogin() {
        return false;
    }

    /**
     * 参考 https://github.com/Tencent/APIJSON/issues/548
     * 重写响应格式
     * @param request
     * @return
     */
//    @Override
//    public JSONObject parseResponse(JSONObject request) {
//        return super.parseResponse(request);
//    }
}
