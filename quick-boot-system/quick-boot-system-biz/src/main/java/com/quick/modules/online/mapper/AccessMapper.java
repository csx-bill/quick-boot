package com.quick.modules.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.quick.modules.online.entity.Access;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface AccessMapper extends BaseMapper<Access> {
    /**
     * 使用原生SQL获取字段的详细信息
     *
     * @param tableName 表名
     * @return 字段的详细信息
     */
    @Select(value = "SHOW full fields FROM ${tableName}")
    List<Map<String, Object>> getFieldDetails(@Param("tableName") String tableName);

    @MapKey("tableName")
    List<Map<String,String>> listByTableName(@Param("tableNames") List<String> tableNames,@Param("tableName")String tableName);

}
