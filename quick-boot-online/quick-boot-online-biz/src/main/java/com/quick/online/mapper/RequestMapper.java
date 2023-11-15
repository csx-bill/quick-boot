package com.quick.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.online.entity.Request;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RequestMapper extends BaseMapper<Request> {
}
