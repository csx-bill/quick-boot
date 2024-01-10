package com.quick.flow.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.flow.entity.FlowNodeInstanceLog;
import com.quick.flow.mapper.FlowNodeInstanceLogMapper;
import com.quick.flow.service.IFlowNodeInstanceLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class FlowNodeInstanceLogServiceImpl extends ServiceImpl<FlowNodeInstanceLogMapper, FlowNodeInstanceLog> implements IFlowNodeInstanceLogService {

}
