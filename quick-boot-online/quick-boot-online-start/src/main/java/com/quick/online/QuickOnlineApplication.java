package com.quick.online;

import apijson.Log;
import apijson.framework.APIJSONApplication;
import apijson.framework.APIJSONCreator;
import apijson.orm.FunctionParser;
import apijson.orm.Parser;
import apijson.orm.SQLConfig;
import apijson.orm.SQLExecutor;
import com.quick.online.config.OnlineSQLConfig;
import com.quick.online.config.OnlineSQLExecutor;
import com.quick.online.parser.OnlineFunctionParser;
import com.quick.online.parser.OnlineParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

@Slf4j
@EnableFeignClients(value ={"com.quick.*"})
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(value = {"com.quick.*"})
public class QuickOnlineApplication {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext application = SpringApplication.run(QuickOnlineApplication.class, args);
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
            public FunctionParser createFunctionParser() {
                return new OnlineFunctionParser();
            }

            @Override
            public Parser<Long> createParser() {
                return new OnlineParser();
            }

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
