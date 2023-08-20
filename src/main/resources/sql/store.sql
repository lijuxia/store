/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : store

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-05-16 16:45:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_store
-- ----------------------------
DROP TABLE IF EXISTS `sys_store`;
CREATE TABLE `sys_store` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(20) NOT NULL,
  `type` tinyint(4) DEFAULT 0,
  `status` tinyint(4) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_store
-- ----------------------------
INSERT INTO `sys_store` VALUES ('1', '总配中心', 'zongpei', '123456', '1', '1');
INSERT INTO `sys_store` VALUES ('4', '南塘店', 'nantang', '654321', '2', '1');
INSERT INTO `sys_store` VALUES ('5', '时代店', 'shidai', '654321', '2', '1');

DROP TABLE IF EXISTS `sys_product`;
CREATE TABLE `sys_product`(
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`code` VARCHAR(100) NOT NULL,
	`name` varchar(100) not null,
	`unit` VARCHAR(100) not null,
	`status` tinyint(4) DEFAULT 0,
	`type` tinyint(4) DEFAULT 0,
    `inventory` tinyint(1) DEFAULT 0,
PRIMARY KEY(id)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;


INSERT INTO `sys_product` VALUES ('2', 'roumo', '肉末', '斤', '1', '1');
INSERT INTO `sys_product` VALUES ('3', 'shourou', '瘦肉', '斤', '1', '1');
INSERT INTO `sys_product` VALUES ('4', 'feirou', '肥肉', '斤', '1', '1');
INSERT INTO `sys_product` VALUES ('5', 'qingcai', '青菜', '斤', '1', '1');
INSERT INTO `sys_product` VALUES ('6', 'jidan', '本地鸡蛋', '个', '1', '2');
INSERT INTO `sys_product` VALUES ('7', 'kele', '可乐', '罐', '1', '2');
INSERT INTO `sys_product` VALUES ('8', 'zicai', '紫菜', '斤', '1', '1');
INSERT INTO `sys_product` VALUES ('9', 'huntun', '生馄饨', '个', '1', '1');

DROP TABLE IF EXISTS `sys_product_detail`;
CREATE TABLE `sys_product_detail`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productId` int(11) NOT NULL DEFAULT 0,
  `detailId` int(11) NOT NULL DEFAULT 0,
  `num` DECIMAL(10,2) NOT NULL DEFAULT 0,
  `status` TINYINT(4) DEFAULT 0,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sys_warehouse`;
CREATE TABLE `sys_warehouse`(
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`balance` DOUBLE NOT null DEFAULT 0,
	`storeId` int(11) DEFAULT 0,
	`productId` int(11) DEFAULT 0,
	`status` TINYINT(4) DEFAULT 0,
  `time` TIMESTAMP(3) not null,
PRIMARY KEY(id)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sys_warehouse_record`;
CREATE TABLE `sys_warehouse_record`(
`oddId` VARCHAR(32) not null,
`storeId` int(11) not null DEFAULT 0,
`storeName` varchar(50) not null DEFAULT '',
`type` TINYINT(4) not null DEFAULT 0,
`status` TINYINT(4) not null DEFAULT 0,
`inOrOut` TINYINT(4) not null DEFAULT 0,
`creatTime` TIMESTAMP(3) not null,
`confirmFlag` TINYINT(4) not null DEFAULT 0,
`remark` VARCHAR(255) not null DEFAULT '',
`sendStoreId` int(11) not null DEFAULT 0,
`sendStoreName` varchar(50) not null DEFAULT '',
`date` DATE ,
`makeProductId` int(11) not null DEFAULT 0,
`makeNum` DECIMAL(10,2) not null DEFAULT 0,
PRIMARY KEY(oddId)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sys_warehouse_record_detail`;
CREATE TABLE `sys_warehouse_record_detail`(
`uuid` varchar(32) not null,
`oddId` VARCHAR(32) not null,
`productId` int(11) not null DEFAULT 0,
`num` DECIMAL(10,2) not null DEFAULT 0,
`productName` varchar(20) not null DEFAULT 0,
`unit` VARCHAR(100) not null,
`beforeSaveNum` DECIMAL(10,2) not null DEFAULT 0,
PRIMARY KEY(uuid)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;