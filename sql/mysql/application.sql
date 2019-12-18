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

 Date: 18/12/2019 14:45:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for application
-- ----------------------------
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `appid` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '应用ID',
  `secret` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '秘钥',
  `description` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `published` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`appid`),
  UNIQUE KEY `UK_9q5v0sivpmi0ruax0gweere69` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用表';

-- ----------------------------
-- Records of application
-- ----------------------------
BEGIN;
INSERT INTO `application` VALUES (0,'demo', '4c35458e913efbcf86ef621d22387b10', 'Demo', '2019-07-01 00:00:00', 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
