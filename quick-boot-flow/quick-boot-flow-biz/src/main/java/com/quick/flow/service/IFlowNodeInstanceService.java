package com.quick.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.flow.entity.FlowNodeInstance;

import java.util.List;

public interface IFlowNodeInstanceService extends IService<FlowNodeInstance> {

    List<FlowNodeInstance> selectDescByFlowInstanceId(String flowInstanceId);

    List<FlowNodeInstance> selectByFlowInstanceId(String flowInstanceId);

    FlowNodeInstance selectByNodeInstanceId(String nodeInstanceId);

    FlowNodeInstance selectBySourceInstanceId(String flowInstanceId, String sourceNodeInstanceId, String nodeKey);

    FlowNodeInstance selectRecentOne(String flowInstanceId);

}
