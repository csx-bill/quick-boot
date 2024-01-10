package com.quick.flow.engine.executor;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quick.flow.engine.bo.NodeInstanceBO;
import com.quick.flow.engine.common.*;
import com.quick.flow.engine.exception.ProcessException;
import com.quick.flow.engine.exception.ReentrantException;
import com.quick.flow.engine.model.FlowElement;
import com.quick.flow.engine.model.InstanceData;
import com.quick.flow.engine.util.FlowModelUtil;
import com.quick.flow.engine.util.InstanceDataUtil;
import com.quick.flow.entity.FlowInstance;
import com.quick.flow.entity.FlowInstanceData;
import com.quick.flow.entity.FlowNodeInstance;
import com.quick.flow.entity.FlowNodeInstanceLog;
import com.quick.flow.service.IFlowInstanceService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FlowExecutor extends RuntimeExecutor {

    @Resource
    private IFlowInstanceService flowInstanceService;

    ////////////////////////////////////////execute////////////////////////////////////////

    @Override
    public void execute(RuntimeContext runtimeContext) throws ProcessException {
        int processStatus = ProcessStatus.SUCCESS;
        try {
            preExecute(runtimeContext);
            doExecute(runtimeContext);
        } catch (ProcessException pe) {
            if (!ErrorEnum.isSuccess(pe.getErrNo())) {
                processStatus = ProcessStatus.FAILED;
            }
            throw pe;
        } finally {
            runtimeContext.setProcessStatus(processStatus);
            postExecute(runtimeContext);
        }
    }

    /**
     * Fill runtimeContext:
     * 1. Generate flowInstanceId and insert FlowInstancePO into db
     * 2. Generate instanceDataId and insert InstanceDataPO into db
     * 3. Update runtimeContext: flowInstanceId, flowInstanceStatus, instanceDataId, nodeInstanceList, suspendNodeInstance
     *
     * @throws Exception
     */
    private void preExecute(RuntimeContext runtimeContext) throws ProcessException {
        //1.save FlowInstancePO into db
        FlowInstance flowInstancePO = saveFlowInstance(runtimeContext);

        //2.save InstanceDataPO into db
        String instanceDataId = saveInstanceData(flowInstancePO, runtimeContext.getInstanceDataMap());

        //3.update runtimeContext
        fillExecuteContext(runtimeContext, flowInstancePO.getFlowInstanceId(), instanceDataId);
    }

    private FlowInstance saveFlowInstance(RuntimeContext runtimeContext) throws ProcessException {
        FlowInstance flowInstance = buildFlowInstancePO(runtimeContext);
        flowInstanceService.save(flowInstance);
        return flowInstance;
    }

    private FlowInstance buildFlowInstancePO(RuntimeContext runtimeContext) {
        FlowInstance flowInstancePO = new FlowInstance();
        // copy flow info
        BeanUtils.copyProperties(runtimeContext, flowInstancePO);
        // generate flowInstanceId
        flowInstancePO.setFlowInstanceId(genId());
        RuntimeContext parentRuntimeContext = runtimeContext.getParentRuntimeContext();
        if (parentRuntimeContext != null) {
            flowInstancePO.setParentFlowInstanceId(parentRuntimeContext.getFlowInstanceId());
        }
        flowInstancePO.setStatus(FlowInstanceStatus.RUNNING);
        return flowInstancePO;
    }

    private String saveInstanceData(FlowInstance flowInstance, Map<String, InstanceData> instanceDataMap) throws ProcessException {
        if (MapUtils.isEmpty(instanceDataMap)) {
            return StringUtils.EMPTY;
        }

        FlowInstanceData flowInstanceData = buildInstanceDataPO(flowInstance, instanceDataMap);

        flowInstanceDataService.save(flowInstanceData);

        return flowInstanceData.getInstanceDataId();
    }

    private FlowInstanceData buildInstanceDataPO(FlowInstance flowInstance, Map<String, InstanceData> instanceDataMap) {
        FlowInstanceData instanceDataPO = new FlowInstanceData();
        // copy flow info & flowInstanceId
        BeanUtils.copyProperties(flowInstance, instanceDataPO);

        // generate instanceDataId
        instanceDataPO.setInstanceDataId(genId());
        instanceDataPO.setInstanceData(InstanceDataUtil.getInstanceDataListStr(instanceDataMap));

        instanceDataPO.setNodeInstanceId(StringUtils.EMPTY);
        instanceDataPO.setNodeKey(StringUtils.EMPTY);
        instanceDataPO.setType(InstanceDataType.INIT);
        return instanceDataPO;
    }

    private void fillExecuteContext(RuntimeContext runtimeContext, String flowInstanceId, String instanceDataId) throws ProcessException {
        runtimeContext.setFlowInstanceId(flowInstanceId);
        runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.RUNNING);

        runtimeContext.setInstanceDataId(instanceDataId);

        runtimeContext.setNodeInstanceList(Lists.newArrayList());

        //set startEvent into suspendNodeInstance as the first node to process
        Map<String, FlowElement> flowElementMap = runtimeContext.getFlowElementMap();
        FlowElement startEvent = FlowModelUtil.getStartEvent(flowElementMap);
        if (startEvent == null) {
            log.warn("fillExecuteContext failed: cannot get startEvent.||flowInstance={}||flowDeployId={}",
                    runtimeContext.getFlowInstanceId(), runtimeContext.getFlowDeployId());
            throw new ProcessException(ErrorEnum.GET_NODE_FAILED);
        }
        NodeInstanceBO suspendNodeInstance = new NodeInstanceBO();
        suspendNodeInstance.setNodeKey(startEvent.getKey());
        suspendNodeInstance.setStatus(NodeInstanceStatus.ACTIVE);
        suspendNodeInstance.setSourceNodeInstanceId(StringUtils.EMPTY);
        suspendNodeInstance.setSourceNodeKey(StringUtils.EMPTY);
        runtimeContext.setSuspendNodeInstance(suspendNodeInstance);

        runtimeContext.setCurrentNodeModel(startEvent);
    }

    private void doExecute(RuntimeContext runtimeContext) throws ProcessException {
        RuntimeExecutor runtimeExecutor = getExecuteExecutor(runtimeContext);
        while (runtimeExecutor != null) {
            runtimeExecutor.execute(runtimeContext);
            runtimeExecutor = runtimeExecutor.getExecuteExecutor(runtimeContext);
        }
    }

    private void postExecute(RuntimeContext runtimeContext) throws ProcessException {

        //1.update context with processStatus
        if (runtimeContext.getProcessStatus() == ProcessStatus.SUCCESS) {
            //SUCCESS: update runtimeContext: update suspendNodeInstance
            if (runtimeContext.getCurrentNodeInstance() != null) {
                runtimeContext.setSuspendNodeInstance(runtimeContext.getCurrentNodeInstance());
            }
        }

        //2.save nodeInstanceList to db
        saveNodeInstanceList(runtimeContext, NodeInstanceType.EXECUTE);

        //3.update flowInstance status while completed
        if (isCompleted(runtimeContext)) {
            if (isSubFlowInstance(runtimeContext)) {
                flowInstanceService.updateStatus(runtimeContext.getFlowInstanceId(), FlowInstanceStatus.END);
                runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.END);
            } else {
                flowInstanceService.updateStatus(runtimeContext.getFlowInstanceId(), FlowInstanceStatus.COMPLETED);
                runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.COMPLETED);
            }
            log.info("postExecute: flowInstance process completely.||flowInstanceId={}", runtimeContext.getFlowInstanceId());
        }
    }


    ////////////////////////////////////////commit////////////////////////////////////////

    @Override
    public void commit(RuntimeContext runtimeContext) throws ProcessException {
        int processStatus = ProcessStatus.SUCCESS;
        try {
            preCommit(runtimeContext);
            doCommit(runtimeContext);
        } catch (ReentrantException re) {
            //ignore
        } catch (ProcessException pe) {
            if (!ErrorEnum.isSuccess(pe.getErrNo())) {
                processStatus = ProcessStatus.FAILED;
            }
            throw pe;
        } finally {
            runtimeContext.setProcessStatus(processStatus);
            postCommit(runtimeContext);
        }
    }

    /**
     * Fill runtimeContext:
     * 1. Get instanceData from db firstly
     * 2. merge and save instanceData while commitData is not empty
     * 3. Update runtimeContext: instanceDataId, instanceDataMap, nodeInstanceList, suspendNodeInstance
     *
     * @throws Exception
     */
    private void preCommit(RuntimeContext runtimeContext) throws ProcessException {
        String flowInstanceId = runtimeContext.getFlowInstanceId();
        NodeInstanceBO suspendNodeInstance = runtimeContext.getSuspendNodeInstance();
        String nodeInstanceId = suspendNodeInstance.getNodeInstanceId();

        //1.get instanceData from db
        FlowNodeInstance nodeInstancePO = flowNodeInstanceService.selectByNodeInstanceId(nodeInstanceId);
        if (nodeInstancePO == null) {
            log.warn("preCommit failed: cannot find nodeInstancePO from db.||flowInstanceId={}||nodeInstanceId={}",
                    flowInstanceId, nodeInstanceId);
            throw new ProcessException(ErrorEnum.GET_NODE_INSTANCE_FAILED);
        }

        //unexpected: flowInstance is completed
        if (isCompleted(runtimeContext)) {
            log.warn("preExecute warning: reentrant process. FlowInstance has been processed completely.||runtimeContext={}", runtimeContext);
            runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.COMPLETED);
            suspendNodeInstance.setId(nodeInstancePO.getId());
            suspendNodeInstance.setNodeKey(nodeInstancePO.getNodeKey());
            suspendNodeInstance.setSourceNodeInstanceId(nodeInstancePO.getSourceNodeInstanceId());
            suspendNodeInstance.setSourceNodeKey(nodeInstancePO.getSourceNodeKey());
            suspendNodeInstance.setInstanceDataId(nodeInstancePO.getInstanceDataId());
            suspendNodeInstance.setStatus(nodeInstancePO.getStatus());
            throw new ReentrantException(ErrorEnum.REENTRANT_WARNING);
        }
        Map<String, InstanceData> instanceDataMap;
        String instanceDataId = nodeInstancePO.getInstanceDataId();
        if (StringUtils.isBlank(instanceDataId)) {
            instanceDataMap = Maps.newHashMap();
        } else {
            FlowInstanceData instanceDataPO = flowInstanceDataService.selectByFlowInstanceIdAndInstanceDataId(flowInstanceId, instanceDataId);
            if (instanceDataPO == null) {
                log.warn("preCommit failed: cannot find instanceDataPO from db." +
                    "||flowInstanceId={}||instanceDataId={}", flowInstanceId, instanceDataId);
                throw new ProcessException(ErrorEnum.GET_INSTANCE_DATA_FAILED);
            }
            instanceDataMap = InstanceDataUtil.getInstanceDataMap(instanceDataPO.getInstanceData());
        }

        //2.merge data while commitDataMap is not empty
        Map<String, InstanceData> commitDataMap = runtimeContext.getInstanceDataMap();
        boolean isCallActivityNode = FlowModelUtil.isElementType(nodeInstancePO.getNodeKey(), runtimeContext.getFlowElementMap(), FlowElementType.CALL_ACTIVITY);
        if (isCallActivityNode) {
            // commit callActivity not allow merge data
            instanceDataMap = commitDataMap;
        } else if (MapUtils.isNotEmpty(commitDataMap)) {
            instanceDataId = genId();
            instanceDataMap.putAll(commitDataMap);

            FlowInstanceData commitInstanceDataPO = buildCommitInstanceData(runtimeContext, nodeInstanceId,
                nodeInstancePO.getNodeKey(), instanceDataId, instanceDataMap);
            flowInstanceDataService.save(commitInstanceDataPO);
        }

        //3.update runtimeContext
        fillCommitContext(runtimeContext, nodeInstancePO, instanceDataId, instanceDataMap);
    }

    private FlowInstanceData buildCommitInstanceData(RuntimeContext runtimeContext, String nodeInstanceId, String nodeKey,
                                                   String newInstanceDataId, Map<String, InstanceData> instanceDataMap) {
        FlowInstanceData instanceDataPO = new FlowInstanceData();
        BeanUtils.copyProperties(runtimeContext, instanceDataPO);

        instanceDataPO.setNodeInstanceId(nodeInstanceId);
        instanceDataPO.setNodeKey(nodeKey);
        instanceDataPO.setType(InstanceDataType.COMMIT);

        instanceDataPO.setInstanceDataId(newInstanceDataId);
        instanceDataPO.setInstanceData(InstanceDataUtil.getInstanceDataListStr(instanceDataMap));

        return instanceDataPO;
    }

    private void fillCommitContext(RuntimeContext runtimeContext, FlowNodeInstance nodeInstancePO, String instanceDataId,
                                   Map<String, InstanceData> instanceDataMap) throws ProcessException {

        runtimeContext.setInstanceDataId(instanceDataId);
        runtimeContext.setInstanceDataMap(instanceDataMap);

        updateSuspendNodeInstanceBO(runtimeContext.getSuspendNodeInstance(), nodeInstancePO, instanceDataId);

        setCurrentFlowModel(runtimeContext);

        runtimeContext.setNodeInstanceList(Lists.newArrayList());
    }

    private void doCommit(RuntimeContext runtimeContext) throws ProcessException {
        RuntimeExecutor runtimeExecutor = getExecuteExecutor(runtimeContext);
        runtimeExecutor.commit(runtimeContext);

        runtimeExecutor = runtimeExecutor.getExecuteExecutor(runtimeContext);
        while (runtimeExecutor != null) {
            runtimeExecutor.execute(runtimeContext);
            runtimeExecutor = runtimeExecutor.getExecuteExecutor(runtimeContext);
        }
    }

    private void postCommit(RuntimeContext runtimeContext) throws ProcessException {
        if (runtimeContext.getProcessStatus() == ProcessStatus.SUCCESS && runtimeContext.getCurrentNodeInstance() != null) {
            runtimeContext.setSuspendNodeInstance(runtimeContext.getCurrentNodeInstance());
        }
        //update FlowInstancePO to db
        saveNodeInstanceList(runtimeContext, NodeInstanceType.COMMIT);

        if (isCompleted(runtimeContext)) {
            if (isSubFlowInstance(runtimeContext)) {
                flowInstanceService.updateStatus(runtimeContext.getFlowInstanceId(), FlowInstanceStatus.END);
                runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.END);
            } else {
                flowInstanceService.updateStatus(runtimeContext.getFlowInstanceId(), FlowInstanceStatus.COMPLETED);
                runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.COMPLETED);
            }

            log.info("postCommit: flowInstance process completely.||flowInstanceId={}", runtimeContext.getFlowInstanceId());
        }
    }

    ////////////////////////////////////////rollback////////////////////////////////////////

    @Override
    public void rollback(RuntimeContext runtimeContext) throws ProcessException {
        int processStatus = ProcessStatus.SUCCESS;
        try {
            preRollback(runtimeContext);
            doRollback(runtimeContext);
        } catch (ReentrantException re) {
            //ignore
        } catch (ProcessException pe) {
            if (!ErrorEnum.isSuccess(pe.getErrNo())) {
                processStatus = ProcessStatus.FAILED;
            }
            throw pe;
        } finally {
            runtimeContext.setProcessStatus(processStatus);
            postRollback(runtimeContext);
        }
    }

    private void preRollback(RuntimeContext runtimeContext) throws ProcessException {
        String flowInstanceId = runtimeContext.getFlowInstanceId();

        //1.check node: only the latest enabled(ACTIVE or COMPLETED) nodeInstance can be rollbacked.
        String suspendNodeInstanceId = runtimeContext.getSuspendNodeInstance().getNodeInstanceId();
        FlowNodeInstance rollbackNodeInstancePO = getActiveNodeForRollback(flowInstanceId, suspendNodeInstanceId,
            runtimeContext.getFlowElementMap());
        if (rollbackNodeInstancePO == null) {
            log.warn("preRollback failed: cannot rollback.||runtimeContext={}", runtimeContext);
            throw new ProcessException(ErrorEnum.ROLLBACK_FAILED);
        }

        //2.check status: flowInstance is completed
        if (isCompleted(runtimeContext)) {
            log.warn("invalid preRollback: FlowInstance has been processed completely."
                + "||flowInstanceId={}||flowDeployId={}", flowInstanceId, runtimeContext.getFlowDeployId());
            NodeInstanceBO suspendNodeInstance = new NodeInstanceBO();
            BeanUtils.copyProperties(rollbackNodeInstancePO, suspendNodeInstance);
            runtimeContext.setSuspendNodeInstance(suspendNodeInstance);
            runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.COMPLETED);
            throw new ProcessException(ErrorEnum.ROLLBACK_FAILED);
        }

        //3.get instanceData
        String instanceDataId = rollbackNodeInstancePO.getInstanceDataId();
        Map<String, InstanceData> instanceDataMap;
        if (StringUtils.isBlank(instanceDataId)) {
            instanceDataMap = Maps.newHashMap();
        } else {
            FlowInstanceData instanceDataPO = flowInstanceDataService.selectByFlowInstanceIdAndInstanceDataId(flowInstanceId, instanceDataId);
            if (instanceDataPO == null) {
                log.warn("preRollback failed: cannot find instanceDataPO from db."
                    + "||flowInstanceId={}||instanceDataId={}", flowInstanceId, instanceDataId);
                throw new ProcessException(ErrorEnum.GET_INSTANCE_DATA_FAILED);
            }
            instanceDataMap = InstanceDataUtil.getInstanceDataMap(instanceDataPO.getInstanceData());
        }

        //4.update runtimeContext
        fillRollbackContext(runtimeContext, rollbackNodeInstancePO, instanceDataMap);
    }

    // if(canRollback): only the active Node or the lasted completed Node can be rollback
    private FlowNodeInstance getActiveNodeForRollback(String flowInstanceId, String suspendNodeInstanceId,
                                                    Map<String, FlowElement> flowElementMap) {
        List<FlowNodeInstance> nodeInstancePOList = flowNodeInstanceService.selectDescByFlowInstanceId(flowInstanceId);
        if (CollectionUtils.isEmpty(nodeInstancePOList)) {
            log.warn("getActiveNodeForRollback: nodeInstancePOList is empty."
                + "||flowInstanceId={}||suspendNodeInstanceId={}", flowInstanceId, suspendNodeInstanceId);
            return null;
        }

        for (FlowNodeInstance nodeInstancePO : nodeInstancePOList) {
            int elementType = FlowModelUtil.getElementType(nodeInstancePO.getNodeKey(), flowElementMap);
            if (elementType != FlowElementType.USER_TASK
                && elementType != FlowElementType.END_EVENT
                && elementType != FlowElementType.CALL_ACTIVITY) {
                log.info("getActiveNodeForRollback: ignore un-userTask or un-endEvent or un-callActivity nodeInstance.||flowInstanceId={}"
                    + "||suspendNodeInstanceId={}||nodeKey={}", flowInstanceId, suspendNodeInstanceId, nodeInstancePO.getNodeKey());
                continue;
            }

            if (nodeInstancePO.getStatus() == NodeInstanceStatus.ACTIVE) {
                if (nodeInstancePO.getNodeInstanceId().equals(suspendNodeInstanceId)) {
                    log.info("getActiveNodeForRollback: roll back the active Node."
                        + "||flowInstanceId={}||suspendNodeInstanceId={}", flowInstanceId, suspendNodeInstanceId);
                    return nodeInstancePO;
                }
            } else if (nodeInstancePO.getStatus() == NodeInstanceStatus.COMPLETED) {
                if (nodeInstancePO.getNodeInstanceId().equals(suspendNodeInstanceId)) {
                    log.info("getActiveNodeForRollback: roll back the lasted completed Node."
                            + "||flowInstanceId={}||suspendNodeInstanceId={}||activeNodeInstanceId={}",
                        flowInstanceId, suspendNodeInstanceId, nodeInstancePO);
                    return nodeInstancePO;
                }

                log.warn("getActiveNodeForRollback: cannot rollback the Node."
                    + "||flowInstanceId={}||suspendNodeInstanceId={}", flowInstanceId, suspendNodeInstanceId);
                return null;
            }
            log.info("getActiveNodeForRollback: ignore disabled Node instance.||flowInstanceId={}"
                + "||suspendNodeInstanceId={}||status={}", flowInstanceId, suspendNodeInstanceId, nodeInstancePO.getStatus());

        }
        log.warn("getActiveNodeForRollback: cannot rollback the suspendNodeInstance."
            + "||flowInstanceId={}||suspendNodeInstanceId={}", flowInstanceId, suspendNodeInstanceId);
        return null;
    }

    private void doRollback(RuntimeContext runtimeContext) throws ProcessException {
        RuntimeExecutor runtimeExecutor = getRollbackExecutor(runtimeContext);
        while (runtimeExecutor != null) {
            runtimeExecutor.rollback(runtimeContext);
            runtimeExecutor = runtimeExecutor.getRollbackExecutor(runtimeContext);
        }
    }

    private void postRollback(RuntimeContext runtimeContext) {

        if (runtimeContext.getProcessStatus() != ProcessStatus.SUCCESS) {
            log.warn("postRollback: ignore while process failed.||runtimeContext={}", runtimeContext);
            return;
        }
        if (runtimeContext.getCurrentNodeInstance() != null) {
            runtimeContext.setSuspendNodeInstance(runtimeContext.getCurrentNodeInstance());
        }

        //update FlowInstancePO to db
        saveNodeInstanceList(runtimeContext, NodeInstanceType.ROLLBACK);

        if (FlowModelUtil.isElementType(runtimeContext.getCurrentNodeModel().getKey(), runtimeContext.getFlowElementMap(), FlowElementType.START_EVENT)) {
            runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.TERMINATED);
            flowInstanceService.updateStatus(runtimeContext.getFlowInstanceId(), FlowInstanceStatus.TERMINATED);
        } else if (runtimeContext.getFlowInstanceStatus() == FlowInstanceStatus.END) {
            runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.RUNNING);
            flowInstanceService.updateStatus(runtimeContext.getFlowInstanceId(), FlowInstanceStatus.RUNNING);
        }
    }

    private void fillRollbackContext(RuntimeContext runtimeContext, FlowNodeInstance nodeInstancePO,
                                     Map<String, InstanceData> instanceDataMap) throws ProcessException {
        runtimeContext.setInstanceDataId(nodeInstancePO.getInstanceDataId());
        runtimeContext.setInstanceDataMap(instanceDataMap);
        runtimeContext.setNodeInstanceList(Lists.newArrayList());
        NodeInstanceBO suspendNodeInstanceBO = buildSuspendNodeInstanceBO(nodeInstancePO);
        runtimeContext.setSuspendNodeInstance(suspendNodeInstanceBO);
        setCurrentFlowModel(runtimeContext);
    }

    private NodeInstanceBO buildSuspendNodeInstanceBO(FlowNodeInstance nodeInstancePO) {
        NodeInstanceBO suspendNodeInstanceBO = new NodeInstanceBO();
        BeanUtils.copyProperties(nodeInstancePO, suspendNodeInstanceBO);
        return suspendNodeInstanceBO;
    }

    private void updateSuspendNodeInstanceBO(NodeInstanceBO suspendNodeInstanceBO, FlowNodeInstance nodeInstancePO, String
            instanceDataId) {
        suspendNodeInstanceBO.setId(nodeInstancePO.getId());
        suspendNodeInstanceBO.setNodeKey(nodeInstancePO.getNodeKey());
        suspendNodeInstanceBO.setStatus(nodeInstancePO.getStatus());
        suspendNodeInstanceBO.setSourceNodeInstanceId(nodeInstancePO.getSourceNodeInstanceId());
        suspendNodeInstanceBO.setSourceNodeKey(nodeInstancePO.getSourceNodeKey());
        suspendNodeInstanceBO.setInstanceDataId(instanceDataId);
    }

    //suspendNodeInstanceBO is necessary
    private void setCurrentFlowModel(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBO suspendNodeInstanceBO = runtimeContext.getSuspendNodeInstance();
        FlowElement currentNodeModel = FlowModelUtil.getFlowElement(runtimeContext.getFlowElementMap(), suspendNodeInstanceBO.getNodeKey());
        if (currentNodeModel == null) {
            log.warn("setCurrentFlowModel failed: cannot get currentNodeModel.||flowInstance={}||flowDeployId={}||nodeKey={}",
                    runtimeContext.getFlowInstanceId(), runtimeContext.getFlowDeployId(), suspendNodeInstanceBO.getNodeKey());
            throw new ProcessException(ErrorEnum.GET_NODE_FAILED);
        }
        runtimeContext.setCurrentNodeModel(currentNodeModel);
    }

    @Override
    protected boolean isCompleted(RuntimeContext runtimeContext) throws ProcessException {
        if (runtimeContext.getFlowInstanceStatus() == FlowInstanceStatus.COMPLETED) {
            return true;
        }
        if (runtimeContext.getFlowInstanceStatus() == FlowInstanceStatus.END) {
            return false;
        }
        NodeInstanceBO suspendNodeInstance = runtimeContext.getSuspendNodeInstance();
        if (suspendNodeInstance == null) {
            log.warn("suspendNodeInstance is null.||runtimeContext={}", runtimeContext);
            return false;
        }

        if (suspendNodeInstance.getStatus() != NodeInstanceStatus.COMPLETED) {
            return false;
        }

        String nodeKey = suspendNodeInstance.getNodeKey();
        Map<String, FlowElement> flowElementMap = runtimeContext.getFlowElementMap();
        if (FlowModelUtil.getFlowElement(flowElementMap, nodeKey).getType() == FlowElementType.END_EVENT) {
            return true;
        }
        return false;
    }

    @Override
    protected RuntimeExecutor getExecuteExecutor(RuntimeContext runtimeContext) throws ProcessException {
        return getElementExecutor(runtimeContext);
    }

    @Override
    protected RuntimeExecutor getRollbackExecutor(RuntimeContext runtimeContext) throws ProcessException {
        return getElementExecutor(runtimeContext);
    }

    private RuntimeExecutor getElementExecutor(RuntimeContext runtimeContext) throws ProcessException {
        //if process completed, return null
        if (isCompleted(runtimeContext)) {
            return null;
        }
        return executorFactory.getElementExecutor(runtimeContext.getCurrentNodeModel());
    }

    ////////////////////////////////////////common////////////////////////////////////////

    private void saveNodeInstanceList(RuntimeContext runtimeContext, int nodeInstanceType) {

        List<NodeInstanceBO> processNodeList = runtimeContext.getNodeInstanceList();

        if (CollectionUtils.isEmpty(processNodeList)) {
            log.warn("saveNodeInstanceList: processNodeList is empty,||flowInstanceId={}||nodeInstanceType={}",
                    runtimeContext.getFlowInstanceId(), nodeInstanceType);
            return;
        }

        List<FlowNodeInstance> nodeInstancePOList = Lists.newArrayList();
        List<FlowNodeInstanceLog> nodeInstanceLogPOList = Lists.newArrayList();

        processNodeList.forEach(nodeInstanceBO -> {
            FlowNodeInstance nodeInstancePO = buildNodeInstancePO(runtimeContext, nodeInstanceBO);
            if (nodeInstancePO != null) {
                nodeInstancePOList.add(nodeInstancePO);

                //build nodeInstance log
                FlowNodeInstanceLog nodeInstanceLogPO = buildNodeInstanceLogPO(nodeInstancePO, nodeInstanceType);
                nodeInstanceLogPOList.add(nodeInstanceLogPO);
            }
        });
        flowNodeInstanceService.saveOrUpdateBatch(nodeInstancePOList);
        flowNodeInstanceLogService.saveBatch(nodeInstanceLogPOList);
    }

    private FlowNodeInstance buildNodeInstancePO(RuntimeContext runtimeContext, NodeInstanceBO nodeInstanceBO) {
        if (runtimeContext.getProcessStatus() == ProcessStatus.FAILED) {
            //set status=FAILED unless it is origin processNodeInstance(suspendNodeInstance)
            if (nodeInstanceBO.getNodeKey().equals(runtimeContext.getSuspendNodeInstance().getNodeKey())) {
                //keep suspendNodeInstance's status while process failed.
                return null;
            }
            nodeInstanceBO.setStatus(NodeInstanceStatus.FAILED);
        }

        FlowNodeInstance nodeInstancePO = new FlowNodeInstance();
        BeanUtils.copyProperties(nodeInstanceBO, nodeInstancePO);
        nodeInstancePO.setFlowInstanceId(runtimeContext.getFlowInstanceId());
        nodeInstancePO.setFlowDeployId(runtimeContext.getFlowDeployId());
        // 待确定租户
        //nodeInstancePO.setTenant(runtimeContext.getTenant());
        nodeInstancePO.setCaller(runtimeContext.getCaller());
        return nodeInstancePO;
    }

    private FlowNodeInstanceLog buildNodeInstanceLogPO(FlowNodeInstance nodeInstancePO, int nodeInstanceType) {
        FlowNodeInstanceLog nodeInstanceLogPO = new FlowNodeInstanceLog();
        BeanUtils.copyProperties(nodeInstancePO, nodeInstanceLogPO);
        nodeInstanceLogPO.setType(nodeInstanceType);
        return nodeInstanceLogPO;
    }

}
