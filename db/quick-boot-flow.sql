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

 Date: 02/06/2024 23:23:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for flow_definition
-- ----------------------------
DROP TABLE IF EXISTS `flow_definition`;
CREATE TABLE `flow_definition`  (
  `id` bigint UNSIGNED NOT NULL COMMENT '主键id',
  `flow_code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程编码',
  `flow_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程名称',
  `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程版本',
  `is_publish` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否发布（0未发布 1已发布 9失效）',
  `from_custom` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '审批表单是否自定义（Y是 N否）',
  `from_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批表单路径',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志',
  `tenant_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `flow_code_version`(`flow_code` ASC, `version` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程定义表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flow_definition
-- ----------------------------
INSERT INTO `flow_definition` VALUES (1240451602826006528, '999', '99', '9', 0, 'N', NULL, '2024-05-15 15:51:49', '2024-05-15 15:51:49', '0', '1');
INSERT INTO `flow_definition` VALUES (1790761835389313025, 'test', '1', '1', 0, 'N', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `flow_definition` VALUES (1790763197414043650, 'ggg', 'GG', '1', 0, 'N', NULL, NULL, NULL, '0', NULL);
INSERT INTO `flow_definition` VALUES (1790765997636222978, '666', '66', '1', 0, 'N', NULL, NULL, NULL, '0', '1');

-- ----------------------------
-- Table structure for flow_his_task
-- ----------------------------
DROP TABLE IF EXISTS `flow_his_task`;
CREATE TABLE `flow_his_task`  (
  `id` bigint UNSIGNED NOT NULL COMMENT '主键id',
  `definition_id` bigint NOT NULL COMMENT '对应flow_definition表的id',
  `instance_id` bigint NOT NULL COMMENT '对应flow_instance表的id',
  `node_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开始节点编码',
  `node_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开始节点名称',
  `node_type` tinyint(1) NULL DEFAULT NULL COMMENT '开始节点类型（0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关）',
  `target_node_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目标节点编码',
  `target_node_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结束节点名称',
  `approver` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批者',
  `permission_flag` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识（权限类型:权限标识，可以多个，如role:1,role:2)',
  `flow_status` tinyint(1) NOT NULL COMMENT '流程状态（0待提交 1审批中 2 审批通过 8已完成 9已退回 10失效）',
  `message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批意见',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志',
  `tenant_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '历史任务记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flow_his_task
-- ----------------------------

-- ----------------------------
-- Table structure for flow_instance
-- ----------------------------
DROP TABLE IF EXISTS `flow_instance`;
CREATE TABLE `flow_instance`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `definition_id` bigint NOT NULL COMMENT '对应flow_definition表的id',
  `business_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务id',
  `node_type` tinyint(1) NOT NULL COMMENT '结点类型（0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关）',
  `node_code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程节点编码',
  `node_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程节点名称',
  `variable` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '任务变量',
  `flow_status` tinyint(1) NOT NULL COMMENT '流程状态（0待提交 1审批中 2 审批通过 8已完成 9已退回 10失效）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `ext` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扩展字段',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志',
  `tenant_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程实例表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flow_instance
-- ----------------------------

-- ----------------------------
-- Table structure for flow_node
-- ----------------------------
DROP TABLE IF EXISTS `flow_node`;
CREATE TABLE `flow_node`  (
  `id` bigint UNSIGNED NOT NULL COMMENT '主键id',
  `node_type` tinyint(1) NOT NULL COMMENT '节点类型（0开始节点 1中间节点 2结束结点 3互斥网关 4并行网关）',
  `definition_id` bigint NOT NULL COMMENT '流程定义id',
  `node_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程节点编码',
  `node_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程节点名称',
  `permission_flag` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识（权限类型:权限标识，可以多个，如role:1,role:2)',
  `coordinate` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '坐标',
  `skip_any_node` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '是否可以退回任意节点（Y是 N否）',
  `listener_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '监听器类型',
  `listener_path` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '监听器路径',
  `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '版本',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志',
  `tenant_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `info_id_code`(`definition_id` ASC, `node_code` ASC) USING BTREE COMMENT '保证一个流程中node_code是唯一的'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程结点表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flow_node
-- ----------------------------
INSERT INTO `flow_node` VALUES (1246498114894958593, 0, 1240451602826006528, 'Event_2fb1984', '开始', NULL, '250,140|250,180', 'N', NULL, NULL, '9', '2024-06-01 08:18:29', '2024-06-01 08:18:29', '0', '1');
INSERT INTO `flow_node` VALUES (1246498114894958595, 1, 1240451602826006528, 'Activity_63ded5c', '用户任务', NULL, '530,190|530,190', 'N', NULL, NULL, '9', '2024-06-01 08:18:29', '2024-06-01 08:18:29', '0', '1');
INSERT INTO `flow_node` VALUES (1246498114894958597, 2, 1240451602826006528, 'Event_d678d35', '结束', NULL, '810,250|810,290', 'N', NULL, NULL, '9', '2024-06-01 08:18:29', '2024-06-01 08:18:29', '0', '1');

-- ----------------------------
-- Table structure for flow_skip
-- ----------------------------
DROP TABLE IF EXISTS `flow_skip`;
CREATE TABLE `flow_skip`  (
  `id` bigint UNSIGNED NOT NULL COMMENT '主键id',
  `definition_id` bigint NOT NULL COMMENT '流程定义id',
  `now_node_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '当前流程节点的编码',
  `now_node_type` tinyint(1) NULL DEFAULT NULL COMMENT '当前节点类型（0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关）',
  `next_node_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '下一个流程节点的编码',
  `next_node_type` tinyint(1) NULL DEFAULT NULL COMMENT '下一个节点类型（0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关）',
  `skip_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转名称',
  `skip_type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转类型（PASS审批通过 REJECT退回）',
  `skip_condition` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转条件',
  `coordinate` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '坐标',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志',
  `tenant_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '结点跳转关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flow_skip
-- ----------------------------
INSERT INTO `flow_skip` VALUES (1246498114894958594, 1240451602826006528, 'Event_2fb1984', 0, 'Activity_63ded5c', 1, NULL, NULL, NULL, '268,140;298,140;298,190;480,190', '2024-06-01 08:18:29', '2024-06-01 08:18:29', '0', '1');
INSERT INTO `flow_skip` VALUES (1246498114894958596, 1240451602826006528, 'Activity_63ded5c', 1, 'Event_d678d35', 2, NULL, NULL, NULL, '580,190;762,190;762,250;792,250', '2024-06-01 08:18:29', '2024-06-01 08:18:29', '0', '1');

-- ----------------------------
-- Table structure for flow_task
-- ----------------------------
DROP TABLE IF EXISTS `flow_task`;
CREATE TABLE `flow_task`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `definition_id` bigint NOT NULL COMMENT '对应flow_definition表的id',
  `instance_id` bigint NOT NULL COMMENT '对应flow_instance表的id',
  `node_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点编码',
  `node_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点名称',
  `node_type` tinyint(1) NOT NULL COMMENT '节点类型（0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关）',
  `permission_flag` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识（权限类型:权限标识，可以多个，如role:1,role:2)',
  `flow_status` tinyint(1) NOT NULL COMMENT '流程状态（0待提交 1审批中 2 审批通过 8已完成 9已退回 10失效）',
  `approver` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批者',
  `assignee` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转办人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志',
  `tenant_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '待办任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flow_task
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
