package com.quick.flow.engine.service;

import com.quick.flow.engine.common.FlowElementType;
import com.quick.flow.engine.model.FlowElement;
import com.quick.flow.engine.util.FlowModelUtil;
import com.quick.flow.entity.*;
import com.quick.flow.service.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InstanceDataService {

    @Resource
    private IFlowInstanceDataService flowInstanceDataService;


    @Resource
    private IFlowInstanceService flowInstanceService;

    @Resource
    private IFlowDeploymentService flowDeploymentService;

    @Resource
    private IFlowNodeInstanceService flowNodeInstanceService;

    @Resource
    private IFlowInstanceMappingService flowInstanceMappingService;

    @Resource
    private FlowInstanceServiceBiz flowInstanceServiceBiz;

    public FlowInstanceData select(String flowInstanceId, String instanceDataId, boolean effectiveForSubFlowInstance) {
        FlowInstanceData instanceDataPO = flowInstanceDataService.selectByFlowInstanceIdAndInstanceDataId(flowInstanceId, instanceDataId);
        if (!effectiveForSubFlowInstance) {
            return instanceDataPO;
        }
        if (instanceDataPO != null) {
            return instanceDataPO;
        }
        String subFlowInstanceId = flowInstanceServiceBiz.getFlowInstanceIdByRootFlowInstanceIdAndInstanceDataId(flowInstanceId, instanceDataId);
        return flowInstanceDataService.selectByFlowInstanceIdAndInstanceDataId(subFlowInstanceId, instanceDataId);
    }

    public FlowInstanceData select(String flowInstanceId, boolean effectiveForSubFlowInstance) {
        FlowInstanceData instanceDataPO = flowInstanceDataService.selectByflowInstanceIdRecentOne(flowInstanceId);
        if (!effectiveForSubFlowInstance) {
            return instanceDataPO;
        }
        FlowInstance flowInstancePO = flowInstanceService.selectByFlowInstanceId(flowInstanceId);
        FlowDeployment flowDeployment = flowDeploymentService.selectByFlowDeployId(flowInstancePO.getFlowDeployId());
        Map<String, FlowElement> flowElementMap = FlowModelUtil.getFlowElementMap(flowDeployment.getFlowModel());

        FlowNodeInstance nodeInstancePO = flowNodeInstanceService.selectRecentOne(flowInstanceId);
        int elementType = FlowModelUtil.getElementType(nodeInstancePO.getNodeKey(), flowElementMap);
        if (elementType != FlowElementType.CALL_ACTIVITY) {
            return flowInstanceDataService.selectByflowInstanceIdRecentOne(flowInstanceId);
        } else {
            FlowInstanceMapping flowInstanceMappingPO = flowInstanceMappingService.selectFlowInstanceMapping(flowInstanceId, nodeInstancePO.getNodeInstanceId());
            return select(flowInstanceMappingPO.getSubFlowInstanceId(), true);
        }
    }
}
