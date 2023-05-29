package com.quick.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.constant.CacheConstant;
import com.quick.modules.system.entity.SysDict;
import com.quick.modules.system.mapper.SysDictMapper;
import com.quick.modules.system.service.ISysDictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {
    @CacheEvict(value = {CacheConstant.SYS_DICT_CACHE}, allEntries = true)
    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @CacheEvict(value = {CacheConstant.SYS_DICT_CACHE}, allEntries = true)
    @Override
    public boolean removeByIds(Collection<?> list) {
        return super.removeByIds(list);
    }
}
