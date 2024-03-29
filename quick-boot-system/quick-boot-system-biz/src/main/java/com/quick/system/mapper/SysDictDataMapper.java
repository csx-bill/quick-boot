package com.quick.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.system.entity.SysDictData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysDictDataMapper extends BaseMapper<SysDictData> {
    List<SysDictData> queryDictDataByDictCode(@Param("dictCode") String dictCode);

}