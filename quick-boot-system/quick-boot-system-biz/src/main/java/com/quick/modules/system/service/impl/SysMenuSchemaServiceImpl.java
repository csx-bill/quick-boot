package com.quick.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.modules.system.entity.SysMenuSchema;
import com.quick.modules.system.mapper.SysMenuSchemaMapper;
import com.quick.modules.system.service.ISysMenuSchemaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuSchemaServiceImpl extends ServiceImpl<SysMenuSchemaMapper, SysMenuSchema> implements ISysMenuSchemaService {
    @Override
    public SysMenuSchema queryByPath(String path) {
        return baseMapper.queryByPath(path);
    }
}
