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
INSERT INTO `sys_store` VALUES ('1', '总配中心', 'zongpei', '123456', '1', '2');
INSERT INTO `sys_store` VALUES ('4', '南塘店', 'nantang', '654321', '2', '1');
INSERT INTO `sys_store` VALUES ('5', '时代店', 'shidai', '654321', '2', '1');
INSERT INTO `sys_store` VALUES ('8', '测试门店', 'ceshimendian', '123', '2', '1');
INSERT INTO `sys_store` VALUES ('9', '修改测试1', 'xiugai', 'xiugaiceshi', '1', '2');

DROP TABLE IF EXISTS `sys_product`;
CREATE TABLE `sys_product`(
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`code` VARCHAR(100) NOT NULL,
	`name` varchar(100) not null,
	`unit` VARCHAR(100) not null,
	`status` tinyint(4) DEFAULT 0,
	`type` tinyint(4) DEFAULT 0,
PRIMARY KEY(id)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sys_warehouse`;
CREATE TABLE `sys_warehouse`(
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`balance` DOUBLE NOT null DEFAULT 0,
	`storeId` int(11) DEFAULT 0,
	`productId` int(11) DEFAULT 0,
	`status` TINYINT(4) DEFAULT 0,
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
`creatTime` TIMESTAMP not null,
`confirmFlag` TINYINT(4) not null DEFAULT 0,
`remark` VARCHAR(255) not null DEFAULT '',
`sendStoreId` int(11) not null DEFAULT 0,
`sendStoreName` varchar(50) not null DEFAULT '',
PRIMARY KEY(oddId)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sys_warehouse_record_detail`;
CREATE TABLE `sys_warehouse_record_detail`(
`uuid` varchar(32) not null,
`oddId` VARCHAR(32) not null,
`productId` int(11) not null DEFAULT 0,
`num` double not null DEFAULT 0,
`productName` varchar(20) not null DEFAULT 0,
PRIMARY KEY(uuid)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;