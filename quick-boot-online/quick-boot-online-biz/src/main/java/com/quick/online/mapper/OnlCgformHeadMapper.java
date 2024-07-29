package com.quick.online.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.online.entity.OnlCgformField;
import com.quick.online.entity.OnlCgformHead;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OnlCgformHeadMapper extends BaseMapper<OnlCgformHead> {
    @DS("#last")
    @InterceptorIgnore(tenantLine = "1")
    OnlCgformHead selectByTableName(@Param("tableName") String tableName, String dsName);
}
