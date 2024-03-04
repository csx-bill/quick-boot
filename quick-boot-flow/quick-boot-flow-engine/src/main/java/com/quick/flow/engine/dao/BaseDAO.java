package com.quick.flow.engine.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseDAO<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IService<T> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseDAO.class);
}
