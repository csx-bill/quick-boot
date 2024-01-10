package com.quick.flow.engine.service;

import com.quick.flow.engine.common.FlowElementType;
import com.quick.flow.engine.model.FlowElement;
import com.quick.flow.engine.util.FlowModelUtil;
import com.quick.flow.entity.FlowDeployment;
import com.quick.flow.entity.FlowInstance;
import com.quick.flow.entity.FlowNodeInstance;
import com.quick.flow.service.IFlowDeploymentService;
import com.quick.flow.service.IFlowInstanceService;
import com.quick.flow.service.IFlowNodeInstanceService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NodeInstanceService {


    @Resource
    private IFlowNodeInstanceService flowNodeInstanceService;

    @Resource
    private IFlowInstanceService flowInstanceService;

    @Resource
    private IFlowDeploymentService flowDeploymentService;

    @Resource
    private FlowInstanceServiceBiz flowInstanceServiceBiz;

    public FlowNodeInstance selectByNodeInstanceId(String flowInstanceId, String nodeInstanceId, boolean effectiveForSubFlowInstance) {
        FlowNodeInstance nodeInstancePO = flowNodeInstanceService.selectByNodeInstanceId(nodeInstanceId);
        if (!effectiveForSubFlowInstance) {
            return nodeInstancePO;
        }
        if (nodeInstancePO != null) {
            return nodeInstancePO;
        }
        flowInstanceServiceBiz.getFlowInstanceIdByRootFlowInstanceIdAndNodeInstanceId(flowInstanceId, nodeInstanceId);
        return flowNodeInstanceService.selectByNodeInstanceId(nodeInstanceId);
    }

    public FlowNodeInstance selectRecentEndNode(String flowInstanceId) {
        FlowInstance rootFlowInstance = flowInstanceService.selectByFlowInstanceId(flowInstanceId);
        FlowDeployment rootFlowDeployment = flowDeploymentService.selectByFlowDeployId(rootFlowInstance.getFlowDeployId());
        Map<String, FlowElement> rootFlowElementMap = FlowModelUtil.getFlowElementMap(rootFlowDeployment.getFlowModel());

        List<FlowNodeInstance> nodeInstancePOList = flowNodeInstanceService.selectDescByFlowInstanceId(flowInstanceId);
        for (FlowNodeInstance nodeInstancePO : nodeInstancePOList) {
            int elementType = FlowModelUtil.getElementType(nodeInstancePO.getNodeKey(), rootFlowElementMap);
            if (elementType == FlowElementType.END_EVENT) {
                return nodeInstancePO;
            }
        }
        return null;
    }
}
