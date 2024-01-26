package com.quick.flow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.flow.entity.FlowProcessNodeRecordAssignUser;
import com.quick.flow.mapper.FlowProcessNodeRecordAssignUserMapper;
import com.quick.flow.service.IFlowProcessNodeRecordAssignUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlowProcessNodeRecordAssignUserServiceImpl extends ServiceImpl<FlowProcessNodeRecordAssignUserMapper, FlowProcessNodeRecordAssignUser> implements IFlowProcessNodeRecordAssignUserService {

}
