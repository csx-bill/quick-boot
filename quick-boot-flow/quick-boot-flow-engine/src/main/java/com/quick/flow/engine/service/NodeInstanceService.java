package com.quick.flow.engine.service;

import com.quick.flow.engine.common.FlowElementType;
import com.quick.flow.engine.dao.NodeInstanceDAO;
import com.quick.flow.engine.dao.ProcessInstanceDAO;
import com.quick.flow.engine.entity.FlowDeployment;
import com.quick.flow.engine.entity.FlowInstance;
import com.quick.flow.engine.entity.FlowNodeInstance;
import com.quick.flow.engine.model.FlowElement;
import com.quick.flow.engine.util.FlowModelUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NodeInstanceService {

    @Resource
    private NodeInstanceDAO nodeInstanceDAO;

    @Resource
    private ProcessInstanceDAO processInstanceDAO;

    @Resource
    private IFlowDeploymentService flowDeploymentService;

    @Resource
    private FlowInstanceService flowInstanceService;

    public FlowNodeInstance selectByNodeInstanceId(String flowInstanceId, String nodeInstanceId, boolean effectiveForSubFlowInstance) {
        FlowNodeInstance flowNodeInstance = nodeInstanceDAO.selectByNodeInstanceId(flowInstanceId, nodeInstanceId);
        if (!effectiveForSubFlowInstance) {
            return flowNodeInstance;
        }
        if (flowNodeInstance != null) {
            return flowNodeInstance;
        }
        String subFlowInstanceId = flowInstanceService.getFlowInstanceIdByRootFlowInstanceIdAndNodeInstanceId(flowInstanceId, nodeInstanceId);
        return nodeInstanceDAO.selectByNodeInstanceId(subFlowInstanceId, nodeInstanceId);
    }

    public FlowNodeInstance selectRecentEndNode(String flowInstanceId) {
        FlowInstance rootFlowInstance = processInstanceDAO.selectByFlowInstanceId(flowInstanceId);
        FlowDeployment rootFlowDeployment = flowDeploymentService.selectByDeployId(rootFlowInstance.getFlowDeployId());
        Map<String, FlowElement> rootFlowElementMap = FlowModelUtil.getFlowElementMap(rootFlowDeployment.getFlowModel());

        List<FlowNodeInstance> flowNodeInstanceList = nodeInstanceDAO.selectDescByFlowInstanceId(flowInstanceId);
        for (FlowNodeInstance flowNodeInstance : flowNodeInstanceList) {
            int elementType = FlowModelUtil.getElementType(flowNodeInstance.getNodeKey(), rootFlowElementMap);
            if (elementType == FlowElementType.END_EVENT) {
                return flowNodeInstance;
            }
        }
        return null;
    }
}
