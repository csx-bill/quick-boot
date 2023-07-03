package com.quick.modules.config;

import apijson.JSONResponse;
import apijson.RequestMethod;
import apijson.orm.*;
import com.alibaba.fastjson.JSONObject;
import com.quick.common.constant.CommonConstant;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

public class OnlineParser extends AbstractParser<Long> {
    public static final Map<String, HttpSession> KEY_MAP;
    static {
        KEY_MAP = new HashMap<>();
    }
    public OnlineParser() {
        super();
    }
    public OnlineParser(RequestMethod method) {
        super(method);
    }
    public OnlineParser(RequestMethod method, boolean needVerify) {
        super(method, needVerify);
    }

    private HttpSession session;
    public Parser<Long> setSession(HttpSession session) {
        this.session = session;
        return this;
    }
    public HttpSession getSession() {
        return session;
    }


    @Override
    public Parser<Long> createParser() {
        return new OnlineParser();
    }

    @Override
    public ObjectParser createObjectParser(JSONObject request, String parentPath, SQLConfig arrayConfig
            , boolean isSubquery, boolean isTable, boolean isArrayMainTable) throws Exception {
        return new OnlineObjectParser(getSession(), request, parentPath, arrayConfig
                , isSubquery, isTable, isArrayMainTable).setMethod(getMethod()).setParser(this);
    }

    @Override
    public FunctionParser createFunctionParser() {
        return new OnlineFunctionParser();
    }

    @Override
    public SQLConfig createSQLConfig() {
        return new OnlineSQLConfig();
    }

    @Override
    public SQLExecutor createSQLExecutor() {
        return new OnlineSQLExecutor();
    }

    @Override
    public Verifier<Long> createVerifier() {
        return new OnlineVerifier();
    }

    private FunctionParser functionParser;
    public FunctionParser getFunctionParser() {
        return functionParser;
    }
    public Object onFunctionParse(String key, String function, String parentPath, String currentName, JSONObject currentObject, boolean containRaw) throws Exception {
        if (functionParser == null) {
            functionParser = createFunctionParser();
            functionParser.setMethod(getMethod());
            functionParser.setTag(getTag());
            functionParser.setVersion(getVersion());
            functionParser.setRequest(requestObject);
        }
        functionParser.setKey(key);
        functionParser.setParentPath(parentPath);
        functionParser.setCurrentName(currentName);
        functionParser.setCurrentObject(currentObject);

        return functionParser.invoke(function, currentObject, containRaw);
    }

    /**
     * 重写 parseResponse 调整返回格式
     * @param request
     * @return
     */
    @Override
    public JSONObject parseResponse(JSONObject request) {
        JSONObject res = super.parseResponse(request);
        if (res.getIntValue(JSONResponse.KEY_CODE) == JSONResponse.CODE_SUCCESS) { // 或 JSONResponse.isSuccess(res.getIntValue(JSONResponse.KEY_CODE))
            res.put(JSONResponse.KEY_CODE, 0);
            res.put("status", CommonConstant.SUCCESS_CODE);
        }else {
            res.put("status", res.getIntValue(JSONResponse.KEY_CODE));
        }
        return res;
    }
}
