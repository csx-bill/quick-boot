package com.quick.flow.service;


import com.quick.flow.dto.Flow;

/**
 * @author minliuhua
 * @description: 流程执行service
 * @date: 2023/5/29 13:09
 */
public interface IExecuteService {


    /**
     * 提交审批
     * @param flow
     * @param tableName
     */
    void startFlow(Flow flow, String tableName);
}
