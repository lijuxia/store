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
  `type` tinyint(4) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
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
