package com.quick.system.service.impl;

import com.quick.common.constant.CacheConstant;
import com.quick.system.mapper.SysDictMapper;
import com.quick.system.service.ISysDictApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictApiServiceImpl implements ISysDictApiService {
    private final SysDictMapper sysDictMapper;
    @Cacheable(value = CacheConstant.SYS_DICT_CACHE,key = "#dictCode+':'+#dictValue", unless = "#result == null ")
    @Override
    public String translateDict(String dictCode, String dictValue) {
        return sysDictMapper.queryDictTextByKey(dictCode,dictValue);
    }
}
