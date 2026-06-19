/*
 Travel demo database initialization script.
 This script is intended for course/demo deployment and will recreate tables.
*/

CREATE DATABASE IF NOT EXISTS `db_travel`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE `db_travel`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `praise`;
DROP TABLE IF EXISTS `feedback`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `tourism`;
DROP TABLE IF EXISTS `collect`;
DROP TABLE IF EXISTS `comment`;
DROP TABLE IF EXISTS `travels`;
DROP TABLE IF EXISTS `routes`;
DROP TABLE IF EXISTS `article`;
DROP TABLE IF EXISTS `notice`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `admin`;

CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '??ID',
  `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `role` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_admin_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='????';

INSERT INTO `admin` (`id`, `username`, `password`, `name`, `avatar`, `role`, `phone`, `email`) VALUES
  (1, 'admin', 'admin', '???', 'http://localhost:9090/files/download/1721114905635-??.jpeg', 'ADMIN', '18899990011', 'admin2@xm.com');

CREATE TABLE `notice` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '??ID',
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '????',
  `time` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  PRIMARY KEY (`id`),
  KEY `idx_notice_title` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='?????';

INSERT INTO `notice` (`id`, `title`, `content`, `time`) VALUES
  (1, '????????', '?????????????????????????????????????????', '2024-07-16 15:51:17'),
  (2, '???????????', '???????????????????????', '2024-07-16 15:52:22'),
  (3, '???????????????', '???????????????????????????????', '2024-07-16 15:52:56');

CREATE TABLE `article` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `descr` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `cover` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `content` longtext COLLATE utf8mb4_unicode_ci COMMENT '??',
  `read_count` int DEFAULT 0 COMMENT '???',
  `date` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  PRIMARY KEY (`id`),
  KEY `idx_article_title` (`title`),
  KEY `idx_article_read_count` (`read_count`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='??????';

CREATE TABLE `routes` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `days` int DEFAULT NULL COMMENT '??',
  `tips` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '???',
  `content` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `location` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `longitude` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `latitude` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  PRIMARY KEY (`id`),
  KEY `idx_routes_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='??????';

CREATE TABLE `travels` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `cover` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `descr` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `content` longtext COLLATE utf8mb4_unicode_ci COMMENT '??',
  `user_id` int DEFAULT NULL COMMENT '???ID',
  `time` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `read_count` int DEFAULT 0 COMMENT '???',
  `start_date` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `money` int DEFAULT NULL COMMENT '??',
  `days` int DEFAULT NULL COMMENT '????',
  `location` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  PRIMARY KEY (`id`),
  KEY `idx_travels_user_id` (`user_id`),
  KEY `idx_travels_status` (`status`),
  KEY `idx_travels_read_count` (`read_count`),
  KEY `idx_travels_title` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='????';

CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `role` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='????';

CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `content` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `user_id` int DEFAULT NULL COMMENT '???ID',
  `pid` int DEFAULT NULL COMMENT '??ID',
  `time` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `fid` int DEFAULT NULL COMMENT '??ID',
  `module` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `root_id` int DEFAULT NULL COMMENT '???ID',
  PRIMARY KEY (`id`),
  KEY `idx_comment_fid_module` (`fid`, `module`),
  KEY `idx_comment_root_id` (`root_id`),
  KEY `idx_comment_pid` (`pid`),
  KEY `idx_comment_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='???';

CREATE TABLE `collect` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `fid` int DEFAULT NULL COMMENT '??ID',
  `user_id` int DEFAULT NULL COMMENT '???ID',
  `time` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_collect_fid_user` (`fid`, `user_id`),
  KEY `idx_collect_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='????';

CREATE TABLE `tourism` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `descr` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '??',
  `price` decimal(10,2) DEFAULT NULL COMMENT '??',
  `store` int DEFAULT 0 COMMENT '??',
  `specials` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `date` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  PRIMARY KEY (`id`),
  KEY `idx_tourism_name` (`name`),
  KEY `idx_tourism_date` (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='????';

CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_no` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `tourism_img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `tourism_price` decimal(10,2) DEFAULT NULL COMMENT '????',
  `tourism_id` int DEFAULT NULL COMMENT '??ID',
  `num` int DEFAULT NULL COMMENT '????',
  `total` decimal(10,2) DEFAULT NULL COMMENT '????',
  `user_id` int DEFAULT NULL COMMENT '???ID',
  `time` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `pay_no` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `pay_time` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_orders_order_no` (`order_no`),
  KEY `idx_orders_user_id` (`user_id`),
  KEY `idx_orders_tourism_id` (`tourism_id`),
  KEY `idx_orders_status` (`status`),
  KEY `idx_orders_pay_no` (`pay_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='????';

CREATE TABLE `feedback` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `user_id` int DEFAULT NULL COMMENT '???ID',
  `time` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '????',
  `reply` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '?????',
  PRIMARY KEY (`id`),
  KEY `idx_feedback_user_id` (`user_id`),
  KEY `idx_feedback_title` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='????';

CREATE TABLE `praise` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `fid` int DEFAULT NULL COMMENT '??ID',
  `user_id` int DEFAULT NULL COMMENT '??ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_praise_fid_user` (`fid`, `user_id`),
  KEY `idx_praise_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='????';

SET FOREIGN_KEY_CHECKS = 1;
