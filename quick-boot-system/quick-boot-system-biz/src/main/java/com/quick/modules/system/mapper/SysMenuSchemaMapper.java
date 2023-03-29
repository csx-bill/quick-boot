package com.quick.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.modules.system.entity.SysMenuSchema;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysMenuSchemaMapper extends BaseMapper<SysMenuSchema> {
    SysMenuSchema queryByPath(@Param("path") String path);
}
