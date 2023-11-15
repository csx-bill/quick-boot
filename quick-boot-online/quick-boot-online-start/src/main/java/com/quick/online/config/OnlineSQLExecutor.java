package com.quick.online.config;

import apijson.framework.APIJSONSQLExecutor;
import apijson.orm.SQLConfig;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.quick.online.QuickOnlineApplication;

import javax.sql.DataSource;
import java.sql.Connection;

public class OnlineSQLExecutor extends APIJSONSQLExecutor {
    public static final String TAG = "OnlineSQLExecutor";

    // 适配连接池
    @Override
    public Connection getConnection(SQLConfig config) throws Exception {
        String key = config.getDatasource() + "-" + config.getDatabase();
        Connection c = connectionMap.get(key);
        if (c == null || c.isClosed()) {
            DataSource dataSource = QuickOnlineApplication.getApplicationContext().getBean(DataSource.class);
            DynamicRoutingDataSource datasource = (DynamicRoutingDataSource) dataSource;
            DataSource ds = datasource.determineDataSource();
            connectionMap.put(key, ds == null ? null : ds.getConnection());
        }
        return super.getConnection(config);
    }

}
