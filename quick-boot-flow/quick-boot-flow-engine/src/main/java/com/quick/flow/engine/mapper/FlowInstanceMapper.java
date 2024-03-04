package com.quick.flow.engine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.flow.engine.entity.FlowInstance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
@Mapper
public interface FlowInstanceMapper extends BaseMapper<FlowInstance> {

    @Update("UPDATE flow_instance SET status=#{status}, update_time= #{updateTime} " +
        "WHERE flow_instance_id=#{flowInstanceId}")
    void updateStatus(FlowInstance entity);
}
