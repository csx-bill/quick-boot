package com.quick.online.config;

import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class JdbcDynamicDataSourceProvider extends AbstractJdbcDataSourceProvider {

    public JdbcDynamicDataSourceProvider(DefaultDataSourceCreator defaultDataSourceCreator, String driverClassName, String url, String username, String password) {
        super(defaultDataSourceCreator, driverClassName, url, username, password);
    }

    /**
     * 数据库中获取数据库连接信息
     * @param statement
     * @return
     * @throws SQLException
     */
    @Override
    protected Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException {
        Map<String, DataSourceProperty> map = new HashMap<>(8);
        ResultSet rs = statement.executeQuery("select * from sys_data_source where del_flag = '0'");
        while (rs.next()) {
            String name = rs.getString("name");
            String username = rs.getString("db_username");
            String password = rs.getString("db_password");
            String url = rs.getString("db_url");
            String driver = rs.getString("db_driver");
            DataSourceProperty property = new DataSourceProperty();
            property.setUsername(username);
            property.setPassword(password);
            property.setUrl(url);
            property.setDriverClassName(driver);
            map.put(name, property);
        }
        return map;
    }
}
