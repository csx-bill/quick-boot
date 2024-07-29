package com.quick.online.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.online.entity.OnlCgformField;
import com.quick.online.mapper.OnlCgformFieldMapper;
import com.quick.online.service.IOnlCgformFieldService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnlCgformFieldServiceImpl extends ServiceImpl<OnlCgformFieldMapper, OnlCgformField> implements IOnlCgformFieldService {

    @Override
    public List<OnlCgformField> selectByTableName(String tableName, String dsName) {
        return baseMapper.selectByTableName(tableName,dsName);
    }
}
