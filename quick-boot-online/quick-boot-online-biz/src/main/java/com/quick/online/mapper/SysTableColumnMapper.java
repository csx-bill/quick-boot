package com.quick.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.online.entity.SysTableColumn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysTableColumnMapper extends BaseMapper<SysTableColumn> {
    List<SysTableColumn> selectByTableName(@Param("tableName") String tableName);

}
