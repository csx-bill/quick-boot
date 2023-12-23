package com.quick.online.util;

import cn.hutool.core.util.StrUtil;
import com.quick.common.constant.CommonConstant;

/**
 * APIJSON 格式转换 工具类
 */
public class FormatToAPIJSONUtils {

    /**
     *
     * @param aliasTableName 表别名
     * @param dbFieldName 字段名
     * @return
     */
    public static String getKey(String aliasTableName,String dbFieldName,String queryType){
        String formattedFieldName = "%s:rows[].%s.%s";
        // 此处转小驼峰，全局使用了小驼峰 风格
        String camelCaseFieldName = StrUtil.toCamelCase(dbFieldName);
        String key = String.format(formattedFieldName, aliasTableName, aliasTableName, camelCaseFieldName);
        // 根据 queryType 查询方式构建
        if (CommonConstant.IN.equals(queryType)){
            key = key+CommonConstant.APIJSON_IN;
        }
        if (CommonConstant.LIKE.equals(queryType)){
            key = key+CommonConstant.APIJSON_LIKE;
        }
        if(CommonConstant.BETWEEN.equals(queryType)){
            key = key+CommonConstant.APIJSON_BETWEEN;
        }
        return key;
    }
}
