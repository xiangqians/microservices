-- ------------------------
-- Table structure for user
-- ------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`       INT(8) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `nickname` VARCHAR(60)       DEFAULT '' COMMENT '昵称',
    `name`     VARCHAR(60)       DEFAULT '' COMMENT '用户名',
    `phone`    VARCHAR(60)       DEFAULT '' COMMENT '手机号',
    `email`    VARCHAR(60)       DEFAULT '' COMMENT '邮箱',
    `passwd`   VARCHAR(128)      DEFAULT '' COMMENT '密码',
    `rem`      VARCHAR(255)      DEFAULT '' COMMENT '备注',
    `lock`     CHAR(1)           DEFAULT '0' COMMENT '锁定标记，0-正常，1-锁定',
    `del`      CHAR(1)           DEFAULT '0' COMMENT '删除标识，0-正常，1-已删除',
    `add_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `upd_time` DATETIME          DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX      idx_user_name(`name`),
    INDEX      idx_user_phone(`phone`),
    INDEX      idx_user_email(`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

