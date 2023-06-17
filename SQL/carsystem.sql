/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : localhost:3306
 Source Schema         : vehiclesystem

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 17/06/2023 21:59:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for car
-- ----------------------------
DROP TABLE IF EXISTS `car`;
CREATE TABLE `car`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '车辆id',
  `userId` int(11) NOT NULL COMMENT '车主id',
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车辆型号',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车辆描述',
  `code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车牌号',
  `color` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车辆颜色',
  `deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0为存在，1为删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `userId`(`userId`) USING BTREE,
  CONSTRAINT `car_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for component
-- ----------------------------
DROP TABLE IF EXISTS `component`;
CREATE TABLE `component`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '零件id',
  `number` int(11) NULL DEFAULT NULL COMMENT '零件库存',
  `inprice` double(10, 2) NULL DEFAULT NULL COMMENT '零件进货价格',
  `outprice` double(10, 2) NULL DEFAULT NULL COMMENT '零件出售价格',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '零件功能描述',
  `deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0为存在，1为删除）',
  `version` int(255) NOT NULL DEFAULT 0 COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_component
-- ----------------------------
DROP TABLE IF EXISTS `order_component`;
CREATE TABLE `order_component`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '关系表id',
  `orderId` int(11) NULL DEFAULT NULL COMMENT '订单id',
  `comId` int(11) NULL DEFAULT NULL COMMENT '零件id',
  `deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0为存在，1为删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `orderId`(`orderId`) USING BTREE,
  INDEX `comId`(`comId`) USING BTREE,
  CONSTRAINT `order_component_ibfk_1` FOREIGN KEY (`orderId`) REFERENCES `theorder` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `order_component_ibfk_2` FOREIGN KEY (`comId`) REFERENCES `component` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for theorder
-- ----------------------------
DROP TABLE IF EXISTS `theorder`;
CREATE TABLE `theorder`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单号',
  `carId` int(11) NOT NULL COMMENT '车辆id',
  `situation` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车辆情况',
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '订单创建日期',
  `status` tinyint(4) NULL DEFAULT 0 COMMENT '订单状态（0未完成，1已完成）',
  `price` double(10, 2) NULL DEFAULT NULL COMMENT '订单总价',
  `deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0为存在，1为删除）',
  `version` int(255) NOT NULL DEFAULT 0 COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `carId`(`carId`) USING BTREE,
  CONSTRAINT `theorder_ibfk_1` FOREIGN KEY (`carId`) REFERENCES `car` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户/管理员的id',
  `marker` int(11) NULL DEFAULT 0 COMMENT '0为用户，1为管理员，2为超级管理员',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录的用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录的密码',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户的电话号码',
  `gender` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户的性别',
  `grade` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '用户会员等级：0普通用户，1普通会员（90%），2高级会员(80%)',
  `deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0为存在，1为删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
