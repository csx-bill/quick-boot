package com.quick.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.flow.entity.FlowInstanceData;

public interface IFlowInstanceDataService extends IService<FlowInstanceData> {

    FlowInstanceData selectByFlowInstanceIdAndInstanceDataId(String flowInstanceId,
                        String instanceDataId);

    FlowInstanceData selectByflowInstanceIdRecentOne(String flowInstanceId);

}
