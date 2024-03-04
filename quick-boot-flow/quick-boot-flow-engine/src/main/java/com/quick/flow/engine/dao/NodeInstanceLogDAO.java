package com.quick.flow.engine.dao;

import com.quick.flow.engine.mapper.FlowNodeInstanceLogMapper;
import com.quick.flow.engine.entity.FlowNodeInstanceLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NodeInstanceLogDAO extends BaseDAO<FlowNodeInstanceLogMapper, FlowNodeInstanceLog> {

    /**
     * insert nodeInstanceLogPO
     *
     * @param flowNodeInstanceLog
     * @return -1 while insert failed
     */
    public int insert(FlowNodeInstanceLog flowNodeInstanceLog) {
        try {
            return baseMapper.insert(flowNodeInstanceLog);
        } catch (Exception e) {
            LOGGER.error("insert exception.||nodeInstanceLogPO={}", flowNodeInstanceLog, e);
        }
        return -1;
    }

    /**
     * nodeInstanceLogList batch insert
     *
     * @param nodeInstanceLogList
     * @return
     */
    public boolean insertList(List<FlowNodeInstanceLog> nodeInstanceLogList) {
        return saveBatch(nodeInstanceLogList);
    }
}
