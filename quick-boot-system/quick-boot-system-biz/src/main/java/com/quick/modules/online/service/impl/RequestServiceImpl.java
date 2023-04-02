package com.quick.modules.online.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.modules.online.entity.Request;
import com.quick.modules.online.mapper.RequestMapper;
import com.quick.modules.online.service.IRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl extends ServiceImpl<RequestMapper, Request> implements IRequestService {
}
