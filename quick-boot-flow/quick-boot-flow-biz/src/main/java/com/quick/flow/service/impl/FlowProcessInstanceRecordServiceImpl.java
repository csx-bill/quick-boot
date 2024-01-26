package com.quick.flow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.flow.entity.FlowProcessInstanceRecord;
import com.quick.flow.mapper.FlowProcessInstanceRecordMapper;
import com.quick.flow.service.IFlowProcessInstanceRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlowProcessInstanceRecordServiceImpl extends ServiceImpl<FlowProcessInstanceRecordMapper, FlowProcessInstanceRecord> implements IFlowProcessInstanceRecordService {

}
