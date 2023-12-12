-- -------------------------
-- Table structure for order
-- -------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`
(
    `id`       INT(8) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`  INT(8) UNSIGNED NOT NULL COMMENT '用户id',
    `amount`   VARCHAR(16)       DEFAULT '' COMMENT '金额',
    `rem`      VARCHAR(256)      DEFAULT '' COMMENT '备注',
    `del`      CHAR(1)           DEFAULT '0' COMMENT '删除标识，0-正常，1-已删除',
    `add_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `upd_time` DATETIME          DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单信息表';
