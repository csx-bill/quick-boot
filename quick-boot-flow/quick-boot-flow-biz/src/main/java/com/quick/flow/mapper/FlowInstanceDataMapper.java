package com.quick.flow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.flow.entity.FlowInstanceData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FlowInstanceDataMapper extends BaseMapper<FlowInstanceData> {

    @Select("SELECT * FROM ei_instance_data WHERE flow_instance_id=#{flowInstanceId} ORDER BY id DESC LIMIT 1")
    FlowInstanceData selectByflowInstanceIdRecentOne(@Param("flowInstanceId") String flowInstanceId);

}
