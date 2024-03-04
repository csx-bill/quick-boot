package com.quick.flow.engine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.flow.engine.entity.FlowInstanceMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface FlowInstanceMappingMapper extends BaseMapper<FlowInstanceMapping> {

    @Select("SELECT * FROM flow_instance_mapping WHERE flow_instance_id= #{flowInstanceId} and node_instance_id = #{nodeInstanceId}")
    List<FlowInstanceMapping> selectFlowInstanceMappingList(@Param("flowInstanceId") String flowInstanceId, @Param("nodeInstanceId") String nodeInstanceId);

    @Select("SELECT * FROM flow_instance_mapping WHERE flow_instance_id= #{flowInstanceId} and node_instance_id = #{nodeInstanceId}")
    FlowInstanceMapping selectFlowInstanceMapping(@Param("flowInstanceId") String flowInstanceId, @Param("nodeInstanceId") String nodeInstanceId);

    @Update("UPDATE flow_instance_mapping SET type= #{type}, update_time= #{updateTime} WHERE flow_instance_id= #{flowInstanceId} and node_instance_id = #{nodeInstanceId}")
    void updateType(FlowInstanceMapping entity);
}
