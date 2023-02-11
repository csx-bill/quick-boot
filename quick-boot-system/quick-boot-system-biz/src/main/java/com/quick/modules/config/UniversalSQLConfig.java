package com.quick.modules.config;

import apijson.framework.APIJSONSQLConfig;

public class UniversalSQLConfig extends APIJSONSQLConfig {

    static {
        DEFAULT_DATABASE = DATABASE_MYSQL;  // TODO 默认数据库类型
        DEFAULT_SCHEMA = "quick-boot";  // TODO 默认数据库名
    }

    /**
     * 获取mysql 版本
     *
     * @return
     */
    @Override
    public String getDBVersion() {
        return "8.0.31";
    }

}
