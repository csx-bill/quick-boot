package com.quick;

import apijson.Log;
import apijson.framework.APIJSONApplication;
import apijson.framework.APIJSONCreator;
import apijson.orm.SQLConfig;
import apijson.orm.SQLExecutor;
import com.quick.modules.config.OnlineSQLConfig;
import com.quick.modules.config.OnlineSQLExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

@Slf4j
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class QuickSystemApplication {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext application = SpringApplication.run(QuickSystemApplication.class, args);
        Environment env = application.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "应用 '{}' 运行成功! 访问连接:\n\t" +
                        "Swagger文档: \t\thttp://{}:{}/doc.html\n\t" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port", "8080"));
        APPLICATION_CONTEXT = application;
        //关闭debug 信息
        Log.DEBUG = false;
        APIJSONApplication.init(false);
    }

    static {
        // 使用本项目的自定义处理类
        APIJSONApplication.DEFAULT_APIJSON_CREATOR = new APIJSONCreator<Long>() {
            @Override
            public SQLConfig createSQLConfig() {
                return new OnlineSQLConfig();
            }

            @Override
            public SQLExecutor createSQLExecutor() {
                return new OnlineSQLExecutor();
            }
        };
    }

    private static ApplicationContext APPLICATION_CONTEXT;

    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }
}
