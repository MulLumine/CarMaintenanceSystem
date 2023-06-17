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

 Date: 17/06/2023 17:24:24
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
-- Records of car
-- ----------------------------
INSERT INTO `car` VALUES (3, 1, '宝马', '描述', 'K11223', '黑色', 0);
INSERT INTO `car` VALUES (4, 5, '奥迪', '你好呀', 'A11122', '白色', 0);
INSERT INTO `car` VALUES (5, 1, '奥迪', '这是一个奥迪', 'A23423', '红色', 0);
INSERT INTO `car` VALUES (6, 6, '红旗', '这是一个红旗', 'BB3423', '红色', 0);
INSERT INTO `car` VALUES (7, 7, '劳斯莱斯', '非常干净', 'JK1235', '白色', 0);
INSERT INTO `car` VALUES (8, 8, '奔驰', '有点脏脏的', 'K9M30', '黑色', 0);
INSERT INTO `car` VALUES (9, 10, '本田', '崭新出厂', 'M10A1', '银色', 0);
INSERT INTO `car` VALUES (10, 1, '路虎', '崭新出厂', '京A29B32', '黑色', 0);

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
-- Records of component
-- ----------------------------
INSERT INTO `component` VALUES (1, 50, 20.00, 40.00, '小装饰A', 0, 1);
INSERT INTO `component` VALUES (2, 80, 200.00, 300.00, '轮胎A', 0, 0);
INSERT INTO `component` VALUES (3, 500, 300.00, 350.00, '车漆B', 0, 0);
INSERT INTO `component` VALUES (4, 20, 200.00, 300.00, '火花塞B', 0, 0);
INSERT INTO `component` VALUES (5, 139, 50.00, 200.00, '后视镜A', 0, 0);
INSERT INTO `component` VALUES (6, 139, 60.00, 300.00, '后视镜B', 0, 0);
INSERT INTO `component` VALUES (7, 80, 10.00, 500.00, '小飞棍', 0, 0);

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
-- Records of order_component
-- ----------------------------
INSERT INTO `order_component` VALUES (1, 1, 2, 1);
INSERT INTO `order_component` VALUES (2, 1, 3, 1);
INSERT INTO `order_component` VALUES (3, 1, 6, 1);
INSERT INTO `order_component` VALUES (4, 2, 2, 0);
INSERT INTO `order_component` VALUES (5, 2, 7, 0);
INSERT INTO `order_component` VALUES (7, 3, 7, 0);
INSERT INTO `order_component` VALUES (8, 13, 1, 0);
INSERT INTO `order_component` VALUES (9, 13, 2, 0);
INSERT INTO `order_component` VALUES (10, 13, 3, 0);
INSERT INTO `order_component` VALUES (11, 14, 4, 0);
INSERT INTO `order_component` VALUES (12, 14, 6, 0);
INSERT INTO `order_component` VALUES (33, 1, 2, 1);
INSERT INTO `order_component` VALUES (34, 1, 7, 1);
INSERT INTO `order_component` VALUES (35, 1, 1, 1);
INSERT INTO `order_component` VALUES (36, 1, 2, 0);
INSERT INTO `order_component` VALUES (37, 1, 7, 0);
INSERT INTO `order_component` VALUES (38, 1, 1, 0);
INSERT INTO `order_component` VALUES (39, 1, 5, 0);
INSERT INTO `order_component` VALUES (40, 1, 6, 0);
INSERT INTO `order_component` VALUES (45, 16, 4, 0);
INSERT INTO `order_component` VALUES (46, 16, 5, 0);
INSERT INTO `order_component` VALUES (47, 16, 6, 0);

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
-- Records of theorder
-- ----------------------------
INSERT INTO `theorder` VALUES (1, 3, '零件是', '2023-06-17 13:59:50', 0, 500.00, 0, 0);
INSERT INTO `theorder` VALUES (2, 5, '换轮胎', '2023-06-17 13:58:42', 0, 3001.00, 0, 0);
INSERT INTO `theorder` VALUES (3, 9, '保养', '2023-06-16 14:44:16', 0, 200.00, 0, 0);
INSERT INTO `theorder` VALUES (13, 3, '我是傻呗', '2023-06-17 13:59:43', 0, 199.00, 0, 0);
INSERT INTO `theorder` VALUES (14, 5, '测试一下', '2023-06-16 19:36:17', 0, 600.00, 0, 0);
INSERT INTO `theorder` VALUES (16, 10, '路虎再次测试', '2023-06-17 17:10:48', 0, 800.00, 0, 0);

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

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 2, 'miumiu', '$2a$10$fBL3qGFFR3GJhvzlYUSa6.TYgNVZR8zsi9h68AKYNIMLwfNeLAvWu', '缪缪', '133', '女', '0', 0);
INSERT INTO `user` VALUES (5, 1, 'aaa', '$2a$10$Vl2knhxEAjNPjA5dYwmxQ.bQGHpxvAGTjN9j4G.zg/Zs8qMxliJ66', '你好', '133', '男', '0', 0);
INSERT INTO `user` VALUES (6, 0, 'abc', '$2a$10$8Eda64BeILUyOaUnyJWg.e5heTYBTYVQ1r/8ynjZzF7RV/lnClURK', '测试', '11', '女', '1', 1);
INSERT INTO `user` VALUES (7, 1, 'yf', '$2a$10$5xlb7Am1iyQYHGiCBvNbA.j.dUORDQawOeT/dKqVtN1DlNLLoFiZe', '阳帆', '12', '男', '1', 0);
INSERT INTO `user` VALUES (8, 1, 'pwb', '$2a$10$95ZyJ7Nj5PnJY9refqO3FeILMRkZLq.6R07ApRlx82bdo13MV6Bv2', '李四1', '13', '男', '0', 0);
INSERT INTO `user` VALUES (9, 1, 'a', '$2a$10$XkOAbqPr1bcydDOdhYRKA.3rdbTx5A7QfrSuytqF3ZWR.D6suG1zS', '爱你哦', '22', '女', '1', 0);
INSERT INTO `user` VALUES (10, 0, 'aa', '$2a$10$k4C4.KMRyV8Or4qslclyWuCaoRhwNJbuaXEguLSopsCb1qCWOVZie', '两个aa', '32', '女', '0', 0);
INSERT INTO `user` VALUES (12, 0, 'b', '$2a$10$iKDFA521iU/fNLjCOacPLuaX77eqtn47FSfxFGZXyObH0BIHmadvO', '一个b', '2523', '男', '0', 0);
INSERT INTO `user` VALUES (13, 0, 'bb', '$2a$10$POBAID.MKrRdfmYBO2ooduodjy8PQqw/eEGQq6p70PQ.OMKWjr7Du', '两个bb', '432', '男', '0', 0);
INSERT INTO `user` VALUES (14, 0, 'bbb', '$2a$10$pVhgCFOQBoplYfcwKnwNG.VGEiBkJHVji.nmr9dORmB/1Gcg.UgKO', '三个bbb', '123', '男', '2', 0);
INSERT INTO `user` VALUES (15, 0, '张三', '$2a$10$RGzpMnIjDJEPhzPVoKsLm.IlCTL3oPBwbxej.AvmYsaDZSQNpAt0u', '张三', '423', '男', '2', 0);
INSERT INTO `user` VALUES (16, 0, '王二麻子', '$2a$10$moIal0Co8sF8.Np3e/AKGez2ChAqbI1YCtOtRdfoT9zl1pVm9SODO', '王二', '235', '女', '2', 0);
INSERT INTO `user` VALUES (17, 0, '隔壁老王', '$2a$10$QzI0oUi38ly6i5.7Q3sVd.ygGER8NmUT4uVi/QgZ2752gYYP6E41C', '老王', '234', '女', '1', 0);
INSERT INTO `user` VALUES (18, 0, '包龙星', '$2a$10$BSqVqr286tURFrOoUJ5ZLuHzoJ30k6xPWDnaR4of12E58bvzar4IK', '包青天', '2212', '男', '2', 0);
INSERT INTO `user` VALUES (19, 0, '苏乞儿', '$2a$10$W2O5n0Ia3vdsRHA9xwPk3.o1uwVry00OHim3g/9pV/BjBi.EXpPam', '武状元', '11', '男', '0', 0);

SET FOREIGN_KEY_CHECKS = 1;
