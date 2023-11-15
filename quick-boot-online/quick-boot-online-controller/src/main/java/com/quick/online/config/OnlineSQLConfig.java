package com.quick.online.config;

import apijson.framework.APIJSONSQLConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class OnlineSQLConfig extends APIJSONSQLConfig {

    private static String defaultDatabase;

    private static String defaultSchema;

    private static String dbVersion;

    @Value("${mysql-server.default-database}")
    public void setDefaultDatabase(String defaultDatabase) {
        OnlineSQLConfig.defaultDatabase = defaultDatabase;
    }

    @Value("${mysql-server.database-name}")
    public void setDefaultSchema(String defaultSchema) {
        OnlineSQLConfig.defaultSchema = defaultSchema;
    }

    @Value("${mysql-server.db-version}")
    public void setDbVersion(String dbVersion) {
        OnlineSQLConfig.dbVersion = dbVersion;
    }


    @PostConstruct
    public void init() {
        DEFAULT_DATABASE = defaultDatabase;  // TODO 默认数据库类型
        DEFAULT_SCHEMA = defaultSchema;  // TODO 默认数据库名
    }

    /**
     * 获取mysql 版本
     *
     * @return
     */
    @Override
    public String getDBVersion() {
        return dbVersion;
    }

}
