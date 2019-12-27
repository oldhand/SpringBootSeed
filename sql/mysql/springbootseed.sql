/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3306
 Source Schema         : springbootseed

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 27/12/2019 14:46:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for application
-- ----------------------------
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appid` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '应用ID',
  `secret` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '秘钥',
  `description` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `published` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`appid`),
  UNIQUE KEY `UK_bqiag6217cgw30yrf6pg8i3ck` (`appid`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用表';

-- ----------------------------
-- Records of application
-- ----------------------------
BEGIN;
INSERT INTO `application` VALUES ('demo', '4c35458e913efbcf86ef621d22387b10', 'Demo', '2019-07-01 00:00:00', 0, NULL);
COMMIT;

-- ----------------------------
-- Table structure for content_ids
-- ----------------------------
DROP TABLE IF EXISTS `content_ids`;
CREATE TABLE `content_ids` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `xn_type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for gen_config
-- ----------------------------
DROP TABLE IF EXISTS `gen_config`;
CREATE TABLE `gen_config` (
  `id` bigint(20) NOT NULL,
  `api_path` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `cover` bit(1) DEFAULT NULL,
  `module_name` varchar(255) DEFAULT NULL,
  `pack` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `prefix` varchar(255) DEFAULT NULL,
  `package_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gen_config
-- ----------------------------
BEGIN;
INSERT INTO `gen_config` VALUES (1, '/raid5/gits/tests/', 'oldhand', b'1', 'samples', 'com.hello', '/raid5/gits/tests/', '', 'com.github.rabbitmq');
COMMIT;

-- ----------------------------
-- Table structure for local_storage
-- ----------------------------
DROP TABLE IF EXISTS `local_storage`;
CREATE TABLE `local_storage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `md5` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `operate` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `published` datetime DEFAULT NULL,
  `real_name` varchar(255) DEFAULT NULL,
  `size` varchar(255) DEFAULT NULL,
  `source_name` varchar(255) DEFAULT NULL,
  `suffix` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_epjuyojj02kd2j0fqk34mabhf` (`md5`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `browser` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `exception_detail` text,
  `log_type` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `params` text,
  `request_ip` varchar(255) DEFAULT NULL,
  `time` bigint(20) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=844 DEFAULT CHARSET=utf8;
 

-- ----------------------------
-- Table structure for mq
-- ----------------------------
DROP TABLE IF EXISTS `mq`;
CREATE TABLE `mq` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '队列ID',
  `published` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建日期',
  `name` varchar(255) NOT NULL COMMENT '队列名称',
  `message` longtext NOT NULL COMMENT '消息体',
  `result` longtext COMMENT '响应体',
  `ack` int(255) NOT NULL COMMENT '响应代码',
  `acktime` timestamp(6) NULL DEFAULT NULL COMMENT '响应时间',
  `uniquekey` varchar(255) NOT NULL COMMENT '唯一键',
  `islock` tinyint(1) NOT NULL COMMENT '是否锁定',
  `isasync` tinyint(1) NOT NULL COMMENT '是否同步',
  PRIMARY KEY (`id`),
  UNIQUE KEY `mq_uniquekey` (`uniquekey`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8;

 
-- ----------------------------
-- Table structure for profile
-- ----------------------------
DROP TABLE IF EXISTS `profile`;
CREATE TABLE `profile` (
  `identifier` bigint(20) NOT NULL AUTO_INCREMENT,
  `id` varchar(64) NOT NULL COMMENT '用户ID',
  `username` varchar(128) NOT NULL COMMENT '用户名称',
  `published` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建日期',
  `updated` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新日期',
  `type` varchar(64) NOT NULL COMMENT '用户类型',
  `regioncode` varchar(64) NOT NULL COMMENT '国家代码',
  `mobile` varchar(64) NOT NULL COMMENT '手机号码',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `givenname` varchar(64) DEFAULT NULL COMMENT '昵称',
  `status` tinyint(1) DEFAULT NULL COMMENT '用户状态',
  `email` varchar(64) DEFAULT NULL COMMENT '用户邮箱',
  `link` varchar(64) DEFAULT NULL COMMENT '用户头像地址',
  `gender` varchar(64) DEFAULT NULL COMMENT '性别',
  `country` varchar(64) DEFAULT NULL COMMENT '所属国家',
  `region` varchar(64) DEFAULT NULL COMMENT '地区',
  `birthdate` varchar(64) DEFAULT NULL COMMENT '出生日期',
  `province` varchar(64) DEFAULT NULL COMMENT '省份',
  `city` varchar(64) DEFAULT NULL COMMENT '城市',
  `realname` varchar(64) DEFAULT NULL COMMENT '真实姓名',
  `identitycard` varchar(64) DEFAULT NULL COMMENT '身份证号码',
  `reg_ip` varchar(64) DEFAULT NULL COMMENT '注册IP',
  `system` varchar(128) DEFAULT NULL COMMENT '注册系统',
  `browser` varchar(128) DEFAULT NULL COMMENT '注册浏览器',
  PRIMARY KEY (`identifier`) USING BTREE,
  UNIQUE KEY `profile_id` (`id`) USING BTREE,
  UNIQUE KEY `profile_username` (`username`) USING BTREE,
  UNIQUE KEY `profile_type_regincode_mobile` (`type`,`regioncode`,`mobile`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for quartz_job
-- ----------------------------
DROP TABLE IF EXISTS `quartz_job`;
CREATE TABLE `quartz_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bean_name` varchar(255) DEFAULT NULL COMMENT 'Spring Bean名称',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT 'cron 表达式',
  `is_pause` bit(1) DEFAULT NULL COMMENT '状态：1暂停、0启用',
  `job_name` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `method_name` varchar(255) DEFAULT NULL COMMENT '方法名称',
  `params` varchar(255) DEFAULT NULL COMMENT '参数',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of quartz_job
-- ----------------------------
BEGIN;
INSERT INTO `quartz_job` VALUES (1, 'testTask', '0/5 * * * * ?', b'1', '带参测试', 'run1', 'test', '带参测试，多参使用json', '2019-08-22 14:08:29');
INSERT INTO `quartz_job` VALUES (2, 'testTask', '0/5 * * * * ?', b'1', '测试', 'run', '', '不带参测试', '2019-09-26 16:44:39');
COMMIT;

-- ----------------------------
-- Table structure for quartz_log
-- ----------------------------
DROP TABLE IF EXISTS `quartz_log`;
CREATE TABLE `quartz_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `baen_name` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `cron_expression` varchar(255) DEFAULT NULL,
  `exception_detail` text,
  `is_success` bit(1) DEFAULT NULL,
  `job_name` varchar(255) DEFAULT NULL,
  `method_name` varchar(255) DEFAULT NULL,
  `params` varchar(255) DEFAULT NULL,
  `time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3173 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `avatar_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `dept_id` bigint(20) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `job_id` bigint(20) DEFAULT NULL,
  `last_password_reset_time` datetime DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
