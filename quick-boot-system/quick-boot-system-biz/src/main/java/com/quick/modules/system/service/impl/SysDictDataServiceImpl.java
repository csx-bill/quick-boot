package com.quick.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.modules.system.entity.SysDictData;
import com.quick.modules.system.mapper.SysDictDataMapper;
import com.quick.modules.system.service.ISysDictDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
