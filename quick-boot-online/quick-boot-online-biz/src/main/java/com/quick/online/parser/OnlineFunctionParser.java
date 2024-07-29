package com.quick.online.parser;

import apijson.NotNull;
import apijson.RequestMethod;
import apijson.framework.APIJSONFunctionParser;
import com.alibaba.fastjson.JSONObject;
import com.quick.common.constant.CommonConstant;
import com.quick.common.util.SpringBeanUtils;
import com.quick.common.vo.Result;
import com.quick.system.api.ISysDictApi;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**可远程调用的函数类，用于自定义业务逻辑处理
 * 具体见 https://github.com/Tencent/APIJSON/issues/101
 */
@Slf4j
public class OnlineFunctionParser extends APIJSONFunctionParser {
    public static final String TAG = "OnlineFunctionParser";


    public OnlineFunctionParser() {
        this(null, null, 0, null, null);
    }

    public OnlineFunctionParser(RequestMethod method, String tag, int version, JSONObject curObj, HttpSession session) {
        super(method, tag, version, curObj, session);
    }

    /**
     * 通过字典code获取字典数据 Text
     * @param dictCode
     * @param dictValue
     * @return
     */
    /**
     *
     * @param current 当前的
     * @param dictCode 字典code
     * @param dictValueKey 字典值Key(字段名)
     */
    public String translateDict(@NotNull JSONObject current,@NotNull String dictCode, String dictValueKey) {
        try {
            ISysDictApi sysDictApi = SpringBeanUtils.getBean(ISysDictApi.class);
            String dictValue = current.getString(dictValueKey);
            if(StringUtils.hasText(dictValue)){
                Result<String> result = sysDictApi.translateDict(dictCode, dictValue);
                String textValue = result.getData();
                current.put(dictValueKey + CommonConstant.DICT_TEXT_SUFFIX, textValue);
            }
            // 此处如果不返回  current.getString(dictValueKey)   dictValueKey 的字段会丢失
            return current.getString(dictValueKey);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }


}
