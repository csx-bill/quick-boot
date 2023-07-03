package com.quick.modules.config;
import apijson.orm.AbstractFunctionParser;
import com.alibaba.fastjson.JSONObject;
import apijson.RequestMethod;

/**可远程调用的函数类，用于自定义业务逻辑处理
 * 具体见 https://github.com/Tencent/APIJSON/issues/101
 * @author Lemon
 */
public class OnlineFunctionParser extends AbstractFunctionParser{
    public static final String TAG = "OnlineFunctionParser";


    public OnlineFunctionParser() {
        this(null, null, 0, null);
    }
    public OnlineFunctionParser(RequestMethod method, String tag, int version, JSONObject request) {
        super(method, tag, version, request);
    }
}
