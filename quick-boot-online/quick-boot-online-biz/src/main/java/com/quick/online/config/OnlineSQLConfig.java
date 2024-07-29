package com.quick.online.config;

import apijson.JSONRequest;
import apijson.RequestMethod;
import apijson.framework.APIJSONSQLConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class OnlineSQLConfig extends APIJSONSQLConfig<Long> {
    public OnlineSQLConfig() {
        super();
    }

    public OnlineSQLConfig(RequestMethod method, String table) {
        super(method, table);
    }

    private static String defaultDatabase;

    private static String defaultSchema;

    private static String dbVersion;

    private static String dbUri;

    private static String dbAccount;

    private static String dbPassword;

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

    @Value("${mysql-server.address}")
    public void setDBUri(String dbUri) {
        this.dbUri = dbUri;
    }

    @Value("${mysql-server.username}")
    public void setDBAccount(String dbAccount) {
        this.dbAccount = dbAccount;
    }

    @Value("${mysql-server.password}")
    public void setDBPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }


    @PostConstruct
    public void init() {
        DEFAULT_DATABASE = defaultDatabase;  // TODO 默认数据库类型
        DEFAULT_SCHEMA = defaultSchema;  // TODO 默认数据库名
    }

    @Override
    public String getDBUri() {
        return dbUri;
    }

    @Override
    public String getDBAccount() {
        return dbAccount;
    }

    @Override
    public String getDBPassword() {
        return dbPassword;
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


    /**
     * 软删除 配置
     */
    @Override
    public boolean isFakeDelete() {
        return true;
    }

    @Override
    public Map<String, Object> onFakeDelete(Map map) {
        return super.onFakeDelete(map);
    }

    /**
     * 前端传参小驼峰命名转为蛇形命名
     */
    @Override
    public String getSQLKey(String key) {
        return super.getSQLKey(JSONRequest.recoverUnderline(key, false));
    }

    @Override
    public String getSQLTable() {
        String t = super.getSQLTable();
        return isInfluxDB() ? t.toLowerCase() : JSONRequest.recoverUnderline(t, false);
    }

    // 取消注释后，默认的数据库类型会由 MySQL 改为 PostgreSQL
    	@Override
    	public String getDatabase() {
    		String db = super.getDatabase();
    		return db == null ? DATABASE_MYSQL : db;
    	}

    @Override
    public String getDatasource() {
        return super.getSQLSchema();
    }
}
