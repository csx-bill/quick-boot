/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : nacos

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 05/02/2024 00:11:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL,
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 107 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (4, 'redis.yml', 'DEFAULT_GROUP', 'spring:\n  cache:\n    type: redis\n  data:\n    redis:\n      host: 127.0.0.1\n      port: 6379\n      password: 123456', '2e2f4b1741b4d5338c2dd5a1e8ee72c3', '2023-01-24 16:49:35', '2023-05-04 08:11:06', 'nacos', '0:0:0:0:0:0:0:1', '', 'dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (5, 'sa-token.yml', 'DEFAULT_GROUP', 'sa-token:\n  oauth2:\n    is-code: true\n    is-implicit: true\n    is-password: true\n    is-client: true\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  active-timeout: -1\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: false\n  # token风格\n  token-style: random-64\n  # 是否输出操作日志\n  is-log: false', 'd03abd3bae1b56ecd7ff53f876720f54', '2023-01-24 16:53:20', '2023-11-14 08:33:08', 'nacos', '0:0:0:0:0:0:0:1', '', 'dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (14, 'logging.yml', 'DEFAULT_GROUP', 'logging:\n  level:\n    root: info\n  pattern:\n    file: \'%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level [${spring.application.name:},%X{traceId:-},%X{spanId:-}] ==> [%thread] %F.%M - %msg%n\'\n    console: \'%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level [${spring.application.name:},%X{traceId:-},%X{spanId:-}] ==> [%thread] %F.%M - %msg%n\'\n  file:\n    path: ./logs\n    name: ${logging.file.path}/${spring.application.name}/root.log\n  logback:\n    rollingpolicy:\n      max-file-size: 50MB\n      max-history: 7', '1b7f26401c67202206d19fef46440459', '2023-01-26 19:50:52', '2023-06-06 15:36:39', 'nacos', '0:0:0:0:0:0:0:1', '', 'dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (15, 'monitor.yml', 'DEFAULT_GROUP', '# 暴露监控端点\r\nmanagement:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \'*\'\r\n  endpoint:\r\n    health:\r\n      show-details: always', 'ad12d0ced2301c74531e9719649721bf', '2023-01-26 20:13:39', '2023-01-26 20:13:39', NULL, '0:0:0:0:0:0:0:1', '', 'dev', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (32, 'knife4j.yml', 'DEFAULT_GROUP', 'springdoc:\n  swagger-ui:\n    path: /swagger-ui.html\n    tags-sorter: alpha\n    operations-sorter: alpha\n  api-docs:\n    path: /v3/api-docs\n  group-configs:\n    - group: \'default\'\n      paths-to-match: \'/**\'\n      packages-to-scan: com.quick\n\nknife4j:\n  enable: true\n  setting:\n    language: zh_cn\n    swagger-model-name: 实体类列表', '23065df72e7bd07fe74bb4e8fefafbc0', '2023-04-22 07:25:55', '2023-11-14 14:49:27', 'nacos', '0:0:0:0:0:0:0:1', '', 'dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (33, 'mybatis-plus.yml', 'DEFAULT_GROUP', 'mybatis-plus:\n  global-config:\n    db-config:\n      logic-delete-field: delFlag\n      logic-delete-value: 1 # 逻辑已删除值(默认为 1)\n      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)\n      select-strategy: not_empty # 空字符查询\n      column-format: \"`%s`\" # 关键词格式化\n  mapper-locations: classpath*:com/quick/**/*Mapper.xml', 'b50a9d81a84383b2f380cb86d493e772', '2023-04-22 07:26:42', '2023-11-15 09:57:57', 'nacos', '0:0:0:0:0:0:0:1', '', 'dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (50, 'quick-boot-system.yml', 'DEFAULT_GROUP', 'server:\n  port: 8080\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n\n# 租户表维护\nquick:\n  tenant:\n    tables:\n      - sys_user\n      - sys_dept\n      - sys_menu', '47bd3e51dd5ede684ca42d3149b5e049', '2023-11-14 14:39:44', '2024-01-23 06:47:51', 'nacos', '0:0:0:0:0:0:0:1', '', 'dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (52, 'quick-boot-gateway.yml', 'DEFAULT_GROUP', 'server:\n  port: 9999\n\nspring:\n  cloud:\n    gateway:\n      routes:\n        - id: quick-boot-system\n          uri: lb://quick-boot-system\n          predicates:\n            - Path=/api/system/**\n          filters:\n            - StripPrefix=2\n        - id: quick-boot-oauth2\n          uri: lb://quick-boot-oauth2\n          predicates:\n            - Path=/api/auth/**\n          filters:\n            - StripPrefix=2\n        - id: quick-boot-online\n          uri: lb://quick-boot-online\n          predicates:\n            - Path=/api/online/**\n          filters:\n            - StripPrefix=2\n        - id: quick-boot-jimureport\n          uri: lb://quick-boot-jimureport\n          predicates:\n            - Path=/jmreport/**\n          #filters:\n          #  - StripPrefix=2\n\nknife4j:\n  gateway:\n    enabled: true\n    # 指定服务发现的模式聚合微服务文档，并且是默认`default`分组\n    strategy: discover\n    discover:\n      enabled: true\n      # 指定版本号(Swagger2|OpenAPI3)\n      version : openapi3\n      # 需要排除的微服务(eg:网关服务)\n      excluded-services:\n        - quick-boot-gateway\n\n# 忽略认证的地址\nignore:\n  url:\n    - /favicon.ico\n    - /doc.html\n    - /api/system/v3/api-docs/**\n    - /api/auth/v3/api-docs/**\n    - /webjars/**\n    - /swagger-resources/**\n    - /v3/api-docs/**\n    - /actuator/**\n    - /instances/**\n    - /api/system/menu/getRoutes\n    - /api/auth/doLogin\n    - /api/auth/oauth2/**\n    - /jmreport/**\n    - /api/system/tenant/list\n', '74db03bc54be6949f2fa1de9fdc2f561', '2023-11-14 15:24:00', '2024-02-04 07:00:56', 'nacos', '0:0:0:0:0:0:0:1', '', 'dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (53, 'quick-boot-oauth2.yml', 'DEFAULT_GROUP', 'server:\n  port: 8082\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}', '2b57986e41671e9ac4e4852842d8c2c1', '2023-11-14 15:29:16', '2024-01-23 06:56:37', 'nacos', '0:0:0:0:0:0:0:1', '', 'dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (54, 'quick-boot-monitor.yml', 'DEFAULT_GROUP', 'server:\r\n  port: 8081\r\n\r\n\r\n\r\n# 暴露监控端点\r\nmanagement:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \'*\'\r\n  endpoint:\r\n    health:\r\n      show-details: always', '9fbdc478e7d23d7a01d767710fe3b5a8', '2023-11-14 15:36:53', '2023-11-14 15:36:53', NULL, '0:0:0:0:0:0:0:1', '', 'dev', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (55, 'quick-boot-online.yml', 'DEFAULT_GROUP', 'server:\n  port: 8083\n\nspring:\n  datasource:\n    dynamic:\n      primary: quick-boot-online #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        quick-boot-online:\n          url: ${mysql-server.address}/quick-boot-online\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n        quick-boot:\n          url: ${mysql-server.address}/quick-boot\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n\n# APIJSON的配置\nmysql-server:\n  database-name: quick-boot-online\n  defaultDatabase: MYSQL\n  db-version: 8.0.31', 'f03048251b328197ed166fecfc29eeb5', '2023-11-15 02:43:12', '2024-01-25 10:14:28', 'nacos', '0:0:0:0:0:0:0:1', '', 'dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (58, 'mysql.yml', 'DEFAULT_GROUP', 'mysql-server:\n  address: jdbc:p6spy:mysql://127.0.0.1:3306\n  username: root\n  password: 123456\n  driver-class-name: com.p6spy.engine.spy.P6SpyDriver\n\n', 'b45ec670eaec6db7f55278b88ed45b02', '2023-11-15 08:14:24', '2024-01-23 06:41:24', 'nacos', '0:0:0:0:0:0:0:1', '', 'dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (79, 'quick-boot-flow.yml', 'DEFAULT_GROUP', 'server:\n  port: 8084\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot-flow?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&nullCatalogMeansCurrent=true\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}', 'f4ec641b1027f89b31e77ad38e0c0f7d', '2024-01-07 13:02:09', '2024-01-26 05:51:44', 'nacos', '0:0:0:0:0:0:0:1', '', 'dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (99, 'quick-boot-jimureport.yml', 'DEFAULT_GROUP', 'server:\n  port: 8085\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot-jimureport\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n\njeecg :\n  jmreport:\n    #多租户模式，默认值为空(created:按照创建人隔离、tenant:按照租户隔离) (v1.6.2+ 新增)\n    saasMode: tenant\n    # 平台上线安全配置(v1.6.2+ 新增)\n    firewall:\n      # 数据源安全 (开启后，不允许使用平台数据源、SQL解析加签并且不允许查询数据库)\n      dataSourceSafe: false\n      # 低代码开发模式（dev:开发模式，prod:发布模式—关闭在线报表设计功能，分配角色admin、lowdeveloper可以放开限制）\n      lowCodeMode: dev\n    #是否 禁用导出PDF和图片的按钮 默认为false\n    exportDisabled: false\n    #是否自动保存\n    autoSave: true\n    #自动保存间隔时间毫秒\n    interval: 20000\n    # 列数(设计页面展示多少列)\n    col: 100\n    #自定义项目前缀\n    #customPrePath:\n    # 自定义API接口的前缀 #{api_base_path}和#{domainURL}的值\n    #apiBasePath: http://localhost:8080/jeecg-boot\n    #预览分页自定义\n    pageSize:\n      - 10\n      - 20\n      - 30\n      - 40\n    #打印纸张自定义\n    printPaper:\n      - title: A5纸\n        size:\n          - 148\n          - 210\n      - title: B4纸\n        size:\n          - 250\n          - 353\n    #接口超时设置（毫秒）\n    connect-timeout: 300000\n    #Excel导出模式(fast/快、primary/精致模式，默认fast)\n    export-excel-pattern: fast\n    #Excel导出数据每个sheet的行数,每个sheet最大1048576行\n    page-size-number: 10000\n    #设计页面表格的线是否显示 默认true\n    line: true', 'ba84358c6843f550a7916083862abb99', '2024-02-03 03:25:28', '2024-02-03 04:14:52', 'nacos', '0:0:0:0:0:0:0:1', '', 'dev', '', '', '', 'yaml', '', '');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC, `datum_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '增加租户字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info_beta' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC, `tag_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info_tag' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id` ASC, `tag_name` ASC, `tag_type` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_tag_relation' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint UNSIGNED NOT NULL,
  `nid` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL,
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create` ASC) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified` ASC) USING BTREE,
  INDEX `idx_did`(`data_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 112 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '多租户改造' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (50, 81, 'quick-boot-system.yml', 'DEFAULT_GROUP', '', 'server:\r\n  port: 8080\r\n\r\nspring:\r\n  datasource:\r\n    dynamic:\r\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\r\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\r\n      datasource:\r\n        master:\r\n          url: jdbc:p6spy:mysql://127.0.0.1:3306/quick-boot\r\n          username: root\r\n          password: 123456\r\n          driver-class-name: com.p6spy.engine.spy.P6SpyDriver\r\n', 'bd625066428558b946cfcd0afa7a56f6', '2024-01-06 21:34:04', '2024-01-06 13:34:05', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (50, 82, 'quick-boot-system.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8080\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: jdbc:p6spy:mysql://127.0.0.1:3306/quick-boot\n          username: root\n          password: 123456\n          driver-class-name: com.p6spy.engine.spy.P6SpyDriver\n\n# 租户表维护\nquick:\n  tenant:\n    tables:\n      - sys_user\n      - sys_dept', 'a6da539cfbf25f34f022c9e107257a65', '2024-01-06 21:53:27', '2024-01-06 13:53:27', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (0, 83, 'quick-boot-flow.yml', 'DEFAULT_GROUP', '', 'server:\r\n  port: 8084\r\n\r\nspring:\r\n  datasource:\r\n    dynamic:\r\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\r\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\r\n      datasource:\r\n        master:\r\n          url: ${mysql-server.address}/quick-boot-flow\r\n          username: ${mysql-server.username}\r\n          password: ${mysql-server.password}\r\n          driver-class-name: ${mysql-server.driver-class-name}', '1fc4e318303b9882b17d2c61c609b017', '2024-01-07 21:02:09', '2024-01-07 13:02:09', NULL, '0:0:0:0:0:0:0:1', 'I', 'dev', '');
INSERT INTO `his_config_info` VALUES (75, 84, 'quick-boot-flowable.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8084\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot-flowable\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}', 'ff6145e7d3da1dbb5cef1bf849533efa', '2024-01-07 21:02:21', '2024-01-07 13:02:21', NULL, '0:0:0:0:0:0:0:1', 'D', 'dev', '');
INSERT INTO `his_config_info` VALUES (55, 85, 'quick-boot-online.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8083\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: jdbc:p6spy:mysql://127.0.0.1:3306/quick-boot\n          #url: jdbc:p6spy:mysql://127.0.0.1:3306/ry-vue-master\n          username: root\n          password: 123456\n          driver-class-name: com.p6spy.engine.spy.P6SpyDriver\n', '6badddd3fad4ba750cac767ed13153b9', '2024-01-23 14:39:11', '2024-01-23 06:39:11', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (55, 86, 'quick-boot-online.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8083\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot-online\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n', '529d6089dd7a39e166bb8fbcf0e1cf1a', '2024-01-23 14:41:04', '2024-01-23 06:41:05', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (58, 87, 'mysql.yml', 'DEFAULT_GROUP', '', 'mysql-server:\n  address: jdbc:p6spy:mysql://127.0.0.1:3306\n  database-name: quick-boot\n  username: root\n  password: 123456\n  driver-class-name: com.p6spy.engine.spy.P6SpyDriver\n  # APIJSON的配置\n  defaultDatabase: MYSQL\n  db-version: 8.0.31\n\n', '8d260424033e1bf5449b8ff25aed9cf0', '2024-01-23 14:41:23', '2024-01-23 06:41:24', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (50, 88, 'quick-boot-system.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8080\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: jdbc:p6spy:mysql://127.0.0.1:3306/quick-boot\n          username: root\n          password: 123456\n          driver-class-name: com.p6spy.engine.spy.P6SpyDriver\n\n# 租户表维护\nquick:\n  tenant:\n    tables:\n      - sys_user\n      - sys_dept\n      - sys_menu', '80af54261a8d7a2f1edcb10f9ee085cd', '2024-01-23 14:47:50', '2024-01-23 06:47:51', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (53, 89, 'quick-boot-oauth2.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8082\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: jdbc:p6spy:mysql://127.0.0.1:3306/quick-boot\n          username: root\n          password: 123456\n          driver-class-name: com.p6spy.engine.spy.P6SpyDriver', '0dd2c9244f6a31374097a0494de00d29', '2024-01-23 14:56:36', '2024-01-23 06:56:37', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (55, 90, 'quick-boot-online.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8083\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot-online\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n\n# APIJSON的配置\nmysql-server:\n  database-name: quick-boot-online\n  defaultDatabase: MYSQL\n  db-version: 8.0.31', '40dc7ddf1090111e8fb0a621e2a7a06f', '2024-01-23 15:57:48', '2024-01-23 07:57:48', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (55, 91, 'quick-boot-online.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8083\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot-online\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n        quick-boot:\n          url: ${mysql-server.address}/quick-boot\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n# APIJSON的配置\nmysql-server:\n  database-name: quick-boot-online\n  defaultDatabase: MYSQL\n  db-version: 8.0.31', 'f244f7c383969d06c21581a459b032a4', '2024-01-23 16:07:39', '2024-01-23 08:07:40', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (55, 92, 'quick-boot-online.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8083\n\nspring:\n  datasource:\n    dynamic:\n      primary: quick-boot-online #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        quick-boot-online:\n          url: ${mysql-server.address}/quick-boot-online\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n        quick-boot:\n          url: ${mysql-server.address}/quick-boot\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n# APIJSON的配置\nmysql-server:\n  database-name: quick-boot-online\n  defaultDatabase: MYSQL\n  db-version: 8.0.31', '284602999ab8f7ba6b70729bcc749303', '2024-01-24 11:35:50', '2024-01-24 03:35:51', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (55, 93, 'quick-boot-online.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8083\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot-online\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n# APIJSON的配置\nmysql-server:\n  database-name: quick-boot-online\n  defaultDatabase: MYSQL\n  db-version: 8.0.31', '2fd850e41bf4284024ec7a325ac3b851', '2024-01-24 15:45:19', '2024-01-24 07:45:19', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (55, 94, 'quick-boot-online.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8083\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot-online\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n        quick-boot:\n          url: ${mysql-server.address}/quick-boot\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n# APIJSON的配置\nmysql-server:\n  database-name: quick-boot-online\n  defaultDatabase: MYSQL\n  db-version: 8.0.31', 'f244f7c383969d06c21581a459b032a4', '2024-01-24 18:22:38', '2024-01-24 10:22:39', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (55, 95, 'quick-boot-online.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8083\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot-online\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n        # quick-boot:\n        #   url: ${mysql-server.address}/quick-boot\n        #   username: ${mysql-server.username}\n        #   password: ${mysql-server.password}\n        #   driver-class-name: ${mysql-server.driver-class-name}\n\n    hikari:\n      type: com.zaxxer.hikari.HikariDataSource\n      driverClassName: ${mysql-server.driver-class-name}\n      url: ${mysql-server.address}/quick-boot\n      username: ${mysql-server.username}\n      password: ${mysql-server.password}\n      minimum-idle: 5\n      idle-timeout: 600000\n      maximum-pool-size: 10\n      auto-commit: true\n      pool-name: MyHikariCP\n      max-lifetime: 1800000\n      connection-timeout: 30000\n      connection-test-query: SELECT 1\n\n# APIJSON的配置\nmysql-server:\n  database-name: quick-boot-online\n  defaultDatabase: MYSQL\n  db-version: 8.0.31', 'b7cec715a2db8368aaf44236a9427bb9', '2024-01-24 18:24:24', '2024-01-24 10:24:25', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (55, 96, 'quick-boot-online.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8083\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot-online\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n        # quick-boot:\n        #   url: ${mysql-server.address}/quick-boot\n        #   username: ${mysql-server.username}\n        #   password: ${mysql-server.password}\n        #   driver-class-name: ${mysql-server.driver-class-name}\n\n    hikari:\n      type: com.zaxxer.hikari.HikariDataSource\n      driverClassName: ${mysql-server.driver-class-name}\n      url: jdbc:mysql://localhost:3306/quick-boot\n      username: ${mysql-server.username}\n      password: ${mysql-server.password}\n      minimum-idle: 5\n      idle-timeout: 600000\n      maximum-pool-size: 10\n      auto-commit: true\n      pool-name: MyHikariCP\n      max-lifetime: 1800000\n      connection-timeout: 30000\n      connection-test-query: SELECT 1\n\n# APIJSON的配置\nmysql-server:\n  database-name: quick-boot-online\n  defaultDatabase: MYSQL\n  db-version: 8.0.31', 'ea8814b9653d0c70a999c165a652ff15', '2024-01-24 18:25:08', '2024-01-24 10:25:09', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (55, 97, 'quick-boot-online.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8083\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot-online\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n        # quick-boot:\n        #   url: ${mysql-server.address}/quick-boot\n        #   username: ${mysql-server.username}\n        #   password: ${mysql-server.password}\n        #   driver-class-name: ${mysql-server.driver-class-name}\n\n    hikari:\n      type: com.zaxxer.hikari.HikariDataSource\n      driverClassName: com.mysql.cj.jdbc.Driver\n      url: jdbc:mysql://localhost:3306/quick-boot\n      username: ${mysql-server.username}\n      password: ${mysql-server.password}\n      minimum-idle: 5\n      idle-timeout: 600000\n      maximum-pool-size: 10\n      auto-commit: true\n      pool-name: MyHikariCP\n      max-lifetime: 1800000\n      connection-timeout: 30000\n      connection-test-query: SELECT 1\n\n# APIJSON的配置\nmysql-server:\n  database-name: quick-boot-online\n  defaultDatabase: MYSQL\n  db-version: 8.0.31', '80e97cb8f557b9ecc4614fa82549384d', '2024-01-24 18:29:06', '2024-01-24 10:29:07', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (55, 98, 'quick-boot-online.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8083\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot-online\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n        # quick-boot:\n        #   url: ${mysql-server.address}/quick-boot\n        #   username: ${mysql-server.username}\n        #   password: ${mysql-server.password}\n        #   driver-class-name: ${mysql-server.driver-class-name}\n\n    hikari:\n      type: com.zaxxer.hikari.HikariDataSource\n      driverClassName: com.mysql.cj.jdbc.Driver\n      url: jdbc:mysql://localhost:3306\n      username: ${mysql-server.username}\n      password: ${mysql-server.password}\n      minimum-idle: 5\n      idle-timeout: 600000\n      maximum-pool-size: 10\n      auto-commit: true\n      pool-name: MyHikariCP\n      max-lifetime: 1800000\n      connection-timeout: 30000\n      connection-test-query: SELECT 1\n\n# APIJSON的配置\nmysql-server:\n  database-name: quick-boot-online\n  defaultDatabase: MYSQL\n  db-version: 8.0.31', '7988978a203bcdb14e4125dead03923d', '2024-01-24 18:30:45', '2024-01-24 10:30:46', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (55, 99, 'quick-boot-online.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8083\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot-online\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n        # quick-boot:\n        #   url: ${mysql-server.address}/quick-boot\n        #   username: ${mysql-server.username}\n        #   password: ${mysql-server.password}\n        #   driver-class-name: ${mysql-server.driver-class-name}\n\n    hikari:\n      type: com.zaxxer.hikari.HikariDataSource\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      url: jdbc:mysql://localhost:3306\n      username: ${mysql-server.username}\n      password: ${mysql-server.password}\n      minimum-idle: 5\n      idle-timeout: 600000\n      maximum-pool-size: 10\n      auto-commit: true\n      pool-name: MyHikariCP\n      max-lifetime: 1800000\n      connection-timeout: 30000\n      connection-test-query: SELECT 1\n\n# APIJSON的配置\nmysql-server:\n  database-name: quick-boot-online\n  defaultDatabase: MYSQL\n  db-version: 8.0.31', '06cf5d77cf8b023e82d6b3d98a56ac08', '2024-01-25 11:45:03', '2024-01-25 03:45:03', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (55, 100, 'quick-boot-online.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8083\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot-online\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n        quick-boot:\n          url: ${mysql-server.address}/quick-boot\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n\n# APIJSON的配置\nmysql-server:\n  database-name: quick-boot-online\n  defaultDatabase: MYSQL\n  db-version: 8.0.31', '8d36929760703328f4612c86b6f1b4e6', '2024-01-25 18:14:28', '2024-01-25 10:14:28', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (79, 101, 'quick-boot-flow.yml', 'DEFAULT_GROUP', '', 'server:\r\n  port: 8084\r\n\r\nspring:\r\n  datasource:\r\n    dynamic:\r\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\r\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\r\n      datasource:\r\n        master:\r\n          url: ${mysql-server.address}/quick-boot-flow\r\n          username: ${mysql-server.username}\r\n          password: ${mysql-server.password}\r\n          driver-class-name: ${mysql-server.driver-class-name}', '1fc4e318303b9882b17d2c61c609b017', '2024-01-26 11:58:12', '2024-01-26 03:58:13', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (79, 102, 'quick-boot-flow.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8084\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot-flow\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}', 'e3cf00dfbc90a500d374cbe16c34cd6a', '2024-01-26 11:58:22', '2024-01-26 03:58:23', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (79, 103, 'quick-boot-flow.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8084\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot-flow?nullCatalogMeansCurrent=true\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}', 'fcc7f6c4fcad3f500be3cc0bd4f88d02', '2024-01-26 13:51:43', '2024-01-26 05:51:44', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (0, 104, 'quick-boot-jimureport.yml', 'DEFAULT_GROUP', '', 'server:\r\n  port: 8085\r\n\r\nspring:\r\n  datasource:\r\n    dynamic:\r\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\r\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\r\n      datasource:\r\n        master:\r\n          url: ${mysql-server.address}/quick-boot-jimureport\r\n          username: ${mysql-server.username}\r\n          password: ${mysql-server.password}\r\n          driver-class-name: ${mysql-server.driver-class-name}', '1a8622fc33031d3201b87522006aa7bb', '2024-02-03 11:25:27', '2024-02-03 03:25:28', NULL, '0:0:0:0:0:0:0:1', 'I', 'dev', '');
INSERT INTO `his_config_info` VALUES (99, 105, 'quick-boot-jimureport.yml', 'DEFAULT_GROUP', '', 'server:\r\n  port: 8085\r\n\r\nspring:\r\n  datasource:\r\n    dynamic:\r\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\r\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\r\n      datasource:\r\n        master:\r\n          url: ${mysql-server.address}/quick-boot-jimureport\r\n          username: ${mysql-server.username}\r\n          password: ${mysql-server.password}\r\n          driver-class-name: ${mysql-server.driver-class-name}', '1a8622fc33031d3201b87522006aa7bb', '2024-02-03 11:55:05', '2024-02-03 03:55:06', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (99, 106, 'quick-boot-jimureport.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 8085\n\nspring:\n  datasource:\n    dynamic:\n      primary: master #设置默认的数据源或者数据源组,默认值即为master\n      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\n      datasource:\n        master:\n          url: ${mysql-server.address}/quick-boot-jimureport\n          username: ${mysql-server.username}\n          password: ${mysql-server.password}\n          driver-class-name: ${mysql-server.driver-class-name}\n\njeecg :\n  jmreport:\n    #多租户模式，默认值为空(created:按照创建人隔离、tenant:按照租户隔离) (v1.6.2+ 新增)\n    saasMode: tenant\n    # 平台上线安全配置(v1.6.2+ 新增)\n    firewall:\n      # 数据源安全 (开启后，不允许使用平台数据源、SQL解析加签并且不允许查询数据库)\n      dataSourceSafe: true\n      # 低代码开发模式（dev:开发模式，prod:发布模式—关闭在线报表设计功能，分配角色admin、lowdeveloper可以放开限制）\n      lowCodeMode: dev\n    #是否 禁用导出PDF和图片的按钮 默认为false\n    exportDisabled: false\n    #是否自动保存\n    autoSave: true\n    #自动保存间隔时间毫秒\n    interval: 20000\n    # 列数(设计页面展示多少列)\n    col: 100\n    #自定义项目前缀\n    #customPrePath:\n    # 自定义API接口的前缀 #{api_base_path}和#{domainURL}的值\n    #apiBasePath: http://localhost:8080/jeecg-boot\n    #预览分页自定义\n    pageSize:\n      - 10\n      - 20\n      - 30\n      - 40\n    #打印纸张自定义\n    printPaper:\n      - title: A5纸\n        size:\n          - 148\n          - 210\n      - title: B4纸\n        size:\n          - 250\n          - 353\n    #接口超时设置（毫秒）\n    connect-timeout: 300000\n    #Excel导出模式(fast/快、primary/精致模式，默认fast)\n    export-excel-pattern: fast\n    #Excel导出数据每个sheet的行数,每个sheet最大1048576行\n    page-size-number: 10000\n    #设计页面表格的线是否显示 默认true\n    line: true', '5704bc3411e2bcbe1b86303f2e1b0a3b', '2024-02-03 12:14:52', '2024-02-03 04:14:52', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (52, 107, 'quick-boot-gateway.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 9999\n\nspring:\n  cloud:\n    gateway:\n      routes:\n        - id: quick-boot-system\n          uri: lb://quick-boot-system\n          predicates:\n            - Path=/api/system/**\n          filters:\n            - StripPrefix=2\n        - id: quick-boot-oauth2\n          uri: lb://quick-boot-oauth2\n          predicates:\n            - Path=/api/auth/**\n          filters:\n            - StripPrefix=2\n        - id: quick-boot-online\n          uri: lb://quick-boot-online\n          predicates:\n            - Path=/api/online/**\n          filters:\n            - StripPrefix=2\n\nknife4j:\n  gateway:\n    enabled: true\n    # 指定服务发现的模式聚合微服务文档，并且是默认`default`分组\n    strategy: discover\n    discover:\n      enabled: true\n      # 指定版本号(Swagger2|OpenAPI3)\n      version : openapi3\n      # 需要排除的微服务(eg:网关服务)\n      excluded-services:\n        - quick-boot-gateway\n\n# 忽略认证的地址\nignore:\n  url:\n    - /favicon.ico\n    - /doc.html\n    - /api/system/v3/api-docs/**\n    - /api/auth/v3/api-docs/**\n    - /webjars/**\n    - /swagger-resources/**\n    - /v3/api-docs/**\n    - /actuator/**\n    - /instances/**\n    - /api/system/menu/getRoutes\n    - /api/auth/doLogin\n    - /api/auth/oauth2/**', '1650c16ba6f42dd4ad31b6fa089c062a', '2024-02-03 13:58:15', '2024-02-03 05:58:15', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (52, 108, 'quick-boot-gateway.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 9999\n\nspring:\n  cloud:\n    gateway:\n      routes:\n        - id: quick-boot-system\n          uri: lb://quick-boot-system\n          predicates:\n            - Path=/api/system/**\n          filters:\n            - StripPrefix=2\n        - id: quick-boot-oauth2\n          uri: lb://quick-boot-oauth2\n          predicates:\n            - Path=/api/auth/**\n          filters:\n            - StripPrefix=2\n        - id: quick-boot-online\n          uri: lb://quick-boot-online\n          predicates:\n            - Path=/api/online/**\n          filters:\n            - StripPrefix=2\n        - id: quick-boot-jimureport\n          uri: lb://quick-boot-jimureport\n          predicates:\n            - Path=/jmreport/**\n          #filters:\n          #  - StripPrefix=2\n\nknife4j:\n  gateway:\n    enabled: true\n    # 指定服务发现的模式聚合微服务文档，并且是默认`default`分组\n    strategy: discover\n    discover:\n      enabled: true\n      # 指定版本号(Swagger2|OpenAPI3)\n      version : openapi3\n      # 需要排除的微服务(eg:网关服务)\n      excluded-services:\n        - quick-boot-gateway\n\n# 忽略认证的地址\nignore:\n  url:\n    - /favicon.ico\n    - /doc.html\n    - /api/system/v3/api-docs/**\n    - /api/auth/v3/api-docs/**\n    - /webjars/**\n    - /swagger-resources/**\n    - /v3/api-docs/**\n    - /actuator/**\n    - /instances/**\n    - /api/system/menu/getRoutes\n    - /api/auth/doLogin\n    - /api/auth/oauth2/**', 'a586b3ff70a1fec370d463fb58d96061', '2024-02-03 14:04:11', '2024-02-03 06:04:12', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (52, 109, 'quick-boot-gateway.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 9999\n\nspring:\n  cloud:\n    gateway:\n      routes:\n        - id: quick-boot-system\n          uri: lb://quick-boot-system\n          predicates:\n            - Path=/api/system/**\n          filters:\n            - StripPrefix=2\n        - id: quick-boot-oauth2\n          uri: lb://quick-boot-oauth2\n          predicates:\n            - Path=/api/auth/**\n          filters:\n            - StripPrefix=2\n        - id: quick-boot-online\n          uri: lb://quick-boot-online\n          predicates:\n            - Path=/api/online/**\n          filters:\n            - StripPrefix=2\n        - id: quick-boot-jimureport\n          uri: lb://quick-boot-jimureport\n          predicates:\n            - Path=/jmreport/**\n          #filters:\n          #  - StripPrefix=2\n\nknife4j:\n  gateway:\n    enabled: true\n    # 指定服务发现的模式聚合微服务文档，并且是默认`default`分组\n    strategy: discover\n    discover:\n      enabled: true\n      # 指定版本号(Swagger2|OpenAPI3)\n      version : openapi3\n      # 需要排除的微服务(eg:网关服务)\n      excluded-services:\n        - quick-boot-gateway\n\n# 忽略认证的地址\nignore:\n  url:\n    - /favicon.ico\n    - /doc.html\n    - /api/system/v3/api-docs/**\n    - /api/auth/v3/api-docs/**\n    - /webjars/**\n    - /swagger-resources/**\n    - /v3/api-docs/**\n    - /actuator/**\n    - /instances/**\n    - /api/system/menu/getRoutes\n    - /api/auth/doLogin\n    - /api/auth/oauth2/**\n    - /jmreport/**', 'd5b1303f4294498a97a69cf0df5bc081', '2024-02-04 14:56:15', '2024-02-04 06:56:15', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (52, 110, 'quick-boot-gateway.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 9999\n\nspring:\n  cloud:\n    gateway:\n      routes:\n        - id: quick-boot-system\n          uri: lb://quick-boot-system\n          predicates:\n            - Path=/api/system/**\n          filters:\n            - StripPrefix=2\n        - id: quick-boot-oauth2\n          uri: lb://quick-boot-oauth2\n          predicates:\n            - Path=/api/auth/**\n          filters:\n            - StripPrefix=2\n        - id: quick-boot-online\n          uri: lb://quick-boot-online\n          predicates:\n            - Path=/api/online/**\n          filters:\n            - StripPrefix=2\n        - id: quick-boot-jimureport\n          uri: lb://quick-boot-jimureport\n          predicates:\n            - Path=/jmreport/**\n          #filters:\n          #  - StripPrefix=2\n\nknife4j:\n  gateway:\n    enabled: true\n    # 指定服务发现的模式聚合微服务文档，并且是默认`default`分组\n    strategy: discover\n    discover:\n      enabled: true\n      # 指定版本号(Swagger2|OpenAPI3)\n      version : openapi3\n      # 需要排除的微服务(eg:网关服务)\n      excluded-services:\n        - quick-boot-gateway\n\n# 忽略认证的地址\nignore:\n  url:\n    - /favicon.ico\n    - /doc.html\n    - /api/system/v3/api-docs/**\n    - /api/auth/v3/api-docs/**\n    - /webjars/**\n    - /swagger-resources/**\n    - /v3/api-docs/**\n    - /actuator/**\n    - /instances/**\n    - /api/system/menu/getRoutes\n    - /api/auth/doLogin\n    - /api/auth/oauth2/**\n    - /jmreport/**\n    - /api/system/tenant/list', '1bbca25570fd774208b1a47f9225ab22', '2024-02-04 14:57:23', '2024-02-04 06:57:24', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (52, 111, 'quick-boot-gateway.yml', 'DEFAULT_GROUP', '', 'server:\n  port: 9999\n\nspring:\n  cloud:\n    gateway:\n      routes:\n        - id: quick-boot-system\n          uri: lb://quick-boot-system\n          predicates:\n            - Path=/api/system/**\n          filters:\n            - StripPrefix=2\n        - id: quick-boot-oauth2\n          uri: lb://quick-boot-oauth2\n          predicates:\n            - Path=/api/auth/**\n          filters:\n            - StripPrefix=2\n        - id: quick-boot-online\n          uri: lb://quick-boot-online\n          predicates:\n            - Path=/api/online/**\n          filters:\n            - StripPrefix=2\n        - id: quick-boot-jimureport\n          uri: lb://quick-boot-jimureport\n          predicates:\n            - Path=/jmreport/**\n          #filters:\n          #  - StripPrefix=2\n\nknife4j:\n  gateway:\n    enabled: true\n    # 指定服务发现的模式聚合微服务文档，并且是默认`default`分组\n    strategy: discover\n    discover:\n      enabled: true\n      # 指定版本号(Swagger2|OpenAPI3)\n      version : openapi3\n      # 需要排除的微服务(eg:网关服务)\n      excluded-services:\n        - quick-boot-gateway\n\n# 忽略认证的地址\nignore:\n  url:\n    - /favicon.ico\n    - /doc.html\n    - /api/system/v3/api-docs/**\n    - /api/auth/v3/api-docs/**\n    - /webjars/**\n    - /swagger-resources/**\n    - /v3/api-docs/**\n    - /actuator/**\n    - /instances/**\n    - /api/system/menu/getRoutes\n    - /api/auth/doLogin\n    - /api/auth/oauth2/**\n    - /jmreport/**\n', 'ec1d3534d43b81330a5a87418793b2a8', '2024-02-04 15:00:56', '2024-02-04 07:00:56', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  UNIQUE INDEX `uk_role_permission`(`role` ASC, `resource` ASC, `action` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  UNIQUE INDEX `idx_user_role`(`username` ASC, `role` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '租户容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp` ASC, `tenant_id` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'tenant_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
INSERT INTO `tenant_info` VALUES (2, '1', 'dev', 'dev', '开发环境', 'nacos', 1674569029205, 1674569029205);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;
