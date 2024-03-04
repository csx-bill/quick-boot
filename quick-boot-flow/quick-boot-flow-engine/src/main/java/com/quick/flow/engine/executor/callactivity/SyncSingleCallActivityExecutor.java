package com.quick.flow.engine.executor.callactivity;

import com.quick.flow.engine.bo.NodeInstance;
import com.quick.flow.engine.bo.NodeInstanceBO;
import com.quick.flow.engine.common.*;
import com.quick.flow.engine.entity.*;
import com.quick.flow.engine.exception.ProcessException;
import com.quick.flow.engine.exception.SuspendException;
import com.quick.flow.engine.model.FlowElement;
import com.quick.flow.engine.model.InstanceData;
import com.quick.flow.engine.param.CommitTaskParam;
import com.quick.flow.engine.param.RollbackTaskParam;
import com.quick.flow.engine.param.StartProcessParam;
import com.quick.flow.engine.result.CommitTaskResult;
import com.quick.flow.engine.result.RollbackTaskResult;
import com.quick.flow.engine.result.RuntimeResult;
import com.quick.flow.engine.result.StartProcessResult;
import com.quick.flow.engine.util.FlowModelUtil;
import com.quick.flow.engine.util.InstanceDataUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * CallActivityExecutor base on sync and single instance mode,
 * support the dynamic assignment of subFlowModule on the execution side
 * <p>
 * feature e.g.
 * 1.Automatically suspend when executing to CallActivity node
 * 2.External systems can attach unique attributes on CallActivity node
 * 3.When External systems compute subFlowModuleId success, need continue to submit downward
 * 4.CallActivity node support repeated submission
 */
@Service
public class SyncSingleCallActivityExecutor extends AbstractCallActivityExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncSingleCallActivityExecutor.class);

    @Override
    protected void doExecute(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBO currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        if (currentNodeInstance.getStatus() == NodeInstanceStatus.COMPLETED) {
            LOGGER.warn("doExecute reentrant: currentNodeInstance is completed.||runtimeContext={}", runtimeContext);
            return;
        }

        if (currentNodeInstance.getStatus() != NodeInstanceStatus.ACTIVE) {
            currentNodeInstance.setStatus(NodeInstanceStatus.ACTIVE);
        }
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);

        FlowElement flowElement = runtimeContext.getCurrentNodeModel();
        String nodeName = FlowModelUtil.getElementName(flowElement);
        LOGGER.info("doExecute: syncSingleCallActivity to commit.||flowInstanceId={}||nodeInstanceId={}||nodeKey={}||nodeName={}",
            runtimeContext.getFlowInstanceId(), currentNodeInstance.getNodeInstanceId(), flowElement.getKey(), nodeName);
        throw new SuspendException(ErrorEnum.COMMIT_SUSPEND, MessageFormat.format(Constants.NODE_INSTANCE_FORMAT,
            flowElement.getKey(), nodeName, currentNodeInstance.getNodeInstanceId()));
    }

    @Override
    protected void preCommit(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBO suspendNodeInstance = runtimeContext.getSuspendNodeInstance();
        NodeInstanceBO currentNodeInstance = new NodeInstanceBO();
        BeanUtils.copyProperties(suspendNodeInstance, currentNodeInstance);
        runtimeContext.setCurrentNodeInstance(currentNodeInstance);
    }

    @Override
    protected void doCommit(RuntimeContext runtimeContext) throws ProcessException {
        boolean commitCallActivityNode = CollectionUtils.isEmpty(runtimeContext.getSuspendNodeInstanceStack());
        if (commitCallActivityNode) {
            startProcessCallActivity(runtimeContext);
        } else {
            commitCallActivity(runtimeContext);
        }
    }

    @Override
    protected void postCommit(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBO currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);
    }

    @Override
    protected void doRollback(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBO currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        FlowInstanceMapping flowInstanceMapping = flowInstanceMappingDAO.selectFlowInstanceMappingPO(runtimeContext.getFlowInstanceId(), currentNodeInstance.getNodeInstanceId());
        String subFlowInstanceId = flowInstanceMapping.getSubFlowInstanceId();

        String taskInstanceId = null;
        if (CollectionUtils.isEmpty(runtimeContext.getSuspendNodeInstanceStack())) {
            FlowNodeInstance flowNodeInstance = nodeInstanceService.selectRecentEndNode(subFlowInstanceId);
            taskInstanceId = flowNodeInstance.getNodeInstanceId();
        } else {
            taskInstanceId = runtimeContext.getSuspendNodeInstanceStack().pop();
        }

        RollbackTaskParam rollbackTaskParam = new RollbackTaskParam();
        rollbackTaskParam.setRuntimeContext(runtimeContext);
        rollbackTaskParam.setFlowInstanceId(subFlowInstanceId);
        rollbackTaskParam.setTaskInstanceId(taskInstanceId);
        RollbackTaskResult rollbackTaskResult = runtimeProcessor.rollback(rollbackTaskParam);
        LOGGER.info("callActivity rollback.||rollbackTaskParam={}||rollbackTaskResult={}", rollbackTaskParam, rollbackTaskResult);
        // 4.update flowInstance mapping
        updateFlowInstanceMapping(runtimeContext);
        handleCallActivityResult(runtimeContext, rollbackTaskResult);
    }

    @Override
    protected void postRollback(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBO currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);
    }

    protected void startProcessCallActivity(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBO currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        // 1.check reentrant execute
        FlowInstanceMapping flowInstanceMapping = flowInstanceMappingDAO.selectFlowInstanceMappingPO(runtimeContext.getFlowInstanceId(), currentNodeInstance.getNodeInstanceId());
        if (flowInstanceMapping != null) {
            handleReentrantSubFlowInstance(runtimeContext, flowInstanceMapping);
            return;
        }
        // 2.check CallActivity nested level
        preCheckCallActivityNestedLevel(runtimeContext);

        // 3.get flowModuleId
        String callActivityFlowModuleId = runtimeContext.getCallActivityFlowModuleId();
        runtimeContext.setCallActivityFlowModuleId(null); // avoid misuse
        // 4.calculate variables
        List<InstanceData> callActivityVariables = getCallActivityVariables(runtimeContext);

        StartProcessParam startProcessParam = new StartProcessParam();
        startProcessParam.setRuntimeContext(runtimeContext);
        startProcessParam.setFlowModuleId(callActivityFlowModuleId);
        startProcessParam.setVariables(callActivityVariables);
        StartProcessResult startProcessResult = runtimeProcessor.startProcess(startProcessParam);
        LOGGER.info("callActivity startProcess.||startProcessParam={}||startProcessResult={}", startProcessParam, startProcessResult);
        // 5.save flowInstance mapping
        saveFlowInstanceMapping(runtimeContext, startProcessResult.getFlowInstanceId());
        handleCallActivityResult(runtimeContext, startProcessResult);
    }

    private void preCheckCallActivityNestedLevel(RuntimeContext runtimeContext) throws ProcessException {
        int maxCallActivityNestedLevel = businessConfig.getCallActivityNestedLevel(runtimeContext.getCaller());
        int currentCallActivityNestedLevel = 0;
        RuntimeContext tmpRuntimeContext = runtimeContext;
        while (tmpRuntimeContext != null) {
            currentCallActivityNestedLevel++;
            tmpRuntimeContext = tmpRuntimeContext.getParentRuntimeContext();
        }
        if (maxCallActivityNestedLevel < currentCallActivityNestedLevel) {
            throw new ProcessException(ErrorEnum.FLOW_NESTED_LEVEL_EXCEEDED);
        }
    }

    private void saveFlowInstanceMapping(RuntimeContext runtimeContext, String subFlowInstanceId) {
        FlowInstanceMapping flowInstanceMapping = new FlowInstanceMapping();
        flowInstanceMapping.setFlowInstanceId(runtimeContext.getFlowInstanceId());
        NodeInstanceBO currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        flowInstanceMapping.setNodeKey(currentNodeInstance.getNodeKey());
        flowInstanceMapping.setNodeInstanceId(currentNodeInstance.getNodeInstanceId());
        flowInstanceMapping.setSubFlowInstanceId(subFlowInstanceId);
        flowInstanceMapping.setType(FlowInstanceMappingType.EXECUTE);
        flowInstanceMapping.setTenantId(runtimeContext.getTenant());
        flowInstanceMappingDAO.save(flowInstanceMapping);
    }

    private void handleReentrantSubFlowInstance(RuntimeContext runtimeContext, FlowInstanceMapping flowInstanceMapping) throws ProcessException {
        String subFlowInstanceId = flowInstanceMapping.getSubFlowInstanceId();
        RuntimeResult subFlowInstanceFirstUserTask = getSubFlowInstanceFirstUserTask(subFlowInstanceId);
        if (subFlowInstanceFirstUserTask != null) {
            runtimeContext.setCallActivityRuntimeResultList(Arrays.asList(subFlowInstanceFirstUserTask));
            throw new SuspendException(ErrorEnum.COMMIT_SUSPEND);
        }
        LOGGER.info("callActivity did not find userTask.||subFlowInstanceId={}", subFlowInstanceId);
    }

    private RuntimeResult getSubFlowInstanceFirstUserTask(String subFlowInstanceId) {
        FlowInstance subFlowInstance = processInstanceDAO.selectByFlowInstanceId(subFlowInstanceId);
        FlowDeployment subFlowDeployment = flowDeploymentService.selectByDeployId(subFlowInstance.getFlowDeployId());
        Map<String, FlowElement> subFlowElementMap = FlowModelUtil.getFlowElementMap(subFlowDeployment.getFlowModel());

        List<FlowNodeInstance> flowNodeInstanceList = nodeInstanceDAO.selectByFlowInstanceId(subFlowInstanceId);
        for (FlowNodeInstance flowNodeInstance : flowNodeInstanceList) {
            int elementType = FlowModelUtil.getElementType(flowNodeInstance.getNodeKey(), subFlowElementMap);
            if (elementType == FlowElementType.USER_TASK) {
                return buildCallActivityFirstUserTaskRuntimeResult(subFlowInstance, subFlowElementMap, flowNodeInstance);
            } else if (elementType == FlowElementType.CALL_ACTIVITY) {
                FlowInstanceMapping flowInstanceMapping = flowInstanceMappingDAO.selectFlowInstanceMappingPO(subFlowInstanceId, flowNodeInstance.getNodeInstanceId());
                if (flowInstanceMapping == null) {
                    LOGGER.warn("callActivity did not find instanceMapping.||subFlowInstanceId={}", subFlowInstanceId);
                    break;
                }
                RuntimeResult runtimeResult = getSubFlowInstanceFirstUserTask(flowInstanceMapping.getSubFlowInstanceId());
                if (runtimeResult != null) {
                    return runtimeResult;
                }
            }
        }
        return null;
    }

    private RuntimeResult buildCallActivityFirstUserTaskRuntimeResult(FlowInstance subFlowInstance, Map<String, FlowElement> subFlowElementMap, FlowNodeInstance flowNodeInstance) {
        RuntimeResult runtimeResult = new RuntimeResult();
        runtimeResult.setErrCode(ErrorEnum.COMMIT_SUSPEND.getErrNo());
        runtimeResult.setErrMsg(ErrorEnum.COMMIT_SUSPEND.getErrMsg());
        runtimeResult.setFlowInstanceId(subFlowInstance.getFlowInstanceId());
        runtimeResult.setStatus(subFlowInstance.getStatus());

        NodeInstance nodeInstance = new NodeInstance();
        BeanUtils.copyProperties(flowNodeInstance, nodeInstance);
        nodeInstance.setCreateTime(null);
        nodeInstance.setModifyTime(null);
        nodeInstance.setModelKey(flowNodeInstance.getNodeKey());
        FlowElement flowElement = subFlowElementMap.get(flowNodeInstance.getNodeKey());
        nodeInstance.setModelName(FlowModelUtil.getElementName(flowElement));
        nodeInstance.setProperties(flowElement.getProperties());

        runtimeResult.setActiveTaskInstance(nodeInstance);
        FlowInstanceData flowInstanceData = instanceDataDAO.select(subFlowInstance.getFlowInstanceId(), flowNodeInstance.getInstanceDataId());
        Map<String, InstanceData> instanceDataMap = InstanceDataUtil.getInstanceDataMap(flowInstanceData.getInstanceData());
        runtimeResult.setVariables(InstanceDataUtil.getInstanceDataList(instanceDataMap));
        return runtimeResult;
    }

    protected void commitCallActivity(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBO suspendNodeInstance = runtimeContext.getSuspendNodeInstance();
        FlowInstanceMapping flowInstanceMapping = flowInstanceMappingDAO.selectFlowInstanceMappingPO(runtimeContext.getFlowInstanceId(), suspendNodeInstance.getNodeInstanceId());
        String subFlowInstanceId = flowInstanceMapping.getSubFlowInstanceId();

        CommitTaskParam commitTaskParam = new CommitTaskParam();
        commitTaskParam.setRuntimeContext(runtimeContext);
        commitTaskParam.setFlowInstanceId(subFlowInstanceId);
        commitTaskParam.setTaskInstanceId(runtimeContext.getSuspendNodeInstanceStack().pop());
        commitTaskParam.setVariables(InstanceDataUtil.getInstanceDataList(runtimeContext.getInstanceDataMap()));
        // transparent transmission callActivity param
        commitTaskParam.setCallActivityFlowModuleId(runtimeContext.getCallActivityFlowModuleId());
        runtimeContext.setCallActivityFlowModuleId(null); // avoid misuse

        CommitTaskResult commitTaskResult = runtimeProcessor.commit(commitTaskParam);
        LOGGER.info("callActivity commit.||commitTaskParam={}||commitTaskResult={}", commitTaskParam, commitTaskResult);
        handleCallActivityResult(runtimeContext, commitTaskResult);
    }

    private void updateFlowInstanceMapping(RuntimeContext runtimeContext) {
        NodeInstanceBO currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        if (currentNodeInstance.getStatus() != NodeInstanceStatus.COMPLETED) {
            return;
        }
        currentNodeInstance.setStatus(NodeInstanceStatus.DISABLED);
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);

        NodeInstanceBO newNodeInstanceBO = new NodeInstanceBO();
        BeanUtils.copyProperties(currentNodeInstance, newNodeInstanceBO);
        newNodeInstanceBO.setId(null);
        String newNodeInstanceId = genId();
        newNodeInstanceBO.setNodeInstanceId(newNodeInstanceId);
        newNodeInstanceBO.setStatus(NodeInstanceStatus.ACTIVE);
        runtimeContext.setCurrentNodeInstance(newNodeInstanceBO);

        FlowInstanceMapping oldFlowInstanceMapping = flowInstanceMappingDAO.selectFlowInstanceMappingPO(runtimeContext.getFlowInstanceId(), currentNodeInstance.getNodeInstanceId());
        flowInstanceMappingDAO.updateType(oldFlowInstanceMapping.getFlowInstanceId(), oldFlowInstanceMapping.getNodeInstanceId(), FlowInstanceMappingType.TERMINATED);

        FlowInstanceMapping newFlowInstanceMapping = new FlowInstanceMapping();
        BeanUtils.copyProperties(oldFlowInstanceMapping, newFlowInstanceMapping);
        newFlowInstanceMapping.setId(null);
        newFlowInstanceMapping.setNodeInstanceId(newNodeInstanceId);
//        newFlowInstanceMappingPO.setCreateTime(new Date());
//        newFlowInstanceMappingPO.setModifyTime(new Date());
        flowInstanceMappingDAO.insert(newFlowInstanceMapping);
    }

    /**
     * common handle RuntimeResult from startProcessCallActivity, commitCallActivity, rollbackCallActivity.
     *
     * @param runtimeContext
     * @param runtimeResult
     * @throws ProcessException
     */
    protected void handleCallActivityResult(RuntimeContext runtimeContext, RuntimeResult runtimeResult) throws ProcessException {
        ErrorEnum errorEnum = ErrorEnum.getErrorEnum(runtimeResult.getErrCode());
        switch (errorEnum) {
            case SUCCESS:
                handleSuccessSubFlowResult(runtimeContext, runtimeResult);
                break;
            case COMMIT_SUSPEND:
            case ROLLBACK_SUSPEND:
                runtimeContext.getCurrentNodeInstance().setStatus(NodeInstanceStatus.ACTIVE);
                runtimeContext.setCallActivityRuntimeResultList(Arrays.asList(runtimeResult));
                throw new SuspendException(errorEnum);
            default:
                throw new ProcessException(errorEnum);
        }
    }

    private void handleSuccessSubFlowResult(RuntimeContext runtimeContext, RuntimeResult runtimeResult) throws ProcessException {
        NodeInstanceBO currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        if (runtimeResult.getStatus() == FlowInstanceStatus.TERMINATED) {
            // The subFlow rollback from the StartNode to the MainFlow
            currentNodeInstance.setStatus(NodeInstanceStatus.DISABLED);
            flowInstanceMappingDAO.updateType(runtimeContext.getFlowInstanceId(), currentNodeInstance.getNodeInstanceId(), FlowInstanceMappingType.TERMINATED);
        } else if (runtimeResult.getStatus() == FlowInstanceStatus.END) {
            // The subFlow is completed from the EndNode to the MainFlow
            currentNodeInstance.setStatus(NodeInstanceStatus.COMPLETED);
            // transfer data from subFlow to MainFlow
            saveCallActivityEndInstanceData(runtimeContext, runtimeResult);
        }
    }

    private void saveCallActivityEndInstanceData(RuntimeContext runtimeContext, RuntimeResult runtimeResult) throws ProcessException {
        NodeInstanceBO currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        List<InstanceData> instanceDataFromSubFlow = calculateCallActivityOutParamFromSubFlow(runtimeContext, runtimeResult.getVariables());
        // 1.merge to current data
        Map<String, InstanceData> currentInstanceDataMap = runtimeContext.getInstanceDataMap();
        currentInstanceDataMap.putAll(InstanceDataUtil.getInstanceDataMap(instanceDataFromSubFlow));
        // 2.save data
        String instanceDataId = genId();
        FlowInstanceData flowInstanceData = buildCallActivityEndInstanceData(instanceDataId, runtimeContext);
        instanceDataDAO.insert(flowInstanceData);
        runtimeContext.setInstanceDataId(instanceDataId);
        // 3.set currentNode completed
        currentNodeInstance.setInstanceDataId(runtimeContext.getInstanceDataId());
    }
}
