CREATE TABLE guest_user
(
    id                 VARCHAR(255) NOT NULL COMMENT 'ID',
    created_date       DATETIME DEFAULT NULL COMMENT '생성일시',
    last_modified_date DATETIME DEFAULT NULL COMMENT '최종수정일시',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='게스트 회원';

CREATE TABLE user
(
    id                 BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'ID',
    username           VARCHAR(255) NOT NULL COMMENT 'USERNAME',
    password           TEXT         NOT NULL COMMENT 'PASSWORD',
    name               VARCHAR(255) NOT NULL COMMENT '이름',
    created_date       DATETIME DEFAULT NULL COMMENT '생성일시',
    last_modified_date DATETIME DEFAULT NULL COMMENT '최종수정일시',
    PRIMARY KEY (id),
    UNIQUE KEY USER_INDEX_1 (username)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='회원';

CREATE TABLE authority
(
    id      BIGINT      NOT NULL AUTO_INCREMENT COMMENT 'ID',
    name    VARCHAR(45) NOT NULL COMMENT '권한 이름',
    user_id BIGINT      NOT NULL COMMENT '사용자 ID',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='권한';
