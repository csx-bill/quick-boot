package com.quick.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.flow.entity.FlowInstanceMapping;

import java.util.List;

public interface IFlowInstanceMappingService extends IService<FlowInstanceMapping> {

    List<FlowInstanceMapping> selectFlowInstanceMappingList(String flowInstanceId, String nodeInstanceId);

    FlowInstanceMapping selectFlowInstanceMapping(String flowInstanceId, String nodeInstanceId);
    boolean updateStatus(String flowInstanceId, String nodeInstanceId,Integer status);


}
