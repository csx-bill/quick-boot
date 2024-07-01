package com.quick.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.online.entity.SysDataSource;

public interface ISysDataSourceService extends IService<SysDataSource> {
    /**
     * 更新动态数据的数据源列表
     * @param entity
     * @return
     */
    void addDynamicDataSource(SysDataSource entity);

    /**
     * 校验数据源配置是否有效
     * @param entity 数据源信息
     * @return 有效/无效
     */
    Boolean checkDataSource(SysDataSource entity);
}
