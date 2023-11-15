package com.quick.online.util;

import com.quick.online.entity.Request;
import com.quick.online.entity.SysTableColumn;

import java.time.LocalDateTime;
import java.util.List;

/**
 * APIJSON 接口构建工具类
 */
public class APIJSONGeneratorUtils {

    /**
     * 保存接口
     */
    public static Request save(String aliasTableName, List<SysTableColumn> fieldList){
        Request request = Request.builder()
                .tag(aliasTableName)
                .structure("{}")
                .method("POST")
                .detail("新增")
                .date(LocalDateTime.now()).build();
        return request;
    }

    /**
     * 更新接口
     */
    public static Request updateById(String aliasTableName, List<SysTableColumn> fieldList){
        Request request = Request.builder()
                .tag(aliasTableName)
                .structure("{\"NECESSARY\": \"id\"}")
                .method("PUT")
                .detail("根据ID更新")
                .date(LocalDateTime.now()).build();
        return request;
    }


    /**
     * 分页接口
     */
    public static Request page(String aliasTableName, List<SysTableColumn> fieldList){
        return null;
    }

    /**
     * 删除接口
     */
    public static Request deleteById(String aliasTableName){
        Request request = Request.builder()
                .tag(aliasTableName)
                .structure("{\"NECESSARY\": \"id\"}")
                .method("DELETE")
                .detail("根据ID删除")
                .date(LocalDateTime.now()).build();
        return request;
    }

    /**
     * 批量删除接口
     */
    public static Request batchDeleteById(String aliasTableName){
        Request request = Request.builder()
                .tag(aliasTableName)
                .structure("{\"NECESSARY\": \"id{}\"}")
                .method("DELETE")
                .detail("批量ID删除")
                .date(LocalDateTime.now()).build();
        return request;
    }

}
