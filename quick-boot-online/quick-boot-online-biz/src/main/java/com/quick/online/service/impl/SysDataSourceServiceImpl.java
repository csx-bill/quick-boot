package com.quick.online.service.impl;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.online.entity.SysDataSource;
import com.quick.online.mapper.SysDataSourceMapper;
import com.quick.online.service.ISysDataSourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysDataSourceServiceImpl extends ServiceImpl<SysDataSourceMapper, SysDataSource> implements ISysDataSourceService {

    private final DataSource dataSource;
    private final DefaultDataSourceCreator dataSourceCreator;

    @Override
    public boolean removeById(Serializable id) {
        SysDataSource sysDataSource = baseMapper.selectById(id);
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        ds.removeDataSource(sysDataSource.getName());
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<?> list) {
        for (Object id : list) {
            SysDataSource sysDataSource = baseMapper.selectById((Serializable) id);
            DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
            ds.removeDataSource(sysDataSource.getName());
        }
        return super.removeByIds(list);
    }

    @Override
    public boolean save(SysDataSource entity) {
        // 校验配置合法性
        if (!checkDataSource(entity)) {
            return Boolean.FALSE;
        }
        // 添加动态数据源
        addDynamicDataSource(entity);

        return super.save(entity);
    }

    @Override
    public boolean updateById(SysDataSource entity) {
        // 校验配置合法性
        if (!checkDataSource(entity)) {
            return Boolean.FALSE;
        }
        // 先移除
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        ds.removeDataSource(entity.getName());

        // 添加动态数据源
        addDynamicDataSource(entity);

        return super.updateById(entity);
    }

    @Override
    public void addDynamicDataSource(SysDataSource entity) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setPoolName(entity.getName());
        dataSourceProperty.setUrl(entity.getDbUrl());
        dataSourceProperty.setUsername(entity.getDbUsername());
        dataSourceProperty.setPassword(entity.getDbPassword());
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        DataSource dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
        ds.addDataSource(entity.getName(), dataSource);
    }

    @Override
    public Boolean checkDataSource(SysDataSource entity) {
        try (Connection connection = DriverManager.getConnection(entity.getDbUrl(),entity.getDbUsername(), entity.getDbPassword())) {
        }
        catch (SQLException e) {
            log.error("数据源配置 {} , 获取链接失败", entity.getName(), e);
            throw new RuntimeException("数据库配置错误，链接失败");
        }
        return Boolean.TRUE;
    }
}
