package com.quick.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.modules.system.entity.SysDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {
    /**
     * 通过字典code获取字典数据
     *
     * @param dictCode
     * @param dictValue
     * @return
     */
    String queryDictTextByKey(@Param("dictCode") String dictCode, @Param("dictValue") String dictValue);
}