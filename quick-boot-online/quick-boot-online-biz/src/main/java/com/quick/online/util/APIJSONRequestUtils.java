package com.quick.online.util;

import com.quick.common.constant.CommonConstant;
import com.quick.online.entity.Request;
import com.quick.online.entity.SysTableColumn;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;
import java.util.*;


/**
 * APIJSON 接口请求构建工具类
 * 接口参数校验
 * https://github.com/Tencent/APIJSON/blob/master/APIJSONORM/src/main/java/apijson/orm/Operation.java
 */
public class APIJSONRequestUtils {

    /**
     * 保存接口
     */
    public static Request save(String aliasTableName, List<SysTableColumn> fieldList){
        List<String> fields = new ArrayList<>();

        fieldList.forEach(field->{
            if(!shouldIgnoreField(field)
                    && !CommonConstant.Y.equals(field.getDbIsKey())){
                String formKey = FormatToAPIJSONUtils.formKey(aliasTableName,field.getDbFieldName());
                fields.add("!"+formKey);
            }
        });

        fields.add("!");

        String refuse = String.join(",", fields);

        String structure = """
                {
                    "REFUSE": "%s"
                }
                """.formatted(refuse);

        return builderRequest(CommonConstant.SAVE_MSG,RequestMethod.POST.name(),aliasTableName, CommonConstant.SAVE,structure);
    }

    /**
     * ID 查询接口
     */
    public static Request getById(String aliasTableName, List<SysTableColumn> fieldList){
        //  只允许传 id 字段查询
        String structure = """
                {
                    "MUST":"%s:data.id",
                    "REFUSE": "!%s:data.id,!"
                }
                """.formatted(aliasTableName,aliasTableName);
        return builderRequest(CommonConstant.GET_BY_ID_MSG,RequestMethod.GET.name(),aliasTableName, CommonConstant.GET_BY_ID,structure);
    }


    /**
     * 更新接口
     */
    public static Request updateById(String aliasTableName, List<SysTableColumn> fieldList){
        List<String> fields = new ArrayList<>();

        fieldList.forEach(field->{
            if(!shouldIgnoreField(field)){
                String formKey = FormatToAPIJSONUtils.formKey(aliasTableName,field.getDbFieldName());
                fields.add("!"+formKey);
            }
        });

        fields.add("!");

        String refuse = String.join(",", fields);

        String structure = """
                {
                    "MUST":"%s.id",
                    "REFUSE": "%s"
                }
                """.formatted(aliasTableName,refuse);

        return builderRequest(CommonConstant.UPDATE_BY_ID_MSG,RequestMethod.PUT.name(),aliasTableName, CommonConstant.UPDATE_BY_ID,structure);
    }

    /**
     * 批量更新接口
     */
    public static Request updateBatchById(String aliasTableName, List<SysTableColumn> fieldList){
        List<String> fields = new ArrayList<>();

        fieldList.forEach(field->{
            if(!shouldIgnoreField(field)){
                String formKey = FormatToAPIJSONUtils.formKey(aliasTableName,field.getDbFieldName());
                if(CommonConstant.Y.equals(field.getDbIsKey())){
                    formKey = formKey+CommonConstant.APIJSON_IN;
                    fields.add("!"+formKey);
                }else {
                    fields.add("!"+formKey);
                }
            }
        });

        fields.add("!");

        String refuse = String.join(",", fields);

        String structure = """
                {
                   "MUST":"%s.id{}",
                   "REFUSE":"%s"
                }
                """.formatted(aliasTableName,refuse);
        return builderRequest(CommonConstant.UPDATE_BATCH_BY_ID_MSG,RequestMethod.PUT.name(),aliasTableName, CommonConstant.UPDATE_BATCH_BY_ID,structure);
    }


    /**
     * 分页接口
     */
    public static Request page(String aliasTableName, List<SysTableColumn> fieldList){

        List<String> fields = new ArrayList<>();

        fieldList.forEach(field->{
            if(!CommonConstant.DEL_FLAG.equals(field.getDbFieldName())
                    && !CommonConstant.Y.equals(field.getDbIsKey())){
                String key = FormatToAPIJSONUtils.getKey(aliasTableName, field.getDbFieldName(), field.getQueryType());
                fields.add("!"+key);
            }
        });

        fields.add("!");

        String refuse = String.join(",", fields);

        // 必须传  page,count 并且是 NUMBER 类型  structure 为参数校验
        String structure = """
                {
                "MUST":"%s:rows[].page,%s:rows[].count",
                "TYPE":{
                        "%s:rows[].page":"NUMBER",
                        "%s:rows[].count":"NUMBER"
                    },
                "REFUSE":"%s" 
                }
                """.formatted(aliasTableName,aliasTableName,aliasTableName,aliasTableName,refuse);

        return builderRequest(CommonConstant.PAGE_MSG,RequestMethod.GET.name(),aliasTableName, CommonConstant.PAGE,structure);
    }

    /**
     * 删除接口
     */
    public static Request removeById(String aliasTableName){
        String structure = """
                {
                    "MUST":"%s.id",
                    "REFUSE":"!%s.id,!"
                }
                """.formatted(aliasTableName,aliasTableName);
        return builderRequest(CommonConstant.REMOVE_BY_ID_MSG,RequestMethod.DELETE.name(),aliasTableName, CommonConstant.REMOVE_BY_ID,structure);
    }

    /**
     * 批量删除接口
     */
    public static Request removeBatchByIds(String aliasTableName){
        String structure = """
                {
                    "MUST":"%s.id{}",
                    "REFUSE":"!%s.id{},!"
                }
                """.formatted(aliasTableName,aliasTableName);
        return builderRequest(CommonConstant.REMOVE_BATCH_BY_IDS_MSG,RequestMethod.DELETE.name(),aliasTableName, CommonConstant.REMOVE_BATCH_BY_IDS,structure);
    }

    /**
     * 构建请求
     * @param detail
     * @param method
     * @param aliasTableName
     * @param tag
     * @param structure
     * @return
     */
    private static Request builderRequest(String detail, String method, String aliasTableName, String tag, String structure){
        Request request = Request.builder()
                .tag(FormatToAPIJSONUtils.getTag(aliasTableName,tag))
                .structure(structure)
                .method(method)
                .detail(detail)
                .date(LocalDateTime.now()).build();
        return request;
    }

    /**
     * 构建 CRUD
     * @param aliasTableName
     * @param fieldList
     * @return
     */
    public static List<Request> builderCRUDRequest(String aliasTableName, List<SysTableColumn> fieldList){
        ArrayList<Request> crud = new ArrayList<>();

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
