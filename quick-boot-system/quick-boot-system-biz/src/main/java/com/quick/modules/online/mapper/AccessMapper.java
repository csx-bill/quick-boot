package com.quick.modules.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.quick.modules.online.entity.Access;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface AccessMapper extends BaseMapper<Access> {

    @MapKey("tableName")
    List<Map<String,String>> listByTableName(@Param("tableNames") List<String> tableNames,@Param("tableName")String tableName);

}
