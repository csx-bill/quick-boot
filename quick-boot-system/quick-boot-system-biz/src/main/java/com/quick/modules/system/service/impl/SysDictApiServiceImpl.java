package com.quick.modules.system.service.impl;

import com.quick.modules.system.mapper.SysDictMapper;
import com.quick.modules.system.service.ISysDictApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictApiServiceImpl implements ISysDictApiService {
    private final SysDictMapper sysDictMapper;

    @Override
    public String translateDict(String dictCode, String dictValue) {
        return sysDictMapper.queryDictTextByKey(dictCode,dictValue);
    }
}
