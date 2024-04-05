package com.quick.flow.dto;

/**
 * OA 请假申请对象 test_leave
 *
 * @author hh
 * @date 2023-04-09
 */
public interface Flow {

    public Long getId();

    public void setId(Long id);

    public Long getInstanceId();

    public void setInstanceId(Long instanceId);

    public Integer getFlowStatus();

    public void setFlowStatus(Integer flowStatus);

    public String getNodeCode();

    public void setNodeCode(String nodeCode);

    public String getNodeName();

    public void setNodeName(String nodeName);
}