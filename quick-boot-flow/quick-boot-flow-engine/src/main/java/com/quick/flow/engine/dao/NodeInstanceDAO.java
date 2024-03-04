package com.quick.flow.engine.dao;

import com.quick.flow.engine.common.NodeInstanceStatus;
import com.quick.flow.engine.mapper.FlowNodeInstanceMapper;
import com.quick.flow.engine.entity.FlowNodeInstance;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class NodeInstanceDAO extends BaseDAO<FlowNodeInstanceMapper, FlowNodeInstance> {

    /**
     * insert nodeInstancePO
     *
     * @param flowNodeInstance
     * @return -1 while insert failed
     */
    public int insert(FlowNodeInstance flowNodeInstance) {
        try {
            return baseMapper.insert(flowNodeInstance);
        } catch (Exception e) {
            LOGGER.error("insert exception.||nodeInstancePO={}", flowNodeInstance, e);
        }
        return -1;
    }

    /**
     * when nodeInstancePO's id is null, batch insert.
     * when nodeInstancePO's id is not null, update it status.
     *
     * @param nodeInstanceList
     * @return
     */
    // TODO: 2020/1/14 post handle while failed: retry 5 times
    public boolean insertOrUpdateList(List<FlowNodeInstance> nodeInstanceList) {
        if (CollectionUtils.isEmpty(nodeInstanceList)) {
            LOGGER.warn("insertOrUpdateList: nodeInstanceList is empty.");
            return true;
        }

        List<FlowNodeInstance> insertNodeInstanceList = Lists.newArrayList();
        nodeInstanceList.forEach(nodeInstancePO -> {
            if (nodeInstancePO.getId() == null) {
                insertNodeInstanceList.add(nodeInstancePO);
            } else {
                baseMapper.updateStatus(nodeInstancePO);
            }
        });

        if (CollectionUtils.isEmpty(insertNodeInstanceList)) {
            return true;
        }
        return saveBatch(insertNodeInstanceList);
    }

    public FlowNodeInstance selectByNodeInstanceId(String flowInstanceId, String nodeInstanceId) {
        return baseMapper.selectByNodeInstanceId(flowInstanceId, nodeInstanceId);
    }

    public FlowNodeInstance selectBySourceInstanceId(String flowInstanceId, String sourceNodeInstanceId, String nodeKey) {
        return baseMapper.selectBySourceInstanceId(flowInstanceId, sourceNodeInstanceId, nodeKey);
    }

    /**
     * select recent nodeInstancePO order by id desc
     * @param flowInstanceId
     * @return
     */
    public FlowNodeInstance selectRecentOne(String flowInstanceId) {
        return baseMapper.selectRecentOne(flowInstanceId);
    }

    /**
     * select recent active nodeInstancePO order by id desc
     * @param flowInstanceId
     * @return
     */
    public FlowNodeInstance selectRecentActiveOne(String flowInstanceId) {
        return baseMapper.selectRecentOneByStatus(flowInstanceId, NodeInstanceStatus.ACTIVE);
    }

    /**
     * select recent completed nodeInstancePO order by id desc
     * @param flowInstanceId
     * @return
     */
    public FlowNodeInstance selectRecentCompletedOne(String flowInstanceId) {
        return baseMapper.selectRecentOneByStatus(flowInstanceId, NodeInstanceStatus.COMPLETED);
    }

    /**
     * select recent active nodeInstancePO order by id desc
     * If it doesn't exist, select recent completed nodeInstancePO order by id desc
     *
     * @param flowInstanceId
     * @return
     */
    public FlowNodeInstance selectEnabledOne(String flowInstanceId) {
        FlowNodeInstance flowNodeInstance = baseMapper.selectRecentOneByStatus(flowInstanceId, NodeInstanceStatus.ACTIVE);
        if (flowNodeInstance == null) {
            LOGGER.info("selectEnabledOne: there's no active node of the flowInstance.||flowInstanceId={}", flowInstanceId);
            flowNodeInstance = baseMapper.selectRecentOneByStatus(flowInstanceId, NodeInstanceStatus.COMPLETED);
        }
        return flowNodeInstance;
    }

    public List<FlowNodeInstance> selectByFlowInstanceId(String flowInstanceId) {
        return baseMapper.selectByFlowInstanceId(flowInstanceId);
    }

    /**
     * select nodeInstancePOList order by id desc
     *
     * @param flowInstanceId
     * @return
     */
    public List<FlowNodeInstance> selectDescByFlowInstanceId(String flowInstanceId) {
        return baseMapper.selectDescByFlowInstanceId(flowInstanceId);
    }

    /**
     * update nodeInstancePO status by nodeInstanceId
     * @param flowNodeInstance
     * @param status
     */
    public void updateStatus(FlowNodeInstance flowNodeInstance, int status) {
        flowNodeInstance.setStatus(status);
        flowNodeInstance.setUpdateTime(LocalDateTime.now());
        baseMapper.updateStatus(flowNodeInstance);
    }
}
