package com.quick.flow.engine.executor;

import com.quick.flow.engine.bo.NodeInstanceBO;
import com.quick.flow.engine.common.InstanceDataType;
import com.quick.flow.engine.common.NodeInstanceStatus;
import com.quick.flow.engine.common.RuntimeContext;
import com.quick.flow.engine.entity.FlowInstanceData;
import com.quick.flow.engine.exception.ProcessException;
import com.quick.flow.engine.model.FlowElement;
import com.quick.flow.engine.model.InstanceData;
import com.quick.flow.engine.spi.HookService;
import com.quick.flow.engine.util.FlowModelUtil;
import com.quick.flow.engine.util.InstanceDataUtil;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExclusiveGatewayExecutor extends ElementExecutor implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExclusiveGatewayExecutor.class);

    @Resource
    private ApplicationContext applicationContext;

    private List<HookService> hookServices;

    /**
     * Update data map: invoke hook service to update data map
     * You can implement HookService and all implementations of 'HookService' will be executed.
     * Param: one of flowElement's properties
     */
    @Override
    protected void doExecute(RuntimeContext runtimeContext) throws ProcessException {
        // 1.get hook param
        FlowElement flowElement = runtimeContext.getCurrentNodeModel();
        String hookInfoParam = FlowModelUtil.getHookInfos(flowElement);

        // 2.ignore while properties is empty
        if (StringUtils.isBlank(hookInfoParam)) {
            return;
        }

        // 3.invoke hook and get data result
        Map<String, InstanceData> hookInfoValueMap = getHookInfoValueMap(runtimeContext.getFlowInstanceId(), hookInfoParam, runtimeContext.getCurrentNodeInstance().getNodeKey(), runtimeContext.getCurrentNodeInstance().getNodeInstanceId());
        LOGGER.info("doExecute getHookInfoValueMap.||hookInfoValueMap={}", hookInfoValueMap);
        if (MapUtils.isEmpty(hookInfoValueMap)) {
            LOGGER.warn("doExecute: hookInfoValueMap is empty.||flowInstanceId={}||hookInfoParam={}||nodeKey={}",
                runtimeContext.getFlowInstanceId(), hookInfoParam, flowElement.getKey());
            return;
        }

        // 4.merge data to current dataMap
        Map<String, InstanceData> dataMap = runtimeContext.getInstanceDataMap();
        dataMap.putAll(hookInfoValueMap);

        // 5.save data
        if (MapUtils.isNotEmpty(dataMap)) {
            String instanceDataId = saveInstanceDataPO(runtimeContext);
            runtimeContext.setInstanceDataId(instanceDataId);
        }
    }

    private Map<String, InstanceData> getHookInfoValueMap(String flowInstanceId, String hookInfoParam, String nodeKey, String nodeInstanceId) {
        List<InstanceData> dataList = Lists.newArrayList();
        for (HookService service : hookServices) {
            try {
                List<InstanceData> list = service.invoke(flowInstanceId, hookInfoParam, nodeKey, nodeInstanceId);
                if (CollectionUtils.isEmpty(list)) {
                    LOGGER.warn("hook service invoke result is empty, serviceName={}, flowInstanceId={}, hookInfoParam={}",
                        service.getClass().getName(), flowInstanceId, hookInfoParam);
                }
                dataList.addAll(list);
            } catch (Exception e) {
                LOGGER.warn("hook service invoke fail, serviceName={}, flowInstanceId={}, hookInfoParam={}",
                    service.getClass().getName(), flowInstanceId, hookInfoParam);
            }
        }
        return InstanceDataUtil.getInstanceDataMap(dataList);
    }

    private String saveInstanceDataPO(RuntimeContext runtimeContext) {
        String instanceDataId = genId();
        FlowInstanceData flowInstanceData = buildHookInstanceData(instanceDataId, runtimeContext);
        instanceDataDAO.insert(flowInstanceData);
        return instanceDataId;
    }

    private FlowInstanceData buildHookInstanceData(String instanceDataId, RuntimeContext runtimeContext) {
        FlowInstanceData flowInstanceData = new FlowInstanceData();
        BeanUtils.copyProperties(runtimeContext, flowInstanceData);
        flowInstanceData.setInstanceDataId(instanceDataId);
        flowInstanceData.setInstanceData(InstanceDataUtil.getInstanceDataListStr(runtimeContext.getInstanceDataMap()));
        flowInstanceData.setNodeInstanceId(runtimeContext.getCurrentNodeInstance().getNodeInstanceId());
        flowInstanceData.setNodeKey(runtimeContext.getCurrentNodeModel().getKey());
        flowInstanceData.setType(InstanceDataType.HOOK);
        //instanceDataPO.setCreateTime(new Date());
        return flowInstanceData;
    }

    @Override
    protected void postExecute(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBO currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        currentNodeInstance.setInstanceDataId(runtimeContext.getInstanceDataId());
        currentNodeInstance.setStatus(NodeInstanceStatus.COMPLETED);
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);
    }

    /**
     * Calculate unique outgoing
     * Expression: one of flowElement's properties
     * Input: data map
     *
     * @return
     * @throws Exception
     */
    @Override
    protected RuntimeExecutor getExecuteExecutor(RuntimeContext runtimeContext) throws ProcessException {
        FlowElement nextNode = calculateNextNode(runtimeContext.getCurrentNodeModel(),
            runtimeContext.getFlowElementMap(), runtimeContext.getInstanceDataMap());

        runtimeContext.setCurrentNodeModel(nextNode);
        return executorFactory.getElementExecutor(nextNode);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ensureHookService();
    }

    private void ensureHookService() {
        if (hookServices != null) {
            return;
        }

        // init hook services by Spring application context
        synchronized (ExclusiveGatewayExecutor.class) {
            if (hookServices != null) {
                return;
            }
            hookServices = new ArrayList<>();
            String[] names = applicationContext.getBeanNamesForType(HookService.class);
            for (String name : names) {
                Object bean = applicationContext.getBean(name);
                if (bean != null) {
                    hookServices.add((HookService) bean);
                }
            }
        }
    }
}
