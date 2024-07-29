package com.quick.online;

import apijson.Log;
import apijson.framework.APIJSONApplication;
import apijson.framework.APIJSONCreator;
import apijson.orm.*;
import com.quick.online.config.OnlineSQLConfig;
import com.quick.online.config.OnlineSQLExecutor;
import com.quick.online.parser.OnlineFunctionParser;
import com.quick.online.parser.OnlineParser;
import com.quick.online.parser.OnlineVerifier;
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
        //关闭debug 信息(勿动)
        Log.DEBUG = true;

        AbstractParser.MAX_OBJECT_COUNT = 20;
        // 批量更新最大条数
        AbstractParser.MAX_UPDATE_COUNT = 100;

        APIJSONApplication.init();
    }

    static {
        // 使用本项目的自定义处理类
        APIJSONApplication.DEFAULT_APIJSON_CREATOR = new APIJSONCreator<String>() {
            @Override
            public FunctionParser createFunctionParser() {
                return new OnlineFunctionParser();
            }

            @Override
            public Parser<String> createParser() {
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

            @Override
            public Verifier<String> createVerifier() {
                return new OnlineVerifier();
            }
        };
    }

    private static ApplicationContext APPLICATION_CONTEXT;

    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }
}
