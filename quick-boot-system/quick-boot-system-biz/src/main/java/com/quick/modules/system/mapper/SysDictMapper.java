package com.quick.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.modules.system.entity.SysDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {
    /**
     * 通过字典code获取字典数据
     *
     * @param code
     * @param key
     * @return
     */
    @Select(value = "select s.dict_text from sys_dict_data s " +
            " where s.dict_id = (select id from sys_dict where dict_code = #{dictCode})" +
            "  and s.dict_value = #{dictValue}")
    String queryDictTextByKey(@Param("dictCode") String dictCode, @Param("dictValue") String dictValue);
}