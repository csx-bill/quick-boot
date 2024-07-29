package com.quick.online.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.online.entity.OnlCgformField;
import com.quick.online.entity.SysTableColumn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OnlCgformFieldMapper extends BaseMapper<OnlCgformField> {
    @DS("#last")
    @InterceptorIgnore(tenantLine = "1")
    List<OnlCgformField> selectByTableName(@Param("tableName") String tableName,String dsName);

}
