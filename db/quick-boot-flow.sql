/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : quick-boot-flow

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 26/02/2024 11:50:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for flow_definition
-- ----------------------------
DROP TABLE IF EXISTS `flow_definition`;
CREATE TABLE `flow_definition`  (
                                    `id` bigint NOT NULL COMMENT '主键',
                                    `flow_module_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '流程模型id',
                                    `flow_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '流程名称',
                                    `flow_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '流程业务标识',
                                    `flow_model` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '表单定义',
                                    `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态(1.初始态 2.编辑中 3.已下线)',
                                    `operator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作人',
                                    `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
                                    `archive` tinyint NOT NULL DEFAULT 0 COMMENT '归档状态(0未删除，1删除)',
                                    `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
                                    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                    `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
                                    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                    `del_flag` tinyint NULL DEFAULT 0 COMMENT '删除状态',
                                    `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户ID',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    UNIQUE INDEX `uniq_flow_module_id`(`flow_module_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程定义表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for flow_deployment
-- ----------------------------
DROP TABLE IF EXISTS `flow_deployment`;
CREATE TABLE `flow_deployment`  (
                                    `id` bigint NOT NULL COMMENT '主键',
                                    `flow_deploy_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '流程模型部署id',
                                    `flow_module_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '流程模型id',
                                    `flow_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '流程名称',
                                    `flow_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '流程业务标识',
                                    `flow_model` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '表单定义',
                                    `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态(1.已部署 3.已下线)',
                                    `operator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作人',
                                    `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
                                    `archive` tinyint NOT NULL DEFAULT 0 COMMENT '归档状态(0未删除，1删除)',
                                    `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
                                    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                    `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
                                    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                    `del_flag` tinyint NULL DEFAULT 0 COMMENT '删除状态',
                                    `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户ID',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    UNIQUE INDEX `uniq_flow_deploy_id`(`flow_deploy_id` ASC) USING BTREE,
                                    INDEX `idx_flow_module_id`(`flow_module_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程部署表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for flow_instance
-- ----------------------------
DROP TABLE IF EXISTS `flow_instance`;
CREATE TABLE `flow_instance`  (
                                  `id` bigint NOT NULL COMMENT '主键',
                                  `flow_instance_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '流程执行实例id',
                                  `parent_flow_instance_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '父流程执行实例id',
                                  `flow_deploy_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '流程模型部署id',
                                  `flow_module_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '流程模型id',
                                  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态(1.执行完成 2.执行中 3.执行终止(强制终止))',
                                  `archive` tinyint NOT NULL DEFAULT 0 COMMENT '归档状态(0未删除，1删除)',
                                  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
                                  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
                                  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                  `del_flag` tinyint NULL DEFAULT 0 COMMENT '删除状态',
                                  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户ID',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  UNIQUE INDEX `uniq_flow_instance_id`(`flow_instance_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程执行实例表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for flow_instance_data
-- ----------------------------
DROP TABLE IF EXISTS `flow_instance_data`;
CREATE TABLE `flow_instance_data`  (
                                       `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                       `node_instance_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '节点执行实例id',
                                       `flow_instance_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '流程执行实例id',
                                       `instance_data_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '实例数据id',
                                       `flow_deploy_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '流程模型部署id',
                                       `flow_module_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '流程模型id',
                                       `node_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '节点唯一标识',
                                       `instance_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '数据列表json',
                                       `type` tinyint NOT NULL DEFAULT 0 COMMENT '操作类型(1.实例初始化 2.系统执行 3.系统主动获取 4.上游更新 5.任务提交 6.任务撤回)',
                                       `archive` tinyint NOT NULL DEFAULT 0 COMMENT '归档状态(0未删除，1删除)',
                                       `caller` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '调用方',
                                       `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
                                       `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                       `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
                                       `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                       `del_flag` tinyint NULL DEFAULT 0 COMMENT '删除状态',
                                       `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户ID',
                                       PRIMARY KEY (`id`) USING BTREE,
                                       UNIQUE INDEX `uniq_instance_data_id`(`instance_data_id` ASC) USING BTREE,
                                       INDEX `idx_flow_instance_id`(`flow_instance_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '实例数据表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for flow_instance_mapping
-- ----------------------------
DROP TABLE IF EXISTS `flow_instance_mapping`;
CREATE TABLE `flow_instance_mapping`  (
                                          `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                          `flow_instance_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '流程执行实例id',
                                          `node_instance_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '节点执行实例id',
                                          `node_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '节点唯一标识',
                                          `sub_flow_instance_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '子流程执行实例id',
                                          `type` tinyint NOT NULL DEFAULT 0 COMMENT '状态(1.执行 2.回滚)',
                                          `archive` tinyint NOT NULL DEFAULT 0 COMMENT '归档状态(0未删除，1删除)',
                                          `caller` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'optimus-prime' COMMENT '调用方',
                                          `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
                                          `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                          `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
                                          `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                          `del_flag` tinyint NULL DEFAULT 0 COMMENT '删除状态',
                                          `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户ID',
                                          PRIMARY KEY (`id`) USING BTREE,
                                          INDEX `idx_fii`(`flow_instance_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '父子流程实例映射表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for flow_node_instance
-- ----------------------------
DROP TABLE IF EXISTS `flow_node_instance`;
CREATE TABLE `flow_node_instance`  (
                                       `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                       `node_instance_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '节点执行实例id',
                                       `flow_instance_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '流程执行实例id',
                                       `source_node_instance_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '上一个节点执行实例id',
                                       `instance_data_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '实例数据id',
                                       `flow_deploy_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '流程模型部署id',
                                       `node_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '节点唯一标识',
                                       `source_node_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '上一个流程节点唯一标识',
                                       `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态(1.处理成功 2.处理中 3.处理失败 4.处理已撤销)',
                                       `archive` tinyint NOT NULL DEFAULT 0 COMMENT '归档状态(0未删除，1删除)',
                                       `caller` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '调用方',
                                       `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
                                       `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                       `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
                                       `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                       `del_flag` tinyint NULL DEFAULT 0 COMMENT '删除状态',
                                       `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户ID',
                                       PRIMARY KEY (`id`) USING BTREE,
                                       UNIQUE INDEX `uniq_node_instance_id`(`node_instance_id` ASC) USING BTREE,
                                       INDEX `idx_fiid_sniid_nk`(`flow_instance_id` ASC, `source_node_instance_id` ASC, `node_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '节点执行实例表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for flow_node_instance_log
-- ----------------------------
DROP TABLE IF EXISTS `flow_node_instance_log`;
CREATE TABLE `flow_node_instance_log`  (
                                           `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                           `node_instance_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '节点执行实例id',
                                           `flow_instance_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '流程执行实例id',
                                           `instance_data_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '实例数据id',
                                           `node_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '节点唯一标识',
                                           `type` tinyint NOT NULL DEFAULT 0 COMMENT '操作类型(1.系统执行 2.任务提交 3.任务撤销)',
                                           `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态(1.处理成功 2.处理中 3.处理失败 4.处理已撤销)',
                                           `archive` tinyint NOT NULL DEFAULT 0 COMMENT '归档状态(0未删除，1删除)',
                                           `caller` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '调用方',
                                           `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
                                           `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                           `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
                                           `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                           `del_flag` tinyint NULL DEFAULT 0 COMMENT '删除状态',
                                           `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户ID',
                                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '节点执行记录表' ROW_FORMAT = COMPACT;

SET FOREIGN_KEY_CHECKS = 1;
