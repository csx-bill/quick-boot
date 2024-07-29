package com.quick.online.parser;

import apijson.JSONResponse;
import apijson.RequestMethod;
import apijson.framework.APIJSONObjectParser;
import apijson.framework.APIJSONParser;
import apijson.orm.SQLConfig;
import com.alibaba.fastjson.JSONObject;
import com.quick.common.constant.CommonConstant;
import com.quick.common.util.SpringBeanUtils;
import io.micrometer.tracing.Tracer;

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
    @Override
    public JSONObject parseResponse(JSONObject request) {
        JSONObject result = super.parseResponse(request);

        if (result.getIntValue(JSONResponse.KEY_CODE) == JSONResponse.CODE_SUCCESS) {
            result.put(CommonConstant.SUCCESS_KEY,CommonConstant.SUCCESS_CODE);
        } else {
            result.put(CommonConstant.SUCCESS_KEY,result.getIntValue(JSONResponse.KEY_CODE));
        }

        Tracer tracer = SpringBeanUtils.getBean(Tracer.class);
        if(tracer.currentSpan()!=null){
            result.put(CommonConstant.TRACE_ID, tracer.currentSpan().context().traceId());
        }
        return result;
    }
}
