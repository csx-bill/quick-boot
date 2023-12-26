package com.quick.online.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.online.entity.AccessSchema;
import com.quick.online.mapper.AccessSchemaMapper;
import com.quick.online.service.IAccessSchemaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessSchemaServiceImpl extends ServiceImpl<AccessSchemaMapper, AccessSchema> implements IAccessSchemaService {

}
