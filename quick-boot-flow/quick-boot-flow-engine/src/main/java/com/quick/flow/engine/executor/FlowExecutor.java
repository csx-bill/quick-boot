package com.quick.flow.engine.executor;

import com.quick.flow.engine.bo.NodeInstanceBO;
import com.quick.flow.engine.common.*;
import com.quick.flow.engine.dao.ProcessInstanceDAO;
import com.quick.flow.engine.entity.FlowInstance;
import com.quick.flow.engine.entity.FlowInstanceData;
import com.quick.flow.engine.entity.FlowNodeInstanceLog;
import com.quick.flow.engine.entity.FlowNodeInstance;
import com.quick.flow.engine.exception.ProcessException;
import com.quick.flow.engine.exception.ReentrantException;
import com.quick.flow.engine.model.FlowElement;
import com.quick.flow.engine.model.InstanceData;
import com.quick.flow.engine.util.FlowModelUtil;
import com.quick.flow.engine.util.InstanceDataUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FlowExecutor extends RuntimeExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowExecutor.class);

    @Resource
    private ProcessInstanceDAO processInstanceDAO;

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
        FlowInstance flowInstance = saveFlowInstance(runtimeContext);

        //2.save InstanceDataPO into db
        String instanceDataId = saveInstanceData(flowInstance, runtimeContext.getInstanceDataMap());

        //3.update runtimeContext
        fillExecuteContext(runtimeContext, flowInstance.getFlowInstanceId(), instanceDataId);
    }

    private FlowInstance saveFlowInstance(RuntimeContext runtimeContext) throws ProcessException {
        FlowInstance flowInstance = buildFlowInstancePO(runtimeContext);
        int result = processInstanceDAO.insert(flowInstance);
        if (result == 1) {
            return flowInstance;
        }
        LOGGER.warn("saveFlowInstancePO: insert failed.||flowInstancePO={}", flowInstance);
        throw new ProcessException(ErrorEnum.SAVE_FLOW_INSTANCE_FAILED);
    }

    private FlowInstance buildFlowInstancePO(RuntimeContext runtimeContext) {
        FlowInstance flowInstance = new FlowInstance();
        // copy flow info
        BeanUtils.copyProperties(runtimeContext, flowInstance);
        // generate flowInstanceId
        flowInstance.setFlowInstanceId(genId());
        RuntimeContext parentRuntimeContext = runtimeContext.getParentRuntimeContext();
        if (parentRuntimeContext != null) {
            flowInstance.setParentFlowInstanceId(parentRuntimeContext.getFlowInstanceId());
        }
        flowInstance.setStatus(FlowInstanceStatus.RUNNING);
//        Date currentTime = new Date();
//        flowInstancePO.setCreateTime(currentTime);
//        flowInstancePO.setModifyTime(currentTime);
        return flowInstance;
    }

    private String saveInstanceData(FlowInstance flowInstance, Map<String, InstanceData> instanceDataMap) throws ProcessException {
        if (MapUtils.isEmpty(instanceDataMap)) {
            return StringUtils.EMPTY;
        }

        FlowInstanceData flowInstanceData = buildInstanceDataPO(flowInstance, instanceDataMap);
        int result = instanceDataDAO.insert(flowInstanceData);
        if (result == 1) {
            return flowInstanceData.getInstanceDataId();
        }

        LOGGER.warn("saveInstanceDataPO: insert failed.||instanceDataPO={}", flowInstanceData);
        throw new ProcessException(ErrorEnum.SAVE_INSTANCE_DATA_FAILED);
    }

    private FlowInstanceData buildInstanceDataPO(FlowInstance flowInstance, Map<String, InstanceData> instanceDataMap) {
        FlowInstanceData flowInstanceData = new FlowInstanceData();
        // copy flow info & flowInstanceId
        BeanUtils.copyProperties(flowInstance, flowInstanceData);

        // generate instanceDataId
        flowInstanceData.setInstanceDataId(genId());
        flowInstanceData.setInstanceData(InstanceDataUtil.getInstanceDataListStr(instanceDataMap));

        flowInstanceData.setNodeInstanceId(StringUtils.EMPTY);
        flowInstanceData.setNodeKey(StringUtils.EMPTY);
        //instanceDataPO.setCreateTime(new Date());
        flowInstanceData.setType(InstanceDataType.INIT);
        return flowInstanceData;
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
            LOGGER.warn("fillExecuteContext failed: cannot get startEvent.||flowInstance={}||flowDeployId={}",
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
                processInstanceDAO.updateStatus(runtimeContext.getFlowInstanceId(), FlowInstanceStatus.END);
                runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.END);
            } else {
                processInstanceDAO.updateStatus(runtimeContext.getFlowInstanceId(), FlowInstanceStatus.COMPLETED);
                runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.COMPLETED);
            }
            LOGGER.info("postExecute: flowInstance process completely.||flowInstanceId={}", runtimeContext.getFlowInstanceId());
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
        FlowNodeInstance flowNodeInstance = nodeInstanceDAO.selectByNodeInstanceId(flowInstanceId, nodeInstanceId);
        if (flowNodeInstance == null) {
            LOGGER.warn("preCommit failed: cannot find nodeInstancePO from db.||flowInstanceId={}||nodeInstanceId={}",
                    flowInstanceId, nodeInstanceId);
            throw new ProcessException(ErrorEnum.GET_NODE_INSTANCE_FAILED);
        }

        //unexpected: flowInstance is completed
        if (isCompleted(runtimeContext)) {
            LOGGER.warn("preExecute warning: reentrant process. FlowInstance has been processed completely.||runtimeContext={}", runtimeContext);
            runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.COMPLETED);
            suspendNodeInstance.setId(flowNodeInstance.getId());
            suspendNodeInstance.setNodeKey(flowNodeInstance.getNodeKey());
            suspendNodeInstance.setSourceNodeInstanceId(flowNodeInstance.getSourceNodeInstanceId());
            suspendNodeInstance.setSourceNodeKey(flowNodeInstance.getSourceNodeKey());
            suspendNodeInstance.setInstanceDataId(flowNodeInstance.getInstanceDataId());
            suspendNodeInstance.setStatus(flowNodeInstance.getStatus());
            throw new ReentrantException(ErrorEnum.REENTRANT_WARNING);
        }
        Map<String, InstanceData> instanceDataMap;
        String instanceDataId = flowNodeInstance.getInstanceDataId();
        if (StringUtils.isBlank(instanceDataId)) {
            instanceDataMap = Maps.newHashMap();
        } else {
            FlowInstanceData flowInstanceData = instanceDataDAO.select(flowInstanceId, instanceDataId);
            if (flowInstanceData == null) {
                LOGGER.warn("preCommit failed: cannot find instanceDataPO from db." +
                    "||flowInstanceId={}||instanceDataId={}", flowInstanceId, instanceDataId);
                throw new ProcessException(ErrorEnum.GET_INSTANCE_DATA_FAILED);
            }
            instanceDataMap = InstanceDataUtil.getInstanceDataMap(flowInstanceData.getInstanceData());
        }

        //2.merge data while commitDataMap is not empty
        Map<String, InstanceData> commitDataMap = runtimeContext.getInstanceDataMap();
        boolean isCallActivityNode = FlowModelUtil.isElementType(flowNodeInstance.getNodeKey(), runtimeContext.getFlowElementMap(), FlowElementType.CALL_ACTIVITY);
        if (isCallActivityNode) {
            // commit callActivity not allow merge data
            instanceDataMap = commitDataMap;
        } else if (MapUtils.isNotEmpty(commitDataMap)) {
            instanceDataId = genId();
            instanceDataMap.putAll(commitDataMap);

            FlowInstanceData commitFlowInstanceData = buildCommitInstanceData(runtimeContext, nodeInstanceId,
                flowNodeInstance.getNodeKey(), instanceDataId, instanceDataMap);
            instanceDataDAO.insert(commitFlowInstanceData);
        }

        //3.update runtimeContext
        fillCommitContext(runtimeContext, flowNodeInstance, instanceDataId, instanceDataMap);
    }

    private FlowInstanceData buildCommitInstanceData(RuntimeContext runtimeContext, String nodeInstanceId, String nodeKey,
                                                     String newInstanceDataId, Map<String, InstanceData> instanceDataMap) {
        FlowInstanceData flowInstanceData = new FlowInstanceData();
        BeanUtils.copyProperties(runtimeContext, flowInstanceData);

        flowInstanceData.setNodeInstanceId(nodeInstanceId);
        flowInstanceData.setNodeKey(nodeKey);
        flowInstanceData.setType(InstanceDataType.COMMIT);
        //instanceDataPO.setCreateTime(new Date());

        flowInstanceData.setInstanceDataId(newInstanceDataId);
        flowInstanceData.setInstanceData(InstanceDataUtil.getInstanceDataListStr(instanceDataMap));

        return flowInstanceData;
    }

    private void fillCommitContext(RuntimeContext runtimeContext, FlowNodeInstance flowNodeInstance, String instanceDataId,
                                   Map<String, InstanceData> instanceDataMap) throws ProcessException {

        runtimeContext.setInstanceDataId(instanceDataId);
        runtimeContext.setInstanceDataMap(instanceDataMap);

        updateSuspendNodeInstanceBO(runtimeContext.getSuspendNodeInstance(), flowNodeInstance, instanceDataId);

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
                processInstanceDAO.updateStatus(runtimeContext.getFlowInstanceId(), FlowInstanceStatus.END);
                runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.END);
            } else {
                processInstanceDAO.updateStatus(runtimeContext.getFlowInstanceId(), FlowInstanceStatus.COMPLETED);
                runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.COMPLETED);
            }

            LOGGER.info("postCommit: flowInstance process completely.||flowInstanceId={}", runtimeContext.getFlowInstanceId());
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
        FlowNodeInstance rollbackFlowNodeInstance = getActiveNodeForRollback(flowInstanceId, suspendNodeInstanceId,
            runtimeContext.getFlowElementMap());
        if (rollbackFlowNodeInstance == null) {
            LOGGER.warn("preRollback failed: cannot rollback.||runtimeContext={}", runtimeContext);
            throw new ProcessException(ErrorEnum.ROLLBACK_FAILED);
        }

        //2.check status: flowInstance is completed
        if (isCompleted(runtimeContext)) {
            LOGGER.warn("invalid preRollback: FlowInstance has been processed completely."
                + "||flowInstanceId={}||flowDeployId={}", flowInstanceId, runtimeContext.getFlowDeployId());
            NodeInstanceBO suspendNodeInstance = new NodeInstanceBO();
            BeanUtils.copyProperties(rollbackFlowNodeInstance, suspendNodeInstance);
            runtimeContext.setSuspendNodeInstance(suspendNodeInstance);
            runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.COMPLETED);
            throw new ProcessException(ErrorEnum.ROLLBACK_FAILED);
        }

        //3.get instanceData
        String instanceDataId = rollbackFlowNodeInstance.getInstanceDataId();
        Map<String, InstanceData> instanceDataMap;
        if (StringUtils.isBlank(instanceDataId)) {
            instanceDataMap = Maps.newHashMap();
        } else {
            FlowInstanceData flowInstanceData = instanceDataDAO.select(flowInstanceId, instanceDataId);
            if (flowInstanceData == null) {
                LOGGER.warn("preRollback failed: cannot find instanceDataPO from db."
                    + "||flowInstanceId={}||instanceDataId={}", flowInstanceId, instanceDataId);
                throw new ProcessException(ErrorEnum.GET_INSTANCE_DATA_FAILED);
            }
            instanceDataMap = InstanceDataUtil.getInstanceDataMap(flowInstanceData.getInstanceData());
        }

        //4.update runtimeContext
        fillRollbackContext(runtimeContext, rollbackFlowNodeInstance, instanceDataMap);
    }

    // if(canRollback): only the active Node or the lasted completed Node can be rollback
    private FlowNodeInstance getActiveNodeForRollback(String flowInstanceId, String suspendNodeInstanceId,
                                                      Map<String, FlowElement> flowElementMap) {
        List<FlowNodeInstance> flowNodeInstanceList = nodeInstanceDAO.selectDescByFlowInstanceId(flowInstanceId);
        if (CollectionUtils.isEmpty(flowNodeInstanceList)) {
            LOGGER.warn("getActiveNodeForRollback: nodeInstancePOList is empty."
                + "||flowInstanceId={}||suspendNodeInstanceId={}", flowInstanceId, suspendNodeInstanceId);
            return null;
        }

        for (FlowNodeInstance flowNodeInstance : flowNodeInstanceList) {
            int elementType = FlowModelUtil.getElementType(flowNodeInstance.getNodeKey(), flowElementMap);
            if (elementType != FlowElementType.USER_TASK
                && elementType != FlowElementType.END_EVENT
                && elementType != FlowElementType.CALL_ACTIVITY) {
                LOGGER.info("getActiveNodeForRollback: ignore un-userTask or un-endEvent or un-callActivity nodeInstance.||flowInstanceId={}"
                    + "||suspendNodeInstanceId={}||nodeKey={}", flowInstanceId, suspendNodeInstanceId, flowNodeInstance.getNodeKey());
                continue;
            }

            if (flowNodeInstance.getStatus() == NodeInstanceStatus.ACTIVE) {
                if (flowNodeInstance.getNodeInstanceId().equals(suspendNodeInstanceId)) {
                    LOGGER.info("getActiveNodeForRollback: roll back the active Node."
                        + "||flowInstanceId={}||suspendNodeInstanceId={}", flowInstanceId, suspendNodeInstanceId);
                    return flowNodeInstance;
                }
            } else if (flowNodeInstance.getStatus() == NodeInstanceStatus.COMPLETED) {
                if (flowNodeInstance.getNodeInstanceId().equals(suspendNodeInstanceId)) {
                    LOGGER.info("getActiveNodeForRollback: roll back the lasted completed Node."
                            + "||flowInstanceId={}||suspendNodeInstanceId={}||activeNodeInstanceId={}",
                        flowInstanceId, suspendNodeInstanceId, flowNodeInstance);
                    return flowNodeInstance;
                }

                LOGGER.warn("getActiveNodeForRollback: cannot rollback the Node."
                    + "||flowInstanceId={}||suspendNodeInstanceId={}", flowInstanceId, suspendNodeInstanceId);
                return null;
            }
            LOGGER.info("getActiveNodeForRollback: ignore disabled Node instance.||flowInstanceId={}"
                + "||suspendNodeInstanceId={}||status={}", flowInstanceId, suspendNodeInstanceId, flowNodeInstance.getStatus());

        }
        LOGGER.warn("getActiveNodeForRollback: cannot rollback the suspendNodeInstance."
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
            LOGGER.warn("postRollback: ignore while process failed.||runtimeContext={}", runtimeContext);
            return;
        }
        if (runtimeContext.getCurrentNodeInstance() != null) {
            runtimeContext.setSuspendNodeInstance(runtimeContext.getCurrentNodeInstance());
        }

        //update FlowInstancePO to db
        saveNodeInstanceList(runtimeContext, NodeInstanceType.ROLLBACK);

        if (FlowModelUtil.isElementType(runtimeContext.getCurrentNodeModel().getKey(), runtimeContext.getFlowElementMap(), FlowElementType.START_EVENT)) {
            runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.TERMINATED);
            processInstanceDAO.updateStatus(runtimeContext.getFlowInstanceId(), FlowInstanceStatus.TERMINATED);
        } else if (runtimeContext.getFlowInstanceStatus() == FlowInstanceStatus.END) {
            runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.RUNNING);
            processInstanceDAO.updateStatus(runtimeContext.getFlowInstanceId(), FlowInstanceStatus.RUNNING);
        }
    }

    private void fillRollbackContext(RuntimeContext runtimeContext, FlowNodeInstance flowNodeInstance,
                                     Map<String, InstanceData> instanceDataMap) throws ProcessException {
        runtimeContext.setInstanceDataId(flowNodeInstance.getInstanceDataId());
        runtimeContext.setInstanceDataMap(instanceDataMap);
        runtimeContext.setNodeInstanceList(Lists.newArrayList());
        NodeInstanceBO suspendNodeInstanceBO = buildSuspendNodeInstanceBO(flowNodeInstance);
        runtimeContext.setSuspendNodeInstance(suspendNodeInstanceBO);
        setCurrentFlowModel(runtimeContext);
    }

    private NodeInstanceBO buildSuspendNodeInstanceBO(FlowNodeInstance flowNodeInstance) {
        NodeInstanceBO suspendNodeInstanceBO = new NodeInstanceBO();
        BeanUtils.copyProperties(flowNodeInstance, suspendNodeInstanceBO);
        return suspendNodeInstanceBO;
    }

    private void updateSuspendNodeInstanceBO(NodeInstanceBO suspendNodeInstanceBO, FlowNodeInstance flowNodeInstance, String
            instanceDataId) {
        suspendNodeInstanceBO.setId(flowNodeInstance.getId());
        suspendNodeInstanceBO.setNodeKey(flowNodeInstance.getNodeKey());
        suspendNodeInstanceBO.setStatus(flowNodeInstance.getStatus());
        suspendNodeInstanceBO.setSourceNodeInstanceId(flowNodeInstance.getSourceNodeInstanceId());
        suspendNodeInstanceBO.setSourceNodeKey(flowNodeInstance.getSourceNodeKey());
        suspendNodeInstanceBO.setInstanceDataId(instanceDataId);
    }

    //suspendNodeInstanceBO is necessary
    private void setCurrentFlowModel(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBO suspendNodeInstanceBO = runtimeContext.getSuspendNodeInstance();
        FlowElement currentNodeModel = FlowModelUtil.getFlowElement(runtimeContext.getFlowElementMap(), suspendNodeInstanceBO.getNodeKey());
        if (currentNodeModel == null) {
            LOGGER.warn("setCurrentFlowModel failed: cannot get currentNodeModel.||flowInstance={}||flowDeployId={}||nodeKey={}",
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
            LOGGER.warn("suspendNodeInstance is null.||runtimeContext={}", runtimeContext);
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
            LOGGER.warn("saveNodeInstanceList: processNodeList is empty,||flowInstanceId={}||nodeInstanceType={}",
                    runtimeContext.getFlowInstanceId(), nodeInstanceType);
            return;
        }

        List<FlowNodeInstance> flowNodeInstanceList = Lists.newArrayList();
        List<FlowNodeInstanceLog> flowNodeInstanceLogList = Lists.newArrayList();

        processNodeList.forEach(nodeInstanceBO -> {
            FlowNodeInstance flowNodeInstance = buildNodeInstancePO(runtimeContext, nodeInstanceBO);
            if (flowNodeInstance != null) {
                flowNodeInstanceList.add(flowNodeInstance);

                //build nodeInstance log
                FlowNodeInstanceLog flowNodeInstanceLog = buildNodeInstanceLogPO(flowNodeInstance, nodeInstanceType);
                flowNodeInstanceLogList.add(flowNodeInstanceLog);
            }
        });
        nodeInstanceDAO.insertOrUpdateList(flowNodeInstanceList);
        nodeInstanceLogDAO.insertList(flowNodeInstanceLogList);
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

        FlowNodeInstance flowNodeInstance = new FlowNodeInstance();
        BeanUtils.copyProperties(nodeInstanceBO, flowNodeInstance);
        flowNodeInstance.setFlowInstanceId(runtimeContext.getFlowInstanceId());
        flowNodeInstance.setFlowDeployId(runtimeContext.getFlowDeployId());
        flowNodeInstance.setTenantId(runtimeContext.getTenant());
        return flowNodeInstance;
    }

    private FlowNodeInstanceLog buildNodeInstanceLogPO(FlowNodeInstance flowNodeInstance, int nodeInstanceType) {
        FlowNodeInstanceLog flowNodeInstanceLog = new FlowNodeInstanceLog();
        BeanUtils.copyProperties(flowNodeInstance, flowNodeInstanceLog);
        flowNodeInstanceLog.setType(nodeInstanceType);
        return flowNodeInstanceLog;
    }

}
