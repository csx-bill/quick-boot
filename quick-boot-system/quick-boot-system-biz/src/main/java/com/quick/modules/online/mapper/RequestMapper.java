package com.quick.modules.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.modules.online.entity.Request;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RequestMapper extends BaseMapper<Request> {
}
