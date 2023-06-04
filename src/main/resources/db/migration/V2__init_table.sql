CREATE TABLE IF NOT EXISTS sprinter.member
(
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    username            VARCHAR(255)    NOT NULL COMMENT '유저 계정',
    password            VARCHAR(255)    NOT NULL COMMENT '패스워드',
    token               VARCHAR(255)    NOT NULL COMMENT '리프레쉬 토큰',
    profile_name        VARCHAR(255)    NOT NULL COMMENT '유저 닉네임',
    date_created        DATETIME        NOT NULL COMMENT '생성일자',
    date_updated        DATETIME        NOT NULL COMMENT '수정일자',
    auditor             VARCHAR(100)    NULL COMMENT '수정자',
    PRIMARY KEY (`id`),
    UNIQUE  username (`username`)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COMMENT='회원 테이블';

CREATE TABLE IF NOT EXISTS sprinter.role (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    role_type   VARCHAR(255)    NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE  role_type (`role_type`)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COMMENT='권한 테이블';

INSERT INTO sprinter.role(ROLE_TYPE)
SELECT * FROM (SELECT 'ROLE_USER' AS roleType) AS tmp
WHERE NOT EXISTS (
    SELECT * FROM sprinter.role WHERE roleType = 'ROLE_USER'
) LIMIT 1;

INSERT INTO sprinter.role(ROLE_TYPE)
SELECT * FROM (SELECT 'ROLE_ADMIN' AS roleType) AS tmp
WHERE NOT EXISTS (
    SELECT * FROM sprinter.role WHERE roleType = 'ROLE_ADMIN'
) LIMIT 1;

CREATE TABLE IF NOT EXISTS `sprinter`.`member_role_list`
(
    member_id           BIGINT              NOT NULL COMMENT '회원 아이디',
    role_type           VARCHAR(255)        NOT NULL COMMENT '권한',
    PRIMARY KEY (`member_id`)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COMMENT='회원 권한 테이블';

CREATE TABLE IF NOT EXISTS `sprinter`.`post`
(
    id                  BIGINT              NOT NULL AUTO_INCREMENT,
    title               VARCHAR(255)        NOT NULL COMMENT '제목',
    start_date          DATETIME            NOT NULL COMMENT '시작 날짜',
    end_date            DATETIME            NOT NULL COMMENT '종료 날짜',
    owner_member_id     BIGINT              NOT NULL COMMENT '포스트 주인 아이디',
    date_created        DATETIME            NOT NULL COMMENT '생성일자',
    date_updated        DATETIME            NOT NULL COMMENT '수정일자',
    auditor             VARCHAR(100)        NULL COMMENT '수정자',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COMMENT='게시글 테이블';

CREATE TABLE IF NOT EXISTS `sprinter`.`member_post`
(
    id                  BIGINT              NOT NULL AUTO_INCREMENT,
    member_id           BIGINT              NOT NULL COMMENT '회원 아이디',
    post_id             BIGINT              NOT NULL COMMENT '포스트 아이디',
    date_created        DATETIME            NOT NULL COMMENT '생성일자',
    date_updated        DATETIME            NOT NULL COMMENT '수정일자',
    auditor             VARCHAR(100)        NULL COMMENT '수정자',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COMMENT='회원 - 게시글 연관 테이블';

create table `sprinter`.`revinfo`
(
    rev                 BIGINT              NOT NULL AUTO_INCREMENT,
    revtstmp            BIGINT              NULL,
    primary key (REV)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COMMENT='rev 테이블';