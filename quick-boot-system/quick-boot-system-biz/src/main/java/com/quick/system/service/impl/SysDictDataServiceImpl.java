package com.quick.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.constant.CacheConstant;
import com.quick.system.entity.SysDictData;
import com.quick.system.mapper.SysDictDataMapper;
import com.quick.system.service.ISysDictDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {

    /**
     * 通过字典code获取字典数据
     * @param dictCode
     * @return
     */
    @Override
    public List<SysDictData> queryDictDataByDictCode(String dictCode) {
        return baseMapper.queryDictDataByDictCode(dictCode);
    }

    @CacheEvict(value={CacheConstant.SYS_DICT_CACHE}, allEntries=true)
    @Override
    public boolean updateById(SysDictData entity) {
        return super.updateById(entity);
    }

    @CacheEvict(value={CacheConstant.SYS_DICT_CACHE}, allEntries=true)
    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @CacheEvict(value={CacheConstant.SYS_DICT_CACHE}, allEntries=true)
    @Override
    public boolean removeByIds(Collection<?> list) {
        return super.removeByIds(list);
    }
}
