package com.quick.flow.engine.processor;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quick.flow.engine.bo.*;
import com.quick.flow.engine.common.*;
import com.quick.flow.engine.exception.ProcessException;
import com.quick.flow.engine.exception.ReentrantException;
import com.quick.flow.engine.exception.TurboException;
import com.quick.flow.engine.executor.FlowExecutor;
import com.quick.flow.engine.model.FlowElement;
import com.quick.flow.engine.model.InstanceData;
import com.quick.flow.engine.param.CommitTaskParam;
import com.quick.flow.engine.param.RollbackTaskParam;
import com.quick.flow.engine.param.StartProcessParam;
import com.quick.flow.engine.result.*;
import com.quick.flow.engine.service.FlowInstanceServiceBiz;
import com.quick.flow.engine.service.InstanceDataService;
import com.quick.flow.engine.service.NodeInstanceService;
import com.quick.flow.engine.util.FlowModelUtil;
import com.quick.flow.engine.util.InstanceDataUtil;
import com.quick.flow.engine.validator.ParamValidator;
import com.quick.flow.entity.*;
import com.quick.flow.service.IFlowDeploymentService;
import com.quick.flow.service.IFlowInstanceMappingService;
import com.quick.flow.service.IFlowInstanceService;
import com.quick.flow.service.IFlowNodeInstanceService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.*;
@Slf4j
@Component
public class RuntimeProcessor {

    @Resource
    private IFlowDeploymentService flowDeploymentService;

    @Resource
    private IFlowInstanceService flowInstanceService;

    @Resource
    private IFlowNodeInstanceService flowNodeInstanceService;

    @Resource
    private IFlowInstanceMappingService flowInstanceMappingService;

    @Resource
    private FlowExecutor flowExecutor;

    @Resource
    private FlowInstanceServiceBiz flowInstanceServiceBiz;

    @Resource
    private InstanceDataService instanceDataService;

    @Resource
    private NodeInstanceService nodeInstanceService;

    ////////////////////////////////////////startProcess////////////////////////////////////////

    public StartProcessResult startProcess(StartProcessParam startProcessParam) {
        RuntimeContext runtimeContext = null;
        try {
            //1.param validate
            ParamValidator.validate(startProcessParam);

            //2.getFlowInfo
            FlowInfo flowInfo = getFlowInfo(startProcessParam);

            //3.init context for runtime
            runtimeContext = buildStartProcessContext(flowInfo, startProcessParam.getVariables(), startProcessParam.getRuntimeContext());

            //4.process
            flowExecutor.execute(runtimeContext);

            //5.build result
            return buildStartProcessResult(runtimeContext);
        } catch (TurboException e) {
            if (!ErrorEnum.isSuccess(e.getErrNo())) {
                log.warn("startProcess ProcessException.||startProcessParam={}||runtimeContext={}, ",
                    startProcessParam, runtimeContext, e);
            }
            return buildStartProcessResult(runtimeContext, e);
        }
    }

    private FlowInfo getFlowInfo(StartProcessParam startProcessParam) throws ProcessException {
        if (StringUtils.isNotBlank(startProcessParam.getFlowDeployId())) {
            return getFlowInfoByFlowDeployId(startProcessParam.getFlowDeployId());
        } else {
            return getFlowInfoByFlowModuleId(startProcessParam.getFlowModuleId());
        }
    }

    /**
     * Init runtimeContext for startProcess:
     * 1.flowInfo: flowDeployId, flowModuleId, tenantId, flowModel(FlowElementList)
     * 2.variables: inputDataList fr. param
     */
    private RuntimeContext buildStartProcessContext(FlowInfo flowInfo, List<InstanceData> variables, RuntimeContext parentRuntimeContext) {
        return buildRuntimeContext(flowInfo, variables, parentRuntimeContext);
    }

    private StartProcessResult buildStartProcessResult(RuntimeContext runtimeContext) {
        StartProcessResult startProcessResult = new StartProcessResult();
        BeanUtils.copyProperties(runtimeContext, startProcessResult);
        return (StartProcessResult) fillRuntimeResult(startProcessResult, runtimeContext);
    }

    private StartProcessResult buildStartProcessResult(RuntimeContext runtimeContext, TurboException e) {
        StartProcessResult startProcessResult = new StartProcessResult();
        BeanUtils.copyProperties(runtimeContext, startProcessResult);
        return (StartProcessResult) fillRuntimeResult(startProcessResult, runtimeContext, e);
    }

    ////////////////////////////////////////commit////////////////////////////////////////

    public CommitTaskResult commit(CommitTaskParam commitTaskParam) {
        RuntimeContext runtimeContext = null;
        try {
            //1.param validate
            ParamValidator.validate(commitTaskParam);

            //2.get flowInstance
            FlowInstanceBO flowInstanceBO = getFlowInstanceBO(commitTaskParam.getFlowInstanceId());

            //3.check status
            if (flowInstanceBO.getStatus() == FlowInstanceStatus.TERMINATED) {
                log.warn("commit failed: flowInstance has been completed.||commitTaskParam={}", commitTaskParam);
                throw new ProcessException(ErrorEnum.COMMIT_REJECTRD);
            }
            if (flowInstanceBO.getStatus() == FlowInstanceStatus.COMPLETED) {
                log.warn("commit: reentrant process.||commitTaskParam={}", commitTaskParam);
                throw new ReentrantException(ErrorEnum.REENTRANT_WARNING);
            }
            String flowDeployId = flowInstanceBO.getFlowDeployId();

            //4.getFlowInfo
            FlowInfo flowInfo = getFlowInfoByFlowDeployId(flowDeployId);

            //5.init runtimeContext
            runtimeContext = buildCommitContext(commitTaskParam, flowInfo, flowInstanceBO.getStatus());

            //6.process
            flowExecutor.commit(runtimeContext);

            //7.build result
            return buildCommitTaskResult(runtimeContext);
        } catch (TurboException e) {
            if (!ErrorEnum.isSuccess(e.getErrNo())) {
                log.warn("commit ProcessException.||commitTaskParam={}||runtimeContext={}, ", commitTaskParam, runtimeContext, e);
            }
            return buildCommitTaskResult(runtimeContext, e);
        }
    }

    private RuntimeContext buildCommitContext(CommitTaskParam commitTaskParam, FlowInfo flowInfo, int flowInstanceStatus) {
        //1. set flow info
        RuntimeContext runtimeContext = buildRuntimeContext(flowInfo, commitTaskParam.getVariables(), commitTaskParam.getRuntimeContext());

        //2. init flowInstance with flowInstanceId
        runtimeContext.setFlowInstanceId(commitTaskParam.getFlowInstanceId());
        runtimeContext.setFlowInstanceStatus(flowInstanceStatus);

        //3.set suspendNodeInstance stack
        RuntimeContext parentRuntimeContext = runtimeContext.getParentRuntimeContext();
        String realNodeInstanceId = null;
        if (parentRuntimeContext == null) {
            Stack<String> nodeInstanceId2RootStack = flowInstanceServiceBiz.getNodeInstanceIdStack(commitTaskParam.getFlowInstanceId(), commitTaskParam.getTaskInstanceId());
            runtimeContext.setSuspendNodeInstanceStack(nodeInstanceId2RootStack);
            realNodeInstanceId = nodeInstanceId2RootStack.isEmpty() ? commitTaskParam.getTaskInstanceId() : nodeInstanceId2RootStack.pop();
        } else {
            runtimeContext.setSuspendNodeInstanceStack(parentRuntimeContext.getSuspendNodeInstanceStack());
            realNodeInstanceId = commitTaskParam.getTaskInstanceId();
        }

        //4. set suspendNodeInstance with taskInstance in param
        NodeInstanceBO suspendNodeInstance = new NodeInstanceBO();
        suspendNodeInstance.setNodeInstanceId(realNodeInstanceId);
        runtimeContext.setSuspendNodeInstance(suspendNodeInstance);

        //5. set callActivity msg
        runtimeContext.setCallActivityFlowModuleId(commitTaskParam.getCallActivityFlowModuleId());

        return runtimeContext;
    }

    private CommitTaskResult buildCommitTaskResult(RuntimeContext runtimeContext) {
        CommitTaskResult commitTaskResult = new CommitTaskResult();
        return (CommitTaskResult) fillRuntimeResult(commitTaskResult, runtimeContext);
    }

    private CommitTaskResult buildCommitTaskResult(RuntimeContext runtimeContext, TurboException e) {
        CommitTaskResult commitTaskResult = new CommitTaskResult();
        return (CommitTaskResult) fillRuntimeResult(commitTaskResult, runtimeContext, e);
    }

    ////////////////////////////////////////rollback////////////////////////////////////////

    /**
     * Rollback: rollback node process from param.taskInstance to the last taskInstance to suspend
     *
     * @param rollbackTaskParam: flowInstanceId + taskInstanceId(nodeInstanceId)
     * @return rollbackTaskResult: runtimeResult, flowInstanceId + activeTaskInstance(nodeInstanceId,nodeKey,status) + dataMap
     * @throws Exception
     */
    public RollbackTaskResult rollback(RollbackTaskParam rollbackTaskParam) {
        RuntimeContext runtimeContext = null;
        try {
            //1.param validate
            ParamValidator.validate(rollbackTaskParam);

            //2.get flowInstance
            FlowInstanceBO flowInstanceBO = getFlowInstanceBO(rollbackTaskParam.getFlowInstanceId());

            //3.check status
            if ((flowInstanceBO.getStatus() != FlowInstanceStatus.RUNNING) && (flowInstanceBO.getStatus() != FlowInstanceStatus.END)) {
                log.warn("rollback failed: invalid status to rollback.||rollbackTaskParam={}||status={}",
                    rollbackTaskParam, flowInstanceBO.getStatus());
                throw new ProcessException(ErrorEnum.ROLLBACK_REJECTRD);
            }
            String flowDeployId = flowInstanceBO.getFlowDeployId();

            //4.getFlowInfo
            FlowInfo flowInfo = getFlowInfoByFlowDeployId(flowDeployId);

            //5.init runtimeContext
            runtimeContext = buildRollbackContext(rollbackTaskParam, flowInfo, flowInstanceBO.getStatus());

            //6.process
            flowExecutor.rollback(runtimeContext);

            //7.build result
            return buildRollbackTaskResult(runtimeContext);
        } catch (TurboException e) {
            if (!ErrorEnum.isSuccess(e.getErrNo())) {
                log.warn("rollback ProcessException.||rollbackTaskParam={}||runtimeContext={}, ", rollbackTaskParam, runtimeContext, e);
            }
            return buildRollbackTaskResult(runtimeContext, e);
        }
    }

    private RuntimeContext buildRollbackContext(RollbackTaskParam rollbackTaskParam, FlowInfo flowInfo, int flowInstanceStatus) {
        //1. set flow info
        RuntimeContext runtimeContext = buildRuntimeContext(flowInfo);

        //2. init flowInstance with flowInstanceId
        runtimeContext.setFlowInstanceId(rollbackTaskParam.getFlowInstanceId());
        runtimeContext.setFlowInstanceStatus(flowInstanceStatus);

        //3.set suspendNodeInstance stack
        RuntimeContext parentRuntimeContext = rollbackTaskParam.getRuntimeContext();
        String realNodeInstanceId = null;
        if (parentRuntimeContext == null) {
            Stack<String> nodeInstanceId2RootStack = flowInstanceServiceBiz.getNodeInstanceIdStack(rollbackTaskParam.getFlowInstanceId(), rollbackTaskParam.getTaskInstanceId());
            runtimeContext.setSuspendNodeInstanceStack(nodeInstanceId2RootStack);
            realNodeInstanceId = nodeInstanceId2RootStack.isEmpty() ? rollbackTaskParam.getTaskInstanceId() : nodeInstanceId2RootStack.pop();
        } else {
            runtimeContext.setParentRuntimeContext(rollbackTaskParam.getRuntimeContext());
            runtimeContext.setSuspendNodeInstanceStack(rollbackTaskParam.getRuntimeContext().getSuspendNodeInstanceStack());
            realNodeInstanceId = rollbackTaskParam.getTaskInstanceId();
        }

        //3. set suspendNodeInstance with taskInstance in param
        NodeInstanceBO suspendNodeInstance = new NodeInstanceBO();
        suspendNodeInstance.setNodeInstanceId(realNodeInstanceId);
        runtimeContext.setSuspendNodeInstance(suspendNodeInstance);

        return runtimeContext;
    }

    private RollbackTaskResult buildRollbackTaskResult(RuntimeContext runtimeContext) {
        RollbackTaskResult rollbackTaskResult = new RollbackTaskResult();
        return (RollbackTaskResult) fillRuntimeResult(rollbackTaskResult, runtimeContext);
    }

    private RollbackTaskResult buildRollbackTaskResult(RuntimeContext runtimeContext, TurboException e) {
        RollbackTaskResult rollbackTaskResult = new RollbackTaskResult();
        return (RollbackTaskResult) fillRuntimeResult(rollbackTaskResult, runtimeContext, e);
    }

    ////////////////////////////////////////terminate////////////////////////////////////////

    public TerminateResult terminateProcess(String flowInstanceId, boolean effectiveForSubFlowInstance) {
        TerminateResult terminateResult;
        try {
            int flowInstanceStatus;

            FlowInstance flowInstance = flowInstanceService.selectByFlowInstanceId(flowInstanceId);
            if (flowInstance == null) {
                log.warn("terminateProcess failed: cannot find flowInstancePO from db.||flowInstanceId={}", flowInstanceId);
                throw new ProcessException(ErrorEnum.GET_FLOW_INSTANCE_FAILED);
            }

            if (flowInstance.getStatus() == FlowInstanceStatus.COMPLETED) {
                log.warn("terminateProcess: flowInstance is completed.||flowInstanceId={}", flowInstanceId);
                flowInstanceStatus = FlowInstanceStatus.COMPLETED;
            } else {
                flowInstanceService.updateStatus(flowInstance.getFlowInstanceId(), FlowInstanceStatus.TERMINATED);
                flowInstanceStatus = FlowInstanceStatus.TERMINATED;
            }

            if (effectiveForSubFlowInstance) {
                terminateSubFlowInstance(flowInstanceId);
            }

            terminateResult = new TerminateResult(ErrorEnum.SUCCESS);
            terminateResult.setFlowInstanceId(flowInstanceId);
            terminateResult.setStatus(flowInstanceStatus);
        } catch (Exception e) {
            log.error("terminateProcess exception.||flowInstanceId={}, ", flowInstanceId, e);
            terminateResult = new TerminateResult(ErrorEnum.SYSTEM_ERROR);
            terminateResult.setFlowInstanceId(flowInstanceId);
        }
        return terminateResult;
    }

    public void terminateSubFlowInstance(String flowInstanceId) {
        Set<String> allSubFlowInstanceIds = flowInstanceServiceBiz.getAllSubFlowInstanceIds(flowInstanceId);
        for (String subFlowInstanceId : allSubFlowInstanceIds) {
            terminateProcess(subFlowInstanceId, false);
        }
    }

    ////////////////////////////////////////getHistoryUserTaskList////////////////////////////////////////

    public NodeInstanceListResult getHistoryUserTaskList(String flowInstanceId, boolean effectiveForSubFlowInstance) {

        //1.get nodeInstanceList by flowInstanceId order by id desc
        List<FlowNodeInstance> historyNodeInstanceList = getDescHistoryNodeInstanceList(flowInstanceId);

        //2.init result
        NodeInstanceListResult historyListResult = new NodeInstanceListResult(ErrorEnum.SUCCESS);
        historyListResult.setNodeInstanceList(Lists.newArrayList());

        try {

            if (CollectionUtils.isEmpty(historyNodeInstanceList)) {
                log.warn("getHistoryUserTaskList: historyNodeInstanceList is empty.||flowInstanceId={}", flowInstanceId);
                return historyListResult;
            }

            //3.get flow info
            String flowDeployId = historyNodeInstanceList.get(0).getFlowDeployId();
            Map<String, FlowElement> flowElementMap = getFlowElementMap(flowDeployId);

            //4.pick out userTask and build result
            List<NodeInstance> userTaskList = historyListResult.getNodeInstanceList();//empty list

            for (FlowNodeInstance nodeInstancePO : historyNodeInstanceList) {
                //ignore noneffective nodeInstance
                if (!isEffectiveNodeInstance(nodeInstancePO.getStatus())) {
                    continue;
                }

                if (effectiveForSubFlowInstance && isCallActivity(nodeInstancePO.getNodeKey(), flowElementMap)) {
                    //handle subFlowInstance
                    String subFlowInstanceId = getExecuteSubFlowInstanceId(flowInstanceId, nodeInstancePO.getNodeInstanceId());
                    if (StringUtils.isNotBlank(subFlowInstanceId)) {
                        NodeInstanceListResult historyUserTaskList = getHistoryUserTaskList(subFlowInstanceId, true);
                        userTaskList.addAll(historyUserTaskList.getNodeInstanceList());
                    }
                    continue;
                }

                //ignore un-userTask instance
                if (!isUserTask(nodeInstancePO.getNodeKey(), flowElementMap)) {
                    continue;
                }

                //build effective userTask instance
                NodeInstance nodeInstance = new NodeInstance();
                //set instanceId & status
                BeanUtils.copyProperties(nodeInstancePO, nodeInstance);

                //set ElementModel info
                FlowElement flowElement = FlowModelUtil.getFlowElement(flowElementMap, nodeInstancePO.getNodeKey());
                nodeInstance.setModelKey(flowElement.getKey());
                nodeInstance.setModelName(FlowModelUtil.getElementName(flowElement));
                if (MapUtils.isNotEmpty(flowElement.getProperties())) {
                    nodeInstance.setProperties(flowElement.getProperties());
                } else {
                    nodeInstance.setProperties(Maps.newHashMap());
                }
                userTaskList.add(nodeInstance);
            }
        } catch (ProcessException e) {
            historyListResult.setErrCode(e.getErrNo());
            historyListResult.setErrMsg(e.getErrMsg());
        }
        return historyListResult;
    }

    private Map<String, FlowElement> getFlowElementMap(String flowDeployId) throws ProcessException {
        FlowInfo flowInfo = getFlowInfoByFlowDeployId(flowDeployId);
        String flowModel = flowInfo.getFlowModel();
        return FlowModelUtil.getFlowElementMap(flowModel);
    }

    private boolean isEffectiveNodeInstance(int status) {
        return status == NodeInstanceStatus.COMPLETED || status == NodeInstanceStatus.ACTIVE;
    }

    private boolean isUserTask(String nodeKey, Map<String, FlowElement> flowElementMap) throws ProcessException {
        int type = getNodeType(nodeKey, flowElementMap);
        return type == FlowElementType.USER_TASK;
    }

    private int getNodeType(String nodeKey, Map<String, FlowElement> flowElementMap) throws ProcessException {
        if (!flowElementMap.containsKey(nodeKey)) {
            log.warn("isUserTask: invalid nodeKey which is not in flowElementMap.||nodeKey={}||flowElementMap={}",
                nodeKey, flowElementMap);
            throw new ProcessException(ErrorEnum.GET_NODE_FAILED);
        }
        FlowElement flowElement = flowElementMap.get(nodeKey);
        return flowElement.getType();
    }

    private boolean isCallActivity(String nodeKey, Map<String, FlowElement> flowElementMap) throws ProcessException {
        int type = getNodeType(nodeKey, flowElementMap);
        return type == FlowElementType.CALL_ACTIVITY;
    }

    ////////////////////////////////////////getHistoryElementList////////////////////////////////////////

    public ElementInstanceListResult getHistoryElementList(String flowInstanceId, boolean effectiveForSubFlowInstance) {
        //1.getHistoryNodeList
        List<FlowNodeInstance> historyNodeInstanceList = getHistoryNodeInstanceList(flowInstanceId);

        //2.init
        ElementInstanceListResult elementInstanceListResult = new ElementInstanceListResult(ErrorEnum.SUCCESS);
        elementInstanceListResult.setElementInstanceList(Lists.newArrayList());

        try {
            if (CollectionUtils.isEmpty(historyNodeInstanceList)) {
                log.warn("getHistoryElementList: historyNodeInstanceList is empty.||flowInstanceId={}", flowInstanceId);
                return elementInstanceListResult;
            }

            //3.get flow info
            String flowDeployId = historyNodeInstanceList.get(0).getFlowDeployId();
            Map<String, FlowElement> flowElementMap = getFlowElementMap(flowDeployId);

            //4.calculate elementInstanceMap: key=elementKey, value(lasted)=ElementInstance(elementKey, status)
            List<ElementInstance> elementInstanceList = elementInstanceListResult.getElementInstanceList();
            for (FlowNodeInstance nodeInstancePO : historyNodeInstanceList) {
                String nodeKey = nodeInstancePO.getNodeKey();
                String sourceNodeKey = nodeInstancePO.getSourceNodeKey();
                int nodeStatus = nodeInstancePO.getStatus();
                String nodeInstanceId = nodeInstancePO.getNodeInstanceId();
                String instanceDataId = nodeInstancePO.getInstanceDataId();
                //4.1 build the source sequenceFlow instance
                if (StringUtils.isNotBlank(sourceNodeKey)) {
                    FlowElement sourceFlowElement = FlowModelUtil.getSequenceFlow(flowElementMap, sourceNodeKey, nodeKey);
                    if (sourceFlowElement == null) {
                        log.error("getHistoryElementList failed: sourceFlowElement is null."
                            + "||nodeKey={}||sourceNodeKey={}||flowElementMap={}", nodeKey, sourceNodeKey, flowElementMap);
                        throw new ProcessException(ErrorEnum.MODEL_UNKNOWN_ELEMENT_KEY);
                    }

                    //build ElementInstance
                    int sourceSequenceFlowStatus = nodeStatus;
                    if (nodeStatus == NodeInstanceStatus.ACTIVE) {
                        sourceSequenceFlowStatus = NodeInstanceStatus.COMPLETED;
                    }
                    ElementInstance sequenceFlowInstance = new ElementInstance(sourceFlowElement.getKey(), sourceSequenceFlowStatus, null, null);
                    elementInstanceList.add(sequenceFlowInstance);
                }

                //4.2 build nodeInstance
                ElementInstance nodeInstance = new ElementInstance(nodeKey, nodeStatus, nodeInstanceId, instanceDataId);
                elementInstanceList.add(nodeInstance);

                //4.3 handle callActivity
                if (!FlowModelUtil.isElementType(nodeKey, flowElementMap, FlowElementType.CALL_ACTIVITY)) {
                    continue;
                }
                if (!effectiveForSubFlowInstance) {
                    continue;
                }
                List<FlowInstanceMapping> flowInstanceMappingPOS = flowInstanceMappingService.selectFlowInstanceMappingList(flowInstanceId, nodeInstanceId);
                List<ElementInstance> subElementInstanceList = new ArrayList<>();
                nodeInstance.setSubElementInstanceList(subElementInstanceList);
                for (FlowInstanceMapping flowInstanceMappingPO : flowInstanceMappingPOS) {
                    ElementInstanceListResult subElementInstanceListResult = getHistoryElementList(flowInstanceMappingPO.getSubFlowInstanceId(), effectiveForSubFlowInstance);
                    subElementInstanceList.addAll(subElementInstanceListResult.getElementInstanceList());
                }
            }
        } catch (ProcessException e) {
            elementInstanceListResult.setErrCode(e.getErrNo());
            elementInstanceListResult.setErrMsg(e.getErrMsg());
        }
        return elementInstanceListResult;
    }

    private String getExecuteSubFlowInstanceId(String flowInstanceId, String nodeInstanceId) {
        List<FlowInstanceMapping> flowInstanceMappingPOList = flowInstanceMappingService.selectFlowInstanceMappingList(flowInstanceId, nodeInstanceId);
        if (CollectionUtils.isEmpty(flowInstanceMappingPOList)) {
            return null;
        }
        for (FlowInstanceMapping flowInstanceMappingPO : flowInstanceMappingPOList) {
            if (FlowInstanceMappingType.EXECUTE == flowInstanceMappingPO.getStatus()) {
                return flowInstanceMappingPO.getSubFlowInstanceId();
            }
        }
        return flowInstanceMappingPOList.get(0).getSubFlowInstanceId();
    }

    private List<FlowNodeInstance> getHistoryNodeInstanceList(String flowInstanceId) {
        return flowNodeInstanceService.selectByFlowInstanceId(flowInstanceId);
    }

    private List<FlowNodeInstance> getDescHistoryNodeInstanceList(String flowInstanceId) {
        return flowNodeInstanceService.selectDescByFlowInstanceId(flowInstanceId);
    }

    public NodeInstanceResult getNodeInstance(String flowInstanceId, String nodeInstanceId, boolean effectiveForSubFlowInstance) {
        NodeInstanceResult nodeInstanceResult = new NodeInstanceResult();
        try {
            FlowNodeInstance nodeInstancePO = nodeInstanceService.selectByNodeInstanceId(flowInstanceId, nodeInstanceId, effectiveForSubFlowInstance);
            String flowDeployId = nodeInstancePO.getFlowDeployId();
            Map<String, FlowElement> flowElementMap = getFlowElementMap(flowDeployId);
            NodeInstance nodeInstance = new NodeInstance();
            BeanUtils.copyProperties(nodeInstancePO, nodeInstance);
            FlowElement flowElement = FlowModelUtil.getFlowElement(flowElementMap, nodeInstancePO.getNodeKey());
            if (flowElement.getType() == FlowElementType.CALL_ACTIVITY) {
                List<FlowInstanceMapping> flowInstanceMappingPOList = flowInstanceMappingService.selectFlowInstanceMappingList(flowInstanceId, nodeInstanceId);
                List<String> subFlowInstanceIdList = new ArrayList<>();
                for (FlowInstanceMapping flowInstanceMappingPO : flowInstanceMappingPOList) {
                    subFlowInstanceIdList.add(flowInstanceMappingPO.getSubFlowInstanceId());
                }
                nodeInstance.setSubFlowInstanceIdList(subFlowInstanceIdList);
            }
            nodeInstance.setModelKey(flowElement.getKey());
            nodeInstance.setModelName(FlowModelUtil.getElementName(flowElement));
            if (MapUtils.isNotEmpty(flowElement.getProperties())) {
                nodeInstance.setProperties(flowElement.getProperties());
            } else {
                nodeInstance.setProperties(Maps.newHashMap());
            }
            nodeInstanceResult.setNodeInstance(nodeInstance);
            nodeInstanceResult.setErrCode(ErrorEnum.SUCCESS.getErrNo());
            nodeInstanceResult.setErrMsg(ErrorEnum.SUCCESS.getErrMsg());
        } catch (ProcessException e) {
            nodeInstanceResult.setErrCode(e.getErrNo());
            nodeInstanceResult.setErrMsg(e.getErrMsg());
        }
        return nodeInstanceResult;
    }

    ////////////////////////////////////////getInstanceData////////////////////////////////////////
    public InstanceDataListResult getInstanceData(String flowInstanceId, boolean effectiveForSubFlowInstance) {
        FlowInstanceData instanceDataPO = instanceDataService.select(flowInstanceId, effectiveForSubFlowInstance);
        return packageInstanceDataResult(instanceDataPO);
    }

    public InstanceDataListResult getInstanceData(String flowInstanceId, String instanceDataId, boolean effectiveForSubFlowInstance) {
        FlowInstanceData instanceDataPO = instanceDataService.select(flowInstanceId, instanceDataId, effectiveForSubFlowInstance);
        return packageInstanceDataResult(instanceDataPO);
    }

    public InstanceDataListResult packageInstanceDataResult(FlowInstanceData instanceDataPO) {
        TypeReference<List<InstanceData>> typeReference = new TypeReference<List<InstanceData>>() {
        };
        List<InstanceData> instanceDataList = JSONObject.parseObject(instanceDataPO.getInstanceData(), typeReference);
        if (CollectionUtils.isEmpty(instanceDataList)) {
            instanceDataList = Lists.newArrayList();
        }

        InstanceDataListResult instanceDataListResult = new InstanceDataListResult(ErrorEnum.SUCCESS);
        instanceDataListResult.setVariables(instanceDataList);
        return instanceDataListResult;
    }


    public FlowInstanceResult getFlowInstance(String flowInstanceId) {
        FlowInstanceResult flowInstanceResult = new FlowInstanceResult();
        try {
            FlowInstanceBO flowInstanceBO = getFlowInstanceBO(flowInstanceId);
            flowInstanceResult.setFlowInstanceBO(flowInstanceBO);
        } catch (ProcessException e) {
            flowInstanceResult.setErrCode(e.getErrNo());
            flowInstanceResult.setErrMsg(e.getErrMsg());
        }
        return flowInstanceResult;
    }


    ////////////////////////////////////////common////////////////////////////////////////

    private FlowInfo getFlowInfoByFlowDeployId(String flowDeployId) throws ProcessException {

        FlowDeployment flowDeployment = flowDeploymentService.selectByFlowDeployId(flowDeployId);
        if (flowDeployment == null) {
            log.warn("getFlowInfoByFlowDeployId failed.||flowDeployId={}", flowDeployId);
            throw new ProcessException(ErrorEnum.GET_FLOW_DEPLOYMENT_FAILED);
        }
        FlowInfo flowInfo = new FlowInfo();
        BeanUtils.copyProperties(flowDeployment, flowInfo);

        return flowInfo;
    }

    private FlowInfo getFlowInfoByFlowModuleId(String flowModuleId) throws ProcessException {
        //get from db directly
        FlowDeployment flowDeployment = flowDeploymentService.selectByFlowModuleId(flowModuleId);
        if (flowDeployment == null) {
            log.warn("getFlowInfoByFlowModuleId failed.||flowModuleId={}", flowModuleId);
            throw new ProcessException(ErrorEnum.GET_FLOW_DEPLOYMENT_FAILED);
        }

        FlowInfo flowInfo = new FlowInfo();
        BeanUtils.copyProperties(flowDeployment, flowInfo);

        return flowInfo;
    }

    private FlowInstanceBO getFlowInstanceBO(String flowInstanceId) throws ProcessException {
        //get from db
        FlowInstance flowInstance = flowInstanceService.selectByFlowInstanceId(flowInstanceId);
        if (flowInstance == null) {
            log.warn("getFlowInstancePO failed: cannot find flowInstancePO from db.||flowInstanceId={}", flowInstanceId);
            throw new ProcessException(ErrorEnum.GET_FLOW_INSTANCE_FAILED);
        }
        FlowInstanceBO flowInstanceBO = new FlowInstanceBO();
        BeanUtils.copyProperties(flowInstance, flowInstanceBO);

        return flowInstanceBO;
    }

    private RuntimeContext buildRuntimeContext(FlowInfo flowInfo) {
        RuntimeContext runtimeContext = new RuntimeContext();
        BeanUtils.copyProperties(flowInfo, runtimeContext);
        runtimeContext.setFlowElementMap(FlowModelUtil.getFlowElementMap(flowInfo.getFlowModel()));
        return runtimeContext;
    }

    private RuntimeContext buildRuntimeContext(FlowInfo flowInfo, List<InstanceData> variables, RuntimeContext parentRuntimeContext) {
        RuntimeContext runtimeContext = buildRuntimeContext(flowInfo);
        Map<String, InstanceData> instanceDataMap = InstanceDataUtil.getInstanceDataMap(variables);
        runtimeContext.setInstanceDataMap(instanceDataMap);
        runtimeContext.setParentRuntimeContext(parentRuntimeContext);
        return runtimeContext;
    }

    private RuntimeResult fillRuntimeResult(RuntimeResult runtimeResult, RuntimeContext runtimeContext) {
        if (runtimeContext.getProcessStatus() == ProcessStatus.SUCCESS) {
            return fillRuntimeResult(runtimeResult, runtimeContext, ErrorEnum.SUCCESS);
        }
        return fillRuntimeResult(runtimeResult, runtimeContext, ErrorEnum.FAILED);
    }

    private RuntimeResult fillRuntimeResult(RuntimeResult runtimeResult, RuntimeContext runtimeContext, ErrorEnum errorEnum) {
        return fillRuntimeResult(runtimeResult, runtimeContext, errorEnum.getErrNo(), errorEnum.getErrMsg());
    }

    private RuntimeResult fillRuntimeResult(RuntimeResult runtimeResult, RuntimeContext runtimeContext, TurboException e) {
        return fillRuntimeResult(runtimeResult, runtimeContext, e.getErrNo(), e.getErrMsg());
    }

    private RuntimeResult fillRuntimeResult(RuntimeResult runtimeResult, RuntimeContext runtimeContext, int errNo, String errMsg) {
        runtimeResult.setErrCode(errNo);
        runtimeResult.setErrMsg(errMsg);

        if (runtimeContext != null) {
            runtimeResult.setFlowInstanceId(runtimeContext.getFlowInstanceId());
            runtimeResult.setStatus(runtimeContext.getFlowInstanceStatus());
            runtimeResult.setActiveTaskInstance(buildActiveTaskInstance(runtimeContext.getSuspendNodeInstance(), runtimeContext));
            runtimeResult.setVariables(InstanceDataUtil.getInstanceDataList(runtimeContext.getInstanceDataMap()));
        }
        return runtimeResult;
    }

    private NodeInstance buildActiveTaskInstance(NodeInstanceBO nodeInstanceBO, RuntimeContext runtimeContext) {
        NodeInstance activeNodeInstance = new NodeInstance();
        BeanUtils.copyProperties(nodeInstanceBO, activeNodeInstance);
        activeNodeInstance.setModelKey(nodeInstanceBO.getNodeKey());
        FlowElement flowElement = runtimeContext.getFlowElementMap().get(nodeInstanceBO.getNodeKey());
        activeNodeInstance.setModelName(FlowModelUtil.getElementName(flowElement));
        activeNodeInstance.setProperties(flowElement.getProperties());
        activeNodeInstance.setFlowElementType(flowElement.getType());
        activeNodeInstance.setSubNodeResultList(runtimeContext.getCallActivityRuntimeResultList());


        return activeNodeInstance;
    }

    public void checkIsSubFlowInstance(String flowInstanceId) {
        FlowInstance flowInstance = flowInstanceService.selectByFlowInstanceId(flowInstanceId);
        if (flowInstance == null) {
            log.warn("checkIsSubFlowInstance failed: cannot find flowInstancePO from db.||flowInstanceId={}", flowInstanceId);
            throw new RuntimeException(ErrorEnum.GET_FLOW_INSTANCE_FAILED.getErrMsg());
        }
        if (StringUtils.isNotBlank(flowInstance.getParentFlowInstanceId())) {
            log.error("checkIsSubFlowInstance failed: don't receive sub-processes.||flowInstanceId={}", flowInstanceId);
            throw new RuntimeException(ErrorEnum.NO_RECEIVE_SUB_FLOW_INSTANCE.getErrMsg());
        }
    }

}
