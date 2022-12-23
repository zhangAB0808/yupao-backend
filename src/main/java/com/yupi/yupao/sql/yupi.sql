/*
Navicat MySQL Data Transfer

Source Server         : MySQL08
Source Server Version : 80020
Source Host           : 127.0.0.1:3306
Source Database       : yupi

Target Server Type    : MYSQL
Target Server Version : 80020
File Encoding         : 65001

Date: 2022-12-23 12:22:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tagName` varchar(256) DEFAULT NULL COMMENT '标签名称',
  `userId` bigint DEFAULT NULL COMMENT '用户id',
  `parentId` bigint DEFAULT NULL COMMENT '父标签id',
  `isParent` tinyint DEFAULT '1' COMMENT '0不是，1是',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  `tags` varchar(1024) DEFAULT NULL COMMENT '标签列表',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniIdx_tagName` (`tagName`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='标签';

-- ----------------------------
-- Records of tag
-- ----------------------------
INSERT INTO `tag` VALUES ('1', '学习方向', '3', null, '1', '2022-09-30 20:30:13', '2022-11-01 20:50:43', '0', '');
INSERT INTO `tag` VALUES ('2', 'java', '3', '1', '0', '2022-11-01 20:51:05', '2022-11-01 20:51:05', '0', null);
INSERT INTO `tag` VALUES ('3', 'c++', '3', '1', '0', '2022-11-01 20:51:24', '2022-11-01 20:51:24', '0', null);
INSERT INTO `tag` VALUES ('4', '前端', '3', '1', '0', '2022-11-01 20:51:37', '2022-11-01 20:51:37', '0', null);
INSERT INTO `tag` VALUES ('5', '性别', '3', null, '1', '2022-11-01 20:51:56', '2022-11-01 20:51:56', '0', null);
INSERT INTO `tag` VALUES ('6', '男', '3', '5', '0', '2022-11-01 20:52:14', '2022-11-01 20:52:14', '0', null);
INSERT INTO `tag` VALUES ('7', '女', '3', '5', '0', '2022-11-01 20:52:28', '2022-11-01 20:52:28', '0', null);
INSERT INTO `tag` VALUES ('8', '年级', '3', null, '1', '2022-11-01 21:07:05', '2022-11-01 21:07:05', '0', null);
INSERT INTO `tag` VALUES ('9', '大一', '3', '8', '0', '2022-11-01 21:07:24', '2022-11-01 21:07:24', '0', null);
INSERT INTO `tag` VALUES ('10', '大二', '3', '8', '0', '2022-12-21 14:23:06', '2022-12-21 14:23:28', '0', null);
INSERT INTO `tag` VALUES ('11', '大三', '3', '8', '0', '2022-12-21 14:23:06', '2022-12-21 14:23:28', '0', null);
INSERT INTO `tag` VALUES ('12', '大四', '3', '8', '0', '2022-12-21 14:23:06', '2022-12-21 14:23:28', '0', null);
INSERT INTO `tag` VALUES ('14', 'php', '3', '1', '0', '2022-12-23 12:10:15', '2022-12-23 12:10:15', '0', null);
INSERT INTO `tag` VALUES ('15', '兴趣爱好', '3', null, '1', '2022-12-23 12:12:33', '2022-12-23 12:12:33', '0', null);
INSERT INTO `tag` VALUES ('16', '听歌', '3', '15', '0', '2022-12-23 12:12:33', '2022-12-23 12:12:33', '0', null);

-- ----------------------------
-- Table structure for team
-- ----------------------------
DROP TABLE IF EXISTS `team`;
CREATE TABLE `team` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(256) NOT NULL COMMENT '队伍名称',
  `description` varchar(1024) DEFAULT NULL COMMENT '描述',
  `maxNum` int NOT NULL DEFAULT '1' COMMENT '最大人数',
  `expireTime` datetime DEFAULT NULL COMMENT '过期时间',
  `userId` bigint DEFAULT NULL COMMENT '用户id',
  `status` int NOT NULL DEFAULT '0' COMMENT '队伍状态 0-公开，1-私有，2-加密',
  `password` varchar(512) DEFAULT NULL COMMENT '密码',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  `teamAvatarUrl` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='队伍';

-- ----------------------------
-- Records of team
-- ----------------------------
INSERT INTO `team` VALUES ('4', '鱼皮小队', '66666666666666666666666666', '8', '2023-06-25 00:00:00', '3', '0', '', '2022-10-20 11:02:33', '2022-10-20 11:02:33', '0', 'https://img2.baidu.com/it/u=1071507123,4013803160&fm=253&fmt=auto&app=138&f=JPEG?w=200&h=200');
INSERT INTO `team` VALUES ('5', 'ikun小队', '你干嘛，唱跳rap，篮球', '6', '2022-12-05 00:00:00', '3', '2', '0808', '2022-10-21 13:05:48', '2022-10-21 13:05:48', '0', '');
INSERT INTO `team` VALUES ('6', 'ikun', '小黑子。', '4', '2023-01-01 00:00:00', '3', '0', '', '2022-10-30 16:51:33', '2022-10-30 16:51:33', '0', 'https://img2.baidu.com/it/u=2015865969,3401990894&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=546');
INSERT INTO `team` VALUES ('11', 'zhangsan', '......', '3', '2022-12-24 00:00:00', '3', '0', '', '2022-12-21 15:22:34', '2022-12-21 15:22:34', '1', null);
INSERT INTO `team` VALUES ('12', 'zhangsan', '..', '3', '2022-12-26 00:00:00', '3', '0', '', '2022-12-21 15:24:10', '2022-12-21 15:24:10', '1', '');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `username` varchar(256) DEFAULT NULL COMMENT '用户昵称',
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userAccount` varchar(256) DEFAULT NULL COMMENT '账号',
  `avatarUrl` varchar(1024) DEFAULT NULL COMMENT '用户头像',
  `gender` tinyint DEFAULT NULL COMMENT '性别',
  `userPassword` varchar(512) NOT NULL COMMENT '密码',
  `phone` varchar(128) DEFAULT NULL COMMENT '电话',
  `email` varchar(512) DEFAULT NULL COMMENT '邮箱',
  `userStatus` int NOT NULL DEFAULT '0' COMMENT '状态 0 - 正常',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `userProfile` varchar(512) DEFAULT NULL COMMENT '个人简介',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  `userRole` int NOT NULL DEFAULT '0' COMMENT '用户角色 0 - 普通用户 1 - 管理员',
  `planetCode` varchar(512) DEFAULT NULL COMMENT '星球编号',
  `tags` varchar(256) DEFAULT NULL COMMENT '标签列表json',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68475 DEFAULT CHARSET=utf8 COMMENT='用户';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('klaus', '3', 'klaus', 'https://img2.baidu.com/it/u=2015865969,3401990894&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=546', '1', '4fdd01bbb4abc54ff9066d3394a59497', '18438909039', '3317306971@qq.com', '0', '2022-10-15 16:27:03', '2022-10-15 16:27:09', '唱跳rap,篮球样样精通', '0', '1', '7200', '[\"java\",\"大一\",\"男\",\"前端\"]');
INSERT INTO `user` VALUES ('zhangzheng', '4', 'zhangzheng', 'https://img2.baidu.com/it/u=1071507123,4013803160&fm=253&fmt=auto&app=138&f=JPEG?w=200&h=200', '1', '4fdd01bbb4abc54ff9066d3394a59497', null, null, '0', '2022-10-22 09:30:57', '2022-10-22 09:30:57', '6666666', '0', '0', '1200', '[\"男\",\"java\",\"前端\",\"大四\"]');
INSERT INTO `user` VALUES ('haha', '5', 'haha', 'https://img0.baidu.com/it/u=3905825772,3024012927&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', '1', '4fdd01bbb4abc54ff9066d3394a59497', null, null, '0', '2022-10-23 18:11:57', '2022-10-23 18:11:57', '唱跳rap,篮球样样精通唱跳rap,篮球样样精通唱跳rap,篮球样样精通唱跳rap,篮球样样精通唱跳rap,篮球样样精通唱跳rap,篮球样样精通唱跳rap,篮球样样精通唱跳rap,篮球样样精通', '0', '0', '1800', '[\"java\",\"大一\",\"女\"]');
INSERT INTO `user` VALUES ('xixi', '6', 'xixi', 'https://img0.baidu.com/it/u=2267130927,4233025274&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=800', '0', '4fdd01bbb4abc54ff9066d3394a59497', null, null, '0', '2022-10-23 18:46:16', '2022-10-23 18:46:16', '唱跳rap,篮球样样精通', '0', '0', '1635', '[\"c++\",\"大二\",\"女\"]');
INSERT INTO `user` VALUES ('qiqi', '7', 'qiqi', 'https://img1.baidu.com/it/u=592570905,1313515675&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', '0', '4fdd01bbb4abc54ff9066d3394a59497', '15896655669', '465631689@qq.com', '0', '2022-10-27 21:10:17', '2022-10-27 21:10:17', '唱跳rap,篮球样样精通', '0', '0', '5202', null);
INSERT INTO `user` VALUES ('你干嘛', '8', 'niganma', 'https://img0.baidu.com/it/u=3946238700,850577113&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', null, '4fdd01bbb4abc54ff9066d3394a59497', null, null, '0', '2022-10-30 17:24:07', '2022-10-30 17:24:07', '唱跳rap,篮球样样精通', '0', '0', null, null);

-- ----------------------------
-- Table structure for user_friend
-- ----------------------------
DROP TABLE IF EXISTS `user_friend`;
CREATE TABLE `user_friend` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `userId` bigint DEFAULT NULL,
  `friendId` int DEFAULT NULL,
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDelete` tinyint NOT NULL DEFAULT '0',
  `chatContent` varchar(2048) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='存储用户与好友之间的对应关系及聊天内容';

-- ----------------------------
-- Records of user_friend
-- ----------------------------
INSERT INTO `user_friend` VALUES ('9', '4', '6', '2022-12-19 13:59:13', '2022-12-21 14:54:41', '0', '[{\"id\":4,\"content\":\"在吗\"},{\"id\":6,\"content\":\"在的\"},{\"id\":4,\"content\":\"哈啊哈\"},{\"id\":4,\"content\":\"？\"},{\"id\":4,\"content\":\"你干嘛嗨嗨呦\"},{\"id\":6,\"content\":\"漏出鸡脚了吧\"},{\"id\":6,\"content\":\"小黑子\"}]');
INSERT INTO `user_friend` VALUES ('14', '3', '6', '2022-12-19 15:02:23', '2022-12-21 12:32:54', '0', '[{\"id\":6,\"content\":\"你好\"},{\"id\":3,\"content\":\"你是？\"}]');
INSERT INTO `user_friend` VALUES ('15', '4', '5', '2022-12-19 16:27:36', '2022-12-21 12:37:29', '0', '[{\"id\":5,\"content\":\"。。。。\"}]');

-- ----------------------------
-- Table structure for user_team
-- ----------------------------
DROP TABLE IF EXISTS `user_team`;
CREATE TABLE `user_team` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userId` bigint DEFAULT NULL COMMENT '用户id',
  `teamId` bigint DEFAULT NULL COMMENT '队伍id',
  `joinTime` datetime DEFAULT NULL COMMENT '加入时间',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8 COMMENT='用户队伍关系';

-- ----------------------------
-- Records of user_team
-- ----------------------------
INSERT INTO `user_team` VALUES ('1', '3', '1', '2022-10-16 18:57:45', '2022-10-16 18:57:45', '2022-10-16 18:57:45', '0');
INSERT INTO `user_team` VALUES ('2', '3', '2', '2022-10-16 20:26:31', '2022-10-16 20:26:30', '2022-10-16 20:26:30', '0');
INSERT INTO `user_team` VALUES ('3', '3', '3', '2022-10-16 20:36:26', '2022-10-16 20:36:25', '2022-10-16 20:36:25', '0');
INSERT INTO `user_team` VALUES ('4', '3', '4', '2022-10-20 11:02:34', '2022-10-20 11:02:33', '2022-10-20 11:02:33', '0');
INSERT INTO `user_team` VALUES ('5', '3', '5', '2022-10-21 13:05:49', '2022-10-21 13:05:48', '2022-10-21 13:05:48', '0');
INSERT INTO `user_team` VALUES ('6', '68469', '4', '2022-10-22 10:18:49', '2022-10-22 10:18:49', '2022-10-22 10:18:49', '0');
INSERT INTO `user_team` VALUES ('7', '68469', '5', '2022-10-22 11:06:35', '2022-10-22 11:06:35', '2022-10-22 11:06:35', '0');
INSERT INTO `user_team` VALUES ('8', '68472', '4', '2022-10-27 21:21:28', '2022-10-27 21:21:27', '2022-10-27 21:21:27', '1');
INSERT INTO `user_team` VALUES ('9', '68472', '4', '2022-10-27 21:26:49', '2022-10-27 21:26:48', '2022-10-27 21:26:48', '1');
INSERT INTO `user_team` VALUES ('10', '68472', '4', '2022-10-28 13:09:16', '2022-10-28 13:09:16', '2022-10-28 13:09:16', '1');
INSERT INTO `user_team` VALUES ('11', '68472', '4', '2022-10-28 13:23:33', '2022-10-28 13:23:32', '2022-10-28 13:23:32', '1');
INSERT INTO `user_team` VALUES ('12', '68472', '4', '2022-10-28 13:24:39', '2022-10-28 13:24:39', '2022-10-28 13:24:39', '1');
INSERT INTO `user_team` VALUES ('13', '68472', '4', '2022-10-28 13:24:47', '2022-10-28 13:24:46', '2022-10-28 13:24:46', '1');
INSERT INTO `user_team` VALUES ('14', '68472', '4', '2022-10-28 13:25:02', '2022-10-28 13:25:02', '2022-10-28 13:25:02', '1');
INSERT INTO `user_team` VALUES ('15', '68472', '4', '2022-10-28 13:26:51', '2022-10-28 13:26:51', '2022-10-28 13:26:51', '1');
INSERT INTO `user_team` VALUES ('16', '68472', '4', '2022-10-28 13:27:02', '2022-10-28 13:27:02', '2022-10-28 13:27:02', '1');
INSERT INTO `user_team` VALUES ('17', '68472', '4', '2022-10-28 13:35:17', '2022-10-28 13:35:17', '2022-10-28 13:35:17', '1');
INSERT INTO `user_team` VALUES ('18', '68472', '4', '2022-10-28 13:35:50', '2022-10-28 13:35:49', '2022-10-28 13:35:49', '1');
INSERT INTO `user_team` VALUES ('25', '68472', '4', '2022-10-28 13:44:55', '2022-10-28 13:44:54', '2022-10-28 13:44:54', '1');
INSERT INTO `user_team` VALUES ('26', '68472', '4', '2022-10-28 13:44:58', '2022-10-28 13:44:57', '2022-10-28 13:44:57', '1');
INSERT INTO `user_team` VALUES ('27', '68472', '4', '2022-10-28 13:45:45', '2022-10-28 13:45:44', '2022-10-28 13:45:44', '1');
INSERT INTO `user_team` VALUES ('28', '68472', '5', '2022-10-28 13:50:18', '2022-10-28 13:50:17', '2022-10-28 13:50:17', '1');
INSERT INTO `user_team` VALUES ('29', '68472', '4', '2022-10-28 13:51:26', '2022-10-28 13:51:25', '2022-10-28 13:51:25', '1');
INSERT INTO `user_team` VALUES ('30', '68472', '4', '2022-10-28 13:51:29', '2022-10-28 13:51:29', '2022-10-28 13:51:29', '1');
INSERT INTO `user_team` VALUES ('31', '68472', '5', '2022-10-28 13:51:35', '2022-10-28 13:51:35', '2022-10-28 13:51:35', '1');
INSERT INTO `user_team` VALUES ('32', '68472', '5', '2022-10-28 13:52:18', '2022-10-28 13:52:18', '2022-10-28 13:52:18', '1');
INSERT INTO `user_team` VALUES ('33', '68472', '4', '2022-10-28 13:53:44', '2022-10-28 13:53:44', '2022-10-28 13:53:44', '0');
INSERT INTO `user_team` VALUES ('34', '68470', '4', '2022-10-30 16:49:40', '2022-10-30 16:49:40', '2022-10-30 16:49:40', '0');
INSERT INTO `user_team` VALUES ('35', '3', '6', '2022-10-30 16:51:34', '2022-10-30 16:51:33', '2022-10-30 16:51:33', '0');
INSERT INTO `user_team` VALUES ('36', '68469', '6', '2022-10-30 16:51:54', '2022-10-30 16:51:53', '2022-10-30 16:51:53', '0');
INSERT INTO `user_team` VALUES ('37', '68471', '4', '2022-10-30 17:06:22', '2022-10-30 17:06:22', '2022-10-30 17:06:22', '0');
INSERT INTO `user_team` VALUES ('38', '68471', '6', '2022-10-30 17:06:24', '2022-10-30 17:06:23', '2022-10-30 17:06:23', '0');
INSERT INTO `user_team` VALUES ('39', '68472', '6', '2022-10-30 19:16:54', '2022-10-30 19:16:53', '2022-10-30 19:16:53', '0');
INSERT INTO `user_team` VALUES ('40', '4', '4', '2022-12-18 15:18:27', '2022-12-18 15:18:26', '2022-12-18 15:18:26', '1');
INSERT INTO `user_team` VALUES ('41', '4', '4', '2022-12-18 15:18:38', '2022-12-18 15:18:38', '2022-12-18 15:18:38', '1');
INSERT INTO `user_team` VALUES ('42', '4', '4', '2022-12-18 17:02:40', '2022-12-18 17:02:40', '2022-12-18 17:02:40', '1');
INSERT INTO `user_team` VALUES ('43', '4', '4', '2022-12-18 17:02:44', '2022-12-18 17:02:44', '2022-12-18 17:02:44', '1');
INSERT INTO `user_team` VALUES ('44', '4', '4', '2022-12-18 17:02:58', '2022-12-18 17:02:57', '2022-12-18 17:02:57', '1');
INSERT INTO `user_team` VALUES ('45', '4', '4', '2022-12-18 17:03:17', '2022-12-18 17:03:16', '2022-12-18 17:03:16', '1');
INSERT INTO `user_team` VALUES ('46', '4', '4', '2022-12-18 17:03:37', '2022-12-18 17:03:37', '2022-12-18 17:03:37', '1');
INSERT INTO `user_team` VALUES ('47', '4', '4', '2022-12-18 17:57:40', '2022-12-18 17:57:40', '2022-12-18 17:57:40', '1');
INSERT INTO `user_team` VALUES ('48', '4', '7', '2022-12-18 17:58:38', '2022-12-18 17:58:38', '2022-12-18 17:58:38', '1');
INSERT INTO `user_team` VALUES ('49', '4', '4', '2022-12-18 18:00:50', '2022-12-18 18:00:49', '2022-12-18 18:00:49', '1');
INSERT INTO `user_team` VALUES ('50', '4', '4', '2022-12-18 18:01:35', '2022-12-18 18:01:34', '2022-12-18 18:01:34', '1');
INSERT INTO `user_team` VALUES ('51', '4', '4', '2022-12-18 18:02:19', '2022-12-18 18:02:18', '2022-12-18 18:02:18', '1');
INSERT INTO `user_team` VALUES ('52', '3', '8', '2022-12-18 18:55:41', '2022-12-18 18:55:41', '2022-12-18 18:55:41', '1');
INSERT INTO `user_team` VALUES ('53', '4', '4', '2022-12-18 18:57:06', '2022-12-18 18:57:05', '2022-12-18 18:57:05', '1');
INSERT INTO `user_team` VALUES ('54', '4', '4', '2022-12-18 18:59:07', '2022-12-18 18:59:07', '2022-12-18 18:59:07', '1');
INSERT INTO `user_team` VALUES ('55', '4', '4', '2022-12-18 18:59:11', '2022-12-18 18:59:11', '2022-12-18 18:59:11', '1');
INSERT INTO `user_team` VALUES ('56', '4', '9', '2022-12-18 18:59:43', '2022-12-18 18:59:43', '2022-12-18 18:59:43', '1');
INSERT INTO `user_team` VALUES ('57', '4', '4', '2022-12-18 18:59:50', '2022-12-18 18:59:49', '2022-12-18 18:59:49', '1');
INSERT INTO `user_team` VALUES ('58', '4', '4', '2022-12-18 19:00:01', '2022-12-18 19:00:00', '2022-12-18 19:00:00', '1');
INSERT INTO `user_team` VALUES ('59', '4', '4', '2022-12-18 19:00:07', '2022-12-18 19:00:07', '2022-12-18 19:00:07', '1');
INSERT INTO `user_team` VALUES ('60', '4', '4', '2022-12-18 19:05:42', '2022-12-18 19:05:41', '2022-12-18 19:05:41', '1');
INSERT INTO `user_team` VALUES ('61', '4', '4', '2022-12-19 13:24:06', '2022-12-19 13:24:06', '2022-12-19 13:24:06', '1');
INSERT INTO `user_team` VALUES ('62', '4', '4', '2022-12-19 14:02:24', '2022-12-19 14:02:24', '2022-12-19 14:02:24', '1');
INSERT INTO `user_team` VALUES ('63', '5', '4', '2022-12-21 13:20:03', '2022-12-21 13:20:02', '2022-12-21 13:20:02', '1');
INSERT INTO `user_team` VALUES ('64', '5', '10', '2022-12-21 13:21:40', '2022-12-21 13:21:40', '2022-12-21 13:21:40', '1');
INSERT INTO `user_team` VALUES ('65', '3', '11', '2022-12-21 15:22:34', '2022-12-21 15:22:34', '2022-12-21 15:22:34', '1');
INSERT INTO `user_team` VALUES ('66', '3', '12', '2022-12-21 15:24:10', '2022-12-21 15:24:10', '2022-12-21 15:24:10', '1');
