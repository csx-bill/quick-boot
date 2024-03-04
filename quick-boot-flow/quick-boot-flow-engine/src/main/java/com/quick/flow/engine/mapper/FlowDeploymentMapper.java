package com.quick.flow.engine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.flow.engine.entity.FlowDeployment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FlowDeploymentMapper extends BaseMapper<FlowDeployment> {

    @Select("SELECT * FROM flow_deployment WHERE flow_module_id=#{flowModuleId} ORDER BY id DESC LIMIT 1")
    FlowDeployment selectByModuleId(@Param("flowModuleId") String flowModuleId);
}
