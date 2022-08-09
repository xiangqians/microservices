/*
https://github.com/spring-attic/spring-security-oauth/blob/main/spring-security-oauth2/src/test/resources/schema.sql
*/

-- ----------------------------------------
-- Table structure for oauth_client_details
-- ---------------------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`
(
    `client_id`                VARCHAR(64) NOT NULL COMMENT '客户端id',
    `resource_ids`             JSON                 DEFAULT NULL COMMENT '资源ids',
    `secret_required`          CHAR(1)              DEFAULT '1' COMMENT '密钥是否必须，0-不必须；1-必须',
    `client_secret`            VARCHAR(64)          DEFAULT '' COMMENT '客户端密码，client_secret字段不能直接是 secret 的原始值，需要经过加密',
    `scope`                    JSON                 DEFAULT NULL COMMENT '该客户端允许授权的范围，定义客户端的权限，这里只是一个标识，资源服务可以根据这个权限进行鉴权',
    `authorized_grant_types`   JSON                 DEFAULT NULL COMMENT '该客户端允许授权的类型',
    `registered_redirect_uris` JSON                 DEFAULT NULL COMMENT '跳转的uri',
    `authorities`              JSON                 DEFAULT NULL COMMENT 'authorities',
    `access_token_validity`    INT(8) DEFAULT 7200 COMMENT '访问令牌有效期，单位：s',
    `refresh_token_validity`   INT(8) DEFAULT 259200 COMMENT '刷新令牌有效期，单位：s',
    `auto_approve`             CHAR(1)              DEFAULT '1' COMMENT '0-跳转到授权页面；1-不跳转，直接发令牌',
    `additional_information`   JSON                 DEFAULT NULL COMMENT '附加信息',
    `del_flag`                 CHAR(1)              DEFAULT '0' COMMENT '删除标识，0-正常，1-删除',
    `create_time`              DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`              DATETIME             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户端详情';
-- client_secret
-- 原始密码：123456
-- 加密后：$2a$10$yzWoLiGj/gfiDDnkcUnNkeoXUGE5mHOe034p4xbIkHxQxS7D3iJKa
/*
INSERT INTO `oauth_client_details` (`client_id`, `resource_ids`, `secret_required`, `client_secret`, `scope`,
`authorized_grant_types`,
`registered_redirect_uris`, `authorities`, `access_token_validity`, `refresh_token_validity`, `auto_approve`, `additional_information`)
VALUES ('client_1', '[\"order-biz\", \"user-biz\"]', '1', '$2a$10$yzWoLiGj/gfiDDnkcUnNkeoXUGE5mHOe034p4xbIkHxQxS7D3iJKa', '[\"read\"]',
'[\"password\", \"implicit\", \"authorization_code\", \"refresh_token\", \"client_credentials\"]',
'[\"https://www.google.com/\"]', '[]', 7200, 259200, '1', NULL);
*/


-- --------------------------------------
-- Table structure for oauth_access_token
-- --------------------------------------
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token`
(
    `token_id`          VARCHAR(256) NOT NULL,
    `token`             BLOB                  DEFAULT NULL,
    `authentication_id` VARCHAR(256)          DEFAULT NULL,
    `user_name`         VARCHAR(256)          DEFAULT NULL,
    `client_id`         VARCHAR(256)          DEFAULT NULL,
    `authentication`    BLOB                  DEFAULT NULL,
    `refresh_token`     VARCHAR(256)          DEFAULT NULL,
    `create_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       DATETIME              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`token_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='授权令牌数据表';


-- ---------------------------------------
-- Table structure for oauth_refresh_token
-- ---------------------------------------
DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token`
(
    `token_id`       VARCHAR(256) NOT NULL,
    `token`          BLOB                  DEFAULT NULL,
    `authentication` BLOB                  DEFAULT NULL,
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`token_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='授权刷新令牌数据表';


-- ------------------------------
-- Table structure for oauth_code
-- ------------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code`
(
    `code`           VARCHAR(256) NOT NULL COMMENT '存储服务端系统生成的code的值（未加密）',
    `authentication` BLOB                  DEFAULT NULL COMMENT '存储将AuthorizationRequestHolder.java对象序列化后的二进制数据',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='授权码数据存储表';

