package com.quick.modules.online.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.modules.online.entity.Function;
import com.quick.modules.online.mapper.FunctionMapper;
import com.quick.modules.online.service.IFunctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FunctionServiceImpl extends ServiceImpl<FunctionMapper, Function> implements IFunctionService {
}
