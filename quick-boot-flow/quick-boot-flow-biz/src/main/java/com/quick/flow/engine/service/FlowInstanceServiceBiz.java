package com.quick.flow.engine.service;

import com.quick.flow.engine.common.FlowElementType;
import com.quick.flow.engine.model.FlowElement;
import com.quick.flow.engine.util.FlowModelUtil;
import com.quick.flow.entity.FlowDeployment;
import com.quick.flow.entity.FlowInstance;
import com.quick.flow.entity.FlowInstanceMapping;
import com.quick.flow.entity.FlowNodeInstance;
import com.quick.flow.service.IFlowDeploymentService;
import com.quick.flow.service.IFlowInstanceMappingService;
import com.quick.flow.service.IFlowInstanceService;
import com.quick.flow.service.IFlowNodeInstanceService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class FlowInstanceServiceBiz {

    @Resource
    private IFlowNodeInstanceService flowNodeInstanceService;

    @Resource
    private IFlowInstanceMappingService flowInstanceMappingService;

    @Resource
    private IFlowInstanceService flowInstanceService;

    @Resource
    private IFlowDeploymentService flowDeploymentService;

    /**
     * According to rootFlowInstanceId and commitNodeInstanceId, build and return NodeInstance stack.
     * When the subProcessInstance of each layer is executed, stack needs to pop up.
     * <p>
     * e.g.
     * <p>
     * rootNodeInstanceId
     * ^
     * ..................
     * ^
     * commitNodeInstanceId
     *
     * @param rootFlowInstanceId
     * @param commitNodeInstanceId
     * @return
     */
    public Stack<String> getNodeInstanceIdStack(String rootFlowInstanceId, String commitNodeInstanceId) {
        if (StringUtils.isBlank(commitNodeInstanceId)) {
            log.info("getNodeInstanceId2RootStack result is empty.||rootFlowInstanceId={}||commitNodeInstanceId={}", rootFlowInstanceId, commitNodeInstanceId);
            return new Stack<>();
        }
        FlowInstanceTreeResult flowInstanceTreeResult = buildFlowInstanceTree(rootFlowInstanceId,
            nodeInstancePO -> nodeInstancePO.getNodeInstanceId().equals(commitNodeInstanceId));
        NodeInstancePOJO rightNodeInstance = flowInstanceTreeResult.getInterruptNodeInstancePOJO();
        Stack<String> stack = new Stack<>();
        while (rightNodeInstance != null) {
            stack.push(rightNodeInstance.getId());
            rightNodeInstance = rightNodeInstance.getFlowInstance().getBelongNodeInstance();
        }
        log.info("getNodeInstanceId2RootStack result.||rootFlowInstanceId={}||commitNodeInstanceId={}||result={}", rootFlowInstanceId, commitNodeInstanceId, stack);
        return stack;
    }

    /**
     * According to rootFlowInstanceId, get all subFlowInstanceIds from db.
     *
     * @param rootFlowInstanceId
     * @return
     */
    public Set<String> getAllSubFlowInstanceIds(String rootFlowInstanceId) {
        FlowInstanceTreeResult flowInstanceTreeResult = buildFlowInstanceTree(rootFlowInstanceId, null);
        FlowInstancePOJO flowInstancePOJO = flowInstanceTreeResult.getRootFlowInstancePOJO();
        Set<String> result = getAllSubFlowInstanceIdsInternal(flowInstancePOJO);
        result.remove(rootFlowInstanceId);
        log.info("getAllSubFlowInstanceIds result.||rootFlowInstanceId={}||result={}", rootFlowInstanceId, result);
        return result;
    }

    private Set<String> getAllSubFlowInstanceIdsInternal(FlowInstancePOJO flowInstancePOJO) {
        Set<String> result = new TreeSet<>();
        if (flowInstancePOJO == null) {
            return result;
        }
        result.add(flowInstancePOJO.getId());
        List<NodeInstancePOJO> nodeInstanceList = flowInstancePOJO.getNodeInstanceList();
        for (NodeInstancePOJO nodeInstancePOJO : nodeInstanceList) {
            if (CollectionUtils.isEmpty(nodeInstancePOJO.getSubFlowInstanceList())) {
                continue;
            }
            FlowInstancePOJO subFlowInstancePOJO = nodeInstancePOJO.getSubFlowInstanceList().get(0);
            Set<String> subFlowInstanceResult = getAllSubFlowInstanceIdsInternal(subFlowInstancePOJO);
            result.addAll(subFlowInstanceResult);
        }
        return result;
    }


    /**
     * According to rootFlowInstanceId and nodeInstanceId,
     * Return the FlowInstanceId where the nodeInstanceId is located.
     *
     * @param rootFlowInstanceId
     * @param nodeInstanceId
     * @return
     */
    public String getFlowInstanceIdByRootFlowInstanceIdAndNodeInstanceId(String rootFlowInstanceId, String nodeInstanceId) {
        if (StringUtils.isBlank(nodeInstanceId)) {
            return StringUtils.EMPTY;
        }
        FlowInstanceTreeResult flowInstanceTreeResult = buildFlowInstanceTree(rootFlowInstanceId,
            nodeInstancePO -> nodeInstancePO.getNodeInstanceId().equals(nodeInstanceId));
        NodeInstancePOJO rightNodeInstance = flowInstanceTreeResult.getInterruptNodeInstancePOJO();
        if (rightNodeInstance == null) {
            return StringUtils.EMPTY;
        }
        return rightNodeInstance.getFlowInstance().getId();
    }

    /**
     * According to rootFlowInstanceId and instanceDataId,
     * Return the FlowInstanceId where the instanceDataId is located.
     *
     * @param rootFlowInstanceId
     * @param instanceDataId
     * @return
     */
    public String getFlowInstanceIdByRootFlowInstanceIdAndInstanceDataId(String rootFlowInstanceId, String instanceDataId) {
        if (StringUtils.isBlank(instanceDataId)) {
            return StringUtils.EMPTY;
        }
        FlowInstanceTreeResult flowInstanceTreeResult = buildFlowInstanceTree(rootFlowInstanceId,
            nodeInstancePO -> nodeInstancePO.getInstanceDataId().equals(instanceDataId));
        NodeInstancePOJO rightNodeInstance = flowInstanceTreeResult.getInterruptNodeInstancePOJO();
        if (rightNodeInstance == null) {
            return StringUtils.EMPTY;
        }
        return rightNodeInstance.getFlowInstance().getId();
    }

    // common : build a flowInstanceAndNodeInstance tree
    private FlowInstanceTreeResult buildFlowInstanceTree(String rootFlowInstanceId, InterruptCondition interruptCondition) {
        FlowInstanceTreeResult flowInstanceTreeResult = new FlowInstanceTreeResult();
        FlowInstancePOJO flowInstance = new FlowInstancePOJO();
        flowInstance.setId(rootFlowInstanceId);
        flowInstanceTreeResult.setRootFlowInstancePOJO(flowInstance);

        FlowInstance rootFlowInstance = flowInstanceService.selectByFlowInstanceId(rootFlowInstanceId);
        FlowDeployment rootFlowDeployment = flowDeploymentService.selectByFlowDeployId(rootFlowInstance.getFlowDeployId());
        Map<String, FlowElement> rootFlowElementMap = FlowModelUtil.getFlowElementMap(rootFlowDeployment.getFlowModel());

        List<FlowNodeInstance> nodeInstancePOList = flowNodeInstanceService.selectDescByFlowInstanceId(rootFlowInstanceId);
        for (FlowNodeInstance nodeInstancePO : nodeInstancePOList) {
            NodeInstancePOJO nodeInstance = new NodeInstancePOJO();
            nodeInstance.setId(nodeInstancePO.getNodeInstanceId());
            nodeInstance.setFlowInstance(flowInstance);
            flowInstance.getNodeInstanceList().add(nodeInstance);

            if (interruptCondition != null && interruptCondition.match(nodeInstancePO)) {
                flowInstanceTreeResult.setInterruptNodeInstancePOJO(nodeInstance);
                return flowInstanceTreeResult;
            }

            int elementType = FlowModelUtil.getElementType(nodeInstancePO.getNodeKey(), rootFlowElementMap);
            if (elementType != FlowElementType.CALL_ACTIVITY) {
                continue;
            }
            List<FlowInstanceMapping> flowInstanceMappingPOS = flowInstanceMappingService.selectFlowInstanceMappingList(nodeInstancePO.getFlowInstanceId(), nodeInstancePO.getNodeInstanceId());
            for (FlowInstanceMapping flowInstanceMappingPO : flowInstanceMappingPOS) {
                FlowInstanceTreeResult subFlowInstanceTreeResult = buildFlowInstanceTree(flowInstanceMappingPO.getSubFlowInstanceId(), interruptCondition);
                FlowInstancePOJO subFlowInstance = subFlowInstanceTreeResult.getRootFlowInstancePOJO();
                subFlowInstance.setBelongNodeInstance(nodeInstance);
                nodeInstance.getSubFlowInstanceList().add(subFlowInstance);
                if (subFlowInstanceTreeResult.needInterrupt()) {
                    flowInstanceTreeResult.setInterruptNodeInstancePOJO(subFlowInstanceTreeResult.getInterruptNodeInstancePOJO());
                    return flowInstanceTreeResult;
                }
            }
        }
        return flowInstanceTreeResult;
    }

    private static class FlowInstancePOJO {
        private String id;
        private NodeInstancePOJO belongNodeInstance;
        private List<NodeInstancePOJO> nodeInstanceList = new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public NodeInstancePOJO getBelongNodeInstance() {
            return belongNodeInstance;
        }

        public void setBelongNodeInstance(NodeInstancePOJO belongNodeInstance) {
            this.belongNodeInstance = belongNodeInstance;
        }

        public List<NodeInstancePOJO> getNodeInstanceList() {
            return nodeInstanceList;
        }

        public void setNodeInstanceList(List<NodeInstancePOJO> nodeInstanceList) {
            this.nodeInstanceList = nodeInstanceList;
        }
    }

    private static class NodeInstancePOJO {

        private String id;
        private FlowInstancePOJO flowInstance;
        private List<FlowInstancePOJO> subFlowInstanceList = new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public FlowInstancePOJO getFlowInstance() {
            return flowInstance;
        }

        public void setFlowInstance(FlowInstancePOJO flowInstance) {
            this.flowInstance = flowInstance;
        }

        public List<FlowInstancePOJO> getSubFlowInstanceList() {
            return subFlowInstanceList;
        }

        public void setSubFlowInstanceList(List<FlowInstancePOJO> subFlowInstanceList) {
            this.subFlowInstanceList = subFlowInstanceList;
        }
    }

    private static class FlowInstanceTreeResult {
        private FlowInstancePOJO rootFlowInstancePOJO;
        private NodeInstancePOJO interruptNodeInstancePOJO;

        public FlowInstancePOJO getRootFlowInstancePOJO() {
            return rootFlowInstancePOJO;
        }

        public void setRootFlowInstancePOJO(FlowInstancePOJO rootFlowInstancePOJO) {
            this.rootFlowInstancePOJO = rootFlowInstancePOJO;
        }

        public NodeInstancePOJO getInterruptNodeInstancePOJO() {
            return interruptNodeInstancePOJO;
        }

        public void setInterruptNodeInstancePOJO(NodeInstancePOJO interruptNodeInstancePOJO) {
            this.interruptNodeInstancePOJO = interruptNodeInstancePOJO;
        }

        public boolean needInterrupt() {
            return interruptNodeInstancePOJO != null;
        }
    }

    /**
     * When build a flowInstanceAndNodeInstance tree,
     * we allow timely interruption to improve response.
     */
    private interface InterruptCondition {

        /**
         * Returns true when the condition is match
         *
         * @param nodeInstancePO
         * @return
         */
        boolean match(FlowNodeInstance nodeInstancePO);
    }
}
