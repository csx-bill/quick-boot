package com.quick.online.util;

import com.alibaba.fastjson2.JSON;
import com.quick.common.constant.CommonConstant;
import com.quick.online.entity.Document;
import com.quick.online.entity.SysTableColumn;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * APIJSON 接口文档 构建工具类
 */
public class APIJSONDocumentUtils {

    /**
     * 保存接口
     */
    public static Document save(String aliasTableName, List<SysTableColumn> fieldList){
        Map<String, String> columnValues = new HashMap<>();
        for (SysTableColumn sysTableColumn : fieldList) {
            if(!shouldIgnoreField(sysTableColumn) && !CommonConstant.Y.equals(sysTableColumn.getDbIsKey())){
                columnValues.put(sysTableColumn.getDbFieldName(),sysTableColumn.getDbFieldTxt());
            }
        }
        String requestAndApijson = """
                {
                    "%s":%s
                }
                """.formatted(aliasTableName, JSON.toJSON(columnValues));
        return builderDocument(CommonConstant.SAVE_MSG,CommonConstant.POST,aliasTableName,CommonConstant.SAVE, requestAndApijson, requestAndApijson);
    }

    /**
     * ID 查询接口
     */
    public static Document getById(String aliasTableName, List<SysTableColumn> fieldList){
        String requestAndApijson = """
                {
                    "%s":{
                        "id":"id"
                    }
                }
                """.formatted(aliasTableName);
        return builderDocument(CommonConstant.GET_BY_ID_MSG,CommonConstant.GET,aliasTableName,CommonConstant.GET_BY_ID, requestAndApijson, requestAndApijson);
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

        return builderDocument(CommonConstant.UPDATE_BY_ID_MSG,CommonConstant.PUT,aliasTableName,CommonConstant.UPDATE_BY_ID, requestAndApijson, requestAndApijson);
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

        return builderDocument(CommonConstant.UPDATE_BATCH_BY_ID_MSG,CommonConstant.PUT,aliasTableName,CommonConstant.UPDATE_BATCH_BY_ID, requestAndApijson, requestAndApijson);
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
        String requestAndApijson = """
                {
                    "%s[]":{
                        "%s":%s,
                        "page":0,
                        "count":10
                    },
                    "total@":"/%s[]/total",
                    "format":true
                }
                """.formatted(aliasTableName,aliasTableName,JSON.toJSON(columnValues),aliasTableName);

        return builderDocument(CommonConstant.PAGE_MSG,CommonConstant.GET,aliasTableName,CommonConstant.PAGE, requestAndApijson, requestAndApijson);
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

        return builderDocument(CommonConstant.REMOVE_BY_ID_MSG,CommonConstant.DELETE,aliasTableName,CommonConstant.REMOVE_BY_ID, requestAndApijson, requestAndApijson);
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

        return builderDocument(CommonConstant.REMOVE_BATCH_BY_IDS_MSG,CommonConstant.DELETE,aliasTableName,CommonConstant.REMOVE_BATCH_BY_IDS, requestAndApijson, requestAndApijson);
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
   private static Document builderDocument(String name,String method,String aliasTableName,String tag,String request,String apijson){
       Document document = Document.builder()
               .debug(0)
               .userId("0")
               .testAccountId("0")
               .version(0)
               .name(name)
               .type("JSON")
               .url("/%s/%s%s".formatted(method,aliasTableName,tag))
               .request(request)
               .apijson(apijson)
               .date(LocalDateTime.now()).build();
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


}
