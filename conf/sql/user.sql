/*
# which
$ which mysql
/usr/bin/mysql

# login
$ mysql -u root -proot

# create user
CREATE USER microservices IDENTIFIED BY 'microservices';

# create DATABASE
# user
# drop database ?
DROP DATABASE IF EXISTS user;
# create database
CREATE DATABASE IF NOT EXISTS user DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_unicode_ci;
GRANT ALL ON user.* TO microservices;
# flush
FLUSH privileges;

*/


-- ------------------------
-- Table structure for user
-- ------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`              INT(8) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `nickname`        VARCHAR(64)          DEFAULT '' COMMENT '昵称',
    `username`        VARCHAR(64) NOT NULL UNIQUE COMMENT '用户名',
    `password`        VARCHAR(128)         DEFAULT '' COMMENT '密码',
    `desc`            VARCHAR(128)         DEFAULT '' COMMENT '描述',
    `additional_info` JSON                 DEFAULT NULL COMMENT '附加信息',
    `lock_flag`       CHAR(1)              DEFAULT '0' COMMENT '锁定标记，0-正常，1-锁定',
    `del_flag`        CHAR(1)              DEFAULT '0' COMMENT '删除标识，0-正常，1-删除',
    `create_time`     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY               `idx_user_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
-- INSERT
INSERT INTO `user`(`nickname`, `username`, `password`)
VALUES ('系统管理员', 'admin', '$2a$10$IVzj1Wd.ZQdOIWdb1htQjexU94uoNeuk1crlQ9ExVupPi0Iy1uv.C');
-- password: 123456


-- ------------------------
-- Table structure for role
-- ------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`              INT(8) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色id',
    `name`            VARCHAR(64)          DEFAULT '' COMMENT '角色名称',
    `code`            VARCHAR(32) NOT NULL UNIQUE COMMENT '角色码',
    `desc`            VARCHAR(128)         DEFAULT '' COMMENT '角色描述',
    `additional_info` JSON                 DEFAULT NULL COMMENT '附加信息',
    `del_flag`        CHAR(1)              DEFAULT '0' COMMENT '删除标识，0-正常，1-删除',
    `create_time`     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';
-- INSERT
INSERT INTO `role`(`name`, `code`)
VALUES ('系统管理员', 'SYS_ADMIN');


-- -----------------------------
-- Table structure for user_role
-- -----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`
(
    `user_id`     INT(8) UNSIGNED NOT NULL COMMENT '用户id',
    `role_id`     INT(8) UNSIGNED NOT NULL COMMENT '角色id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色表';
-- INSERT
INSERT INTO `user_role`(`user_id`, role_id)
VALUES (1, 1);


-- ------------------------------
-- Table structure for permission
-- ------------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`
(
    `id`              INT(8) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '权限id',
    `parent_id`       INT(8) UNSIGNED DEFAULT 0 COMMENT '权限父id',
    `name`            VARCHAR(64)       DEFAULT '' COMMENT '权限名称',
    `method`          VARCHAR(8)        DEFAULT '' COMMENT '权限允许权限方法，GET、POST、PUT、DELETE',
    `path`            VARCHAR(128)      DEFAULT '' COMMENT '权限路径',
    `desc`            VARCHAR(128)      DEFAULT '' COMMENT '权限描述',
    `additional_info` JSON              DEFAULT NULL COMMENT '附加信息',
    `del_flag`        CHAR(1)           DEFAULT '0' COMMENT '删除标识，0-正常，1-删除',
    `create_time`     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME          DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';


-- -----------------------------------
-- Table structure for role_permission
-- -----------------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`
(
    `role_id`       INT(8) UNSIGNED NOT NULL COMMENT '角色id',
    `permission_id` INT(8) UNSIGNED NOT NULL COMMENT '权限id',
    `create_time`   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`role_id`, `permission_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限表';
