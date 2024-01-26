package com.quick.flow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.flow.entity.FlowProcessCopy;
import com.quick.flow.mapper.FlowProcessCopyMapper;
import com.quick.flow.service.IFlowProcessCopyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlowProcessCopyServiceImpl extends ServiceImpl<FlowProcessCopyMapper, FlowProcessCopy> implements IFlowProcessCopyService {

}
