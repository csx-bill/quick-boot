package com.quick.flow.engine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.flow.engine.entity.FlowNodeInstance;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface FlowNodeInstanceMapper extends BaseMapper<FlowNodeInstance> {

    @Select("SELECT * FROM flow_node_instance WHERE node_instance_id=#{nodeInstanceId}")
    FlowNodeInstance selectByNodeInstanceId(@Param("flowInstanceId") String flowInstanceId,
                                            @Param("nodeInstanceId") String nodeInstanceId);

    @Select("SELECT * FROM flow_node_instance WHERE flow_instance_id=#{flowInstanceId} ORDER BY id DESC LIMIT 1")
    FlowNodeInstance selectRecentOne(@Param("flowInstanceId") String flowInstanceId);

    @Select("SELECT * FROM flow_node_instance WHERE flow_instance_id=#{flowInstanceId} ORDER BY id")
    List<FlowNodeInstance> selectByFlowInstanceId(@Param("flowInstanceId") String flowInstanceId);

    @Select("SELECT * FROM flow_node_instance WHERE flow_instance_id=#{flowInstanceId} ORDER BY id DESC")
    List<FlowNodeInstance> selectDescByFlowInstanceId(@Param("flowInstanceId") String flowInstanceId);

    @Select("SELECT * FROM flow_node_instance WHERE flow_instance_id=#{flowInstanceId} and status=#{status} ORDER BY id" +
            " DESC LIMIT 1")
    FlowNodeInstance selectRecentOneByStatus(@Param("flowInstanceId") String flowInstanceId, @Param("status") int status);

    @Select("SELECT * FROM flow_node_instance WHERE flow_instance_id=#{flowInstanceId} AND node_key=#{nodeKey} AND " +
            "source_node_instance_id=#{sourceNodeInstanceId}")
    FlowNodeInstance selectBySourceInstanceId(@Param("flowInstanceId") String flowInstanceId,
                                              @Param("sourceNodeInstanceId") String sourceNodeInstanceId,
                                              @Param("nodeKey") String nodeKey);

    @Update("UPDATE flow_node_instance SET status=#{status}, update_time= #{updateTime} " +
            "WHERE node_instance_id=#{nodeInstanceId}")
    void updateStatus(FlowNodeInstance entity);


}
