package com.quick.flow.engine.service;

import com.quick.flow.engine.common.FlowElementType;
import com.quick.flow.engine.dao.*;
import com.quick.flow.engine.entity.*;
import com.quick.flow.engine.model.FlowElement;
import com.quick.flow.engine.util.FlowModelUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InstanceDataService {

    @Resource
    private InstanceDataDAO instanceDataDAO;

    @Resource
    private ProcessInstanceDAO processInstanceDAO;

    @Resource
    private IFlowDeploymentService flowDeploymentService;

    @Resource
    private NodeInstanceDAO nodeInstanceDAO;

    @Resource
    private FlowInstanceMappingDAO flowInstanceMappingDAO;

    @Resource
    private FlowInstanceService flowInstanceService;

    public FlowInstanceData select(String flowInstanceId, String instanceDataId, boolean effectiveForSubFlowInstance) {
        FlowInstanceData flowInstanceData = instanceDataDAO.select(flowInstanceId, instanceDataId);
        if (!effectiveForSubFlowInstance) {
            return flowInstanceData;
        }
        if (flowInstanceData != null) {
            return flowInstanceData;
        }
        String subFlowInstanceId = flowInstanceService.getFlowInstanceIdByRootFlowInstanceIdAndInstanceDataId(flowInstanceId, instanceDataId);
        return instanceDataDAO.select(subFlowInstanceId, instanceDataId);
    }

    public FlowInstanceData select(String flowInstanceId, boolean effectiveForSubFlowInstance) {
        FlowInstanceData flowInstanceData = instanceDataDAO.selectRecentOne(flowInstanceId);
        if (!effectiveForSubFlowInstance) {
            return flowInstanceData;
        }
        FlowInstance flowInstance = processInstanceDAO.selectByFlowInstanceId(flowInstanceId);
        FlowDeployment flowDeployment = flowDeploymentService.selectByDeployId(flowInstance.getFlowDeployId());
        Map<String, FlowElement> flowElementMap = FlowModelUtil.getFlowElementMap(flowDeployment.getFlowModel());

        FlowNodeInstance flowNodeInstance = nodeInstanceDAO.selectRecentOne(flowInstanceId);
        int elementType = FlowModelUtil.getElementType(flowNodeInstance.getNodeKey(), flowElementMap);
        if (elementType != FlowElementType.CALL_ACTIVITY) {
            return instanceDataDAO.selectRecentOne(flowInstanceId);
        } else {
            FlowInstanceMapping flowInstanceMapping = flowInstanceMappingDAO.selectFlowInstanceMappingPO(flowInstanceId, flowNodeInstance.getNodeInstanceId());
            return select(flowInstanceMapping.getSubFlowInstanceId(), true);
        }
    }
}
