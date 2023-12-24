package com.quick.online.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.quick.common.constant.CommonConstant;
import com.quick.online.entity.Document;
import com.quick.online.entity.SysTableColumn;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * APIJSON 接口文档 构建工具类
 */
public class APIJSONDocumentUtils {

    /**
     * 保存接口
     */
    public static Document save(String aliasTableName, List<SysTableColumn> fieldList){
        String apijson = """
                {
                    "%s":{
                
                    },
                    "format":true
                }
                """.formatted(aliasTableName);
        JSONObject apijsonObj = JSON.parseObject(apijson);

        Map<String, String> columnValues = new HashMap<>();
        fieldList.forEach(field->{
            if(!shouldIgnoreField(field) && !CommonConstant.Y.equals(field.getDbIsKey())){
                String formKey = FormatToAPIJSONUtils.formKey(aliasTableName,field.getDbFieldName());
                columnValues.put(formKey,field.getDbFieldTxt());
                apijsonObj.put(StrUtil.toCamelCase(field.getDbFieldName())+"@",formKey);
            }
        });

        // 前端请求参数格式
        String request = JSON.toJSONString(columnValues);

        return builderDocument(CommonConstant.SAVE_MSG,CommonConstant.POST,aliasTableName,CommonConstant.SAVE, request, apijsonObj.toString(),CommonConstant.ADD);
    }

    /**
     * ID 查询接口
     */
    public static Document getById(String aliasTableName, List<SysTableColumn> fieldList){
        // 前端 请求接口格式
        String request = """
                {
                    "%s:data.id":"1"
                }
                """.formatted(aliasTableName);
        // 内部接口 格式
        String apijson = """
                {
                    "id@":"%s:data.id",
                    "%s:data":{
                
                    },
                    "format":true
                }
                """.formatted(aliasTableName,aliasTableName);
        return builderDocument(CommonConstant.GET_BY_ID_MSG,CommonConstant.GET,aliasTableName,CommonConstant.GET_BY_ID, request, apijson,null);
    }

    /**
     * 更新接口
     */
    public static Document updateById(String aliasTableName, List<SysTableColumn> fieldList){

        Map<String, String> columnValues = new HashMap<>();
        for (SysTableColumn sysTableColumn : fieldList) {
            if(!shouldIgnoreField(sysTableColumn)){
                columnValues.put(sysTableColumn.getDbFieldName(),sysTableColumn.getDbFieldTxt());
            }
        }
        String requestAndApijson = """
                {
                    "%s":%s
                }
                """.formatted(aliasTableName, JSON.toJSON(columnValues));

        return builderDocument(CommonConstant.UPDATE_BY_ID_MSG,CommonConstant.PUT,aliasTableName,CommonConstant.UPDATE_BY_ID, requestAndApijson, requestAndApijson,CommonConstant.UPDATE);
    }

    /**
     * 批量更新接口
     */
    public static Document updateBatchById(String aliasTableName, List<SysTableColumn> fieldList){
        Map<String, Object> columnValues = new HashMap<>();
        for (SysTableColumn sysTableColumn : fieldList) {
            if(!shouldIgnoreField(sysTableColumn) && !CommonConstant.Y.equals(sysTableColumn.getDbIsKey())){
                columnValues.put(sysTableColumn.getDbFieldName(),sysTableColumn.getDbFieldTxt());
            }
        }
        columnValues.put("id{}",new ArrayList<>());
        String requestAndApijson = """
                {
                    "%s":%s
                }
                """.formatted(aliasTableName,JSON.toJSON(columnValues));

        return builderDocument(CommonConstant.UPDATE_BATCH_BY_ID_MSG,CommonConstant.PUT,aliasTableName,CommonConstant.UPDATE_BATCH_BY_ID, requestAndApijson, requestAndApijson,CommonConstant.BATCHUPDATE);
    }


    /**
     * 分页接口
     */
    public static Document page(String aliasTableName, List<SysTableColumn> fieldList){
        Map<String, Object> columnValues = new HashMap<>();
        for (SysTableColumn sysTableColumn : fieldList) {
            if(!CommonConstant.DEL_FLAG.equals(sysTableColumn.getDbFieldName()) && !CommonConstant.Y.equals(sysTableColumn.getDbIsKey())){
                columnValues.put(sysTableColumn.getDbFieldName(),sysTableColumn.getDbFieldTxt());
            }
        }
        // 过滤出需要解析字典的字段
        List<SysTableColumn> dictFieldList = fieldList.stream().filter(field -> StringUtils.hasText(field.getDictCode())).collect(Collectors.toList());
        // 字典字段
        JSONObject dictObj = new JSONObject();
        dictFieldList.forEach(field -> {
            String key = field.getDbFieldName() + "()";
            String value = String.format("translateDict(%s, %s)", field.getDictCode(), field.getDbFieldName());
            dictObj.put(key, value);
        });


        // 接口 (前端请求) 请求参数格式
        String request = """
                {
                     "%s:rows[].page":0,
                     "%s:rows[].count":10
                }
                """.formatted(aliasTableName,aliasTableName);
        // request 转成 apijson  内部请求格式
        String apijson = """
                {
                    "page@": "%s:rows[].page",
                    "count@": "%s:rows[].count",
                    "%s:rows[]": {
                        "query": 2,
                        "%s": %s
                    },
                    "total@": "/%s:rows[]/total",
                    "format": true
                }
                """.formatted(aliasTableName,aliasTableName,aliasTableName,aliasTableName,JSON.toJSON(dictObj),aliasTableName);

        // 过滤需要查询的字段
        List<SysTableColumn> queryFieldList = fieldList.stream().filter(field -> !field.getIsQuery().equals(CommonConstant.Y)).collect(Collectors.toList());
        // 构建请求 查询字段
        JSONObject requestObj = JSON.parseObject(request);
        // 构建请求 查询字段映射
        JSONObject apijsonObj = JSON.parseObject(apijson);

        queryFieldList.forEach(field -> {
            // 此处转小驼峰，全局使用了小驼峰 风格
            String camelCaseFieldName = StrUtil.toCamelCase(field.getDbFieldName());
            String key = FormatToAPIJSONUtils.getKey(aliasTableName, field.getDbFieldName(), field.getQueryType());

            requestObj.put(key, field.getDbFieldTxt());
            apijsonObj.put(camelCaseFieldName + "@", key);
        });

        return builderDocument(CommonConstant.PAGE_MSG,CommonConstant.GET,aliasTableName,CommonConstant.PAGE, requestObj.toString(), apijsonObj.toString(),null);
    }

    /**
     * 删除接口
     */
    public static Document removeById(String aliasTableName){
        String requestAndApijson = """
                {
                    "%s":{
                    	"id": ""
                    }
                }
                """.formatted(aliasTableName);

        return builderDocument(CommonConstant.REMOVE_BY_ID_MSG,CommonConstant.DELETE,aliasTableName,CommonConstant.REMOVE_BY_ID, requestAndApijson, requestAndApijson,CommonConstant.DELETE);
    }

    /**
     * 批量删除接口
     */
    public static Document removeBatchByIds(String aliasTableName){
        String requestAndApijson = """
                {
                    "%s":{
                    	"id{}": []
                    }
                }
                """.formatted(aliasTableName);

        return builderDocument(CommonConstant.REMOVE_BATCH_BY_IDS_MSG,CommonConstant.DELETE,aliasTableName,CommonConstant.REMOVE_BATCH_BY_IDS, requestAndApijson, requestAndApijson,CommonConstant.BATCHDELETE);
    }

    /**
     * 构建接口文档
     * @param name
     * @param method
     * @param aliasTableName
     * @param tag
     * @param request
     * @param apijson
     * @return
     */
   private static Document builderDocument(String name,String method,String aliasTableName,String tag,String request,String apijson,String permission){
       Document document = Document.builder()
               .debug(0)
               .userId("0")
               .testAccountId("0")
               .version(0)
               .name(name)
               .type("JSON")
               .url(FormatToAPIJSONUtils.getUrl(aliasTableName,method,tag))
               .request(request)
               .apijson(apijson)
               .date(LocalDateTime.now())
               .permission(builderCRUDPermissions(aliasTableName,permission))
               .build();
       return document;
    }


    /**
     * 构建 CRUD
     * @param aliasTableName
     * @param fieldList
     * @return
     */
    public static List<Document> builderCRUDDocument(String aliasTableName, List<SysTableColumn> fieldList){
        ArrayList<Document> crud = new ArrayList<>();

        crud.add(save(aliasTableName, fieldList));
        crud.add(updateById(aliasTableName, fieldList));
        crud.add(updateBatchById(aliasTableName, fieldList));
        crud.add(page(aliasTableName, fieldList));
        crud.add(removeById(aliasTableName));
        crud.add(removeBatchByIds(aliasTableName));
        crud.add(getById(aliasTableName,fieldList));

        return crud;
    }


    /**
     * 忽略字段
     * @param fieldDetail
     * @return
     */
    private static boolean shouldIgnoreField(SysTableColumn fieldDetail) {
        String dbFieldName = fieldDetail.getDbFieldName();
        return CommonConstant.DEL_FLAG.equals(dbFieldName)
                || CommonConstant.CREATE_TIME.equals(dbFieldName)
                || CommonConstant.CREATE_BY.equals(dbFieldName)
                || CommonConstant.UPDATE_TIME.equals(dbFieldName)
                || CommonConstant.UPDATE_BY.equals(dbFieldName);
    }

    /**
     * 构建接口权限
     * @return 格式 online:sys_user:add
     */
    private static String builderCRUDPermissions(String aliasTableName,String permission){
        if(StringUtils.hasText(permission)){
            return "online:%s:%s".formatted(StrUtil.toUnderlineCase(aliasTableName),permission);
        }
        return null;
    }

}
