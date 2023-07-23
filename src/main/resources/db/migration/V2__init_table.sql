CREATE TABLE IF NOT EXISTS sprinter.members
(
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    username            VARCHAR(255)    NOT NULL COMMENT '유저 계정',
    password            VARCHAR(255)    NOT NULL COMMENT '패스워드',
    token               VARCHAR(255)    NOT NULL COMMENT '리프레쉬 토큰',
    profile_name        VARCHAR(255)    NOT NULL COMMENT '유저 닉네임',
    date_created        DATETIME        NOT NULL COMMENT '생성일자',
    date_updated        DATETIME        NOT NULL COMMENT '수정일자',
    date_deleted        DATETIME        NULL COMMENT '삭제일자',
    PRIMARY KEY (`id`),
    UNIQUE  username (`username`)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COMMENT='회원 테이블';

CREATE TABLE IF NOT EXISTS sprinter.roles (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    role_type   VARCHAR(255)    NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE  role_type (`role_type`)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COMMENT='권한 목록 테이블';

INSERT INTO sprinter.roles(ROLE_TYPE)
SELECT * FROM (SELECT 'ROLE_USER' AS roleType) AS tmp
WHERE NOT EXISTS (
    SELECT * FROM sprinter.roles WHERE roleType = 'ROLE_USER'
) LIMIT 1;

INSERT INTO sprinter.roles(ROLE_TYPE)
SELECT * FROM (SELECT 'ROLE_ADMIN' AS roleType) AS tmp
WHERE NOT EXISTS (
    SELECT * FROM sprinter.roles WHERE roleType = 'ROLE_ADMIN'
) LIMIT 1;

CREATE TABLE IF NOT EXISTS `sprinter`.`member_roles`
(
    member_id           BIGINT              NOT NULL COMMENT '회원 아이디',
    role_type           VARCHAR(255)        NOT NULL COMMENT '권한',
    PRIMARY KEY (`member_id`)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COMMENT='회원 별 권한 테이블';

CREATE TABLE IF NOT EXISTS `sprinter`.`posts`
(
    id                  BIGINT              NOT NULL AUTO_INCREMENT,
    title               VARCHAR(255)        NOT NULL COMMENT '제목',
    start_date          DATETIME            NULL COMMENT '시작 날짜',
    end_date            DATETIME            NULL COMMENT '종료 날짜',
    owner_member_id     BIGINT              NOT NULL COMMENT '포스트 주인 아이디',
    date_created        DATETIME            NOT NULL COMMENT '생성일자',
    date_updated        DATETIME            NOT NULL COMMENT '수정일자',
    date_deleted        DATETIME            NULL COMMENT '삭제일자',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COMMENT='게시글 테이블';

CREATE TABLE IF NOT EXISTS `sprinter`.`member_posts`
(
    id                  BIGINT              NOT NULL AUTO_INCREMENT,
    member_id           BIGINT              NOT NULL COMMENT '회원 아이디',
    post_id             BIGINT              NOT NULL COMMENT '포스트 아이디',
    date_created        DATETIME            NOT NULL COMMENT '생성일자',
    date_updated        DATETIME            NOT NULL COMMENT '수정일자',
    date_deleted        DATETIME            NULL COMMENT '삭제일자',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COMMENT='회원 - 게시글 연관 테이블';

create table `sprinter`.`revinfos`
(
    rev                 BIGINT              NOT NULL AUTO_INCREMENT,
    revtstmp            BIGINT              NULL,
    primary key (REV)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COMMENT='rev 테이블';

CREATE table `sprinter`.`aws_s3_objects`
(
    id                  BIGINT              NOT NULL AUTO_INCREMENT,
    url                 VARCHAR(255)        NOT NULL UNIQUE COMMENT 's3 image url',
    date_created        DATETIME            NOT NULL COMMENT '생성일자',
    date_updated        DATETIME            NOT NULL COMMENT '수정일자',
    date_deleted        DATETIME            NULL COMMENT '삭제일자',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COMMENT='AWS S3 이미지 테이블';

CREATE TABLE IF NOT EXISTS profiles
(
    id                      BIGINT          NOT NULL AUTO_INCREMENT,
    member_id               BIGINT          NOT NULL,
    job_level               VARCHAR(255)    NOT NULL,
    job_group               VARCHAR(255)    NOT NULL,
    job                     VARCHAR(255)    NOT NULL,
    affiliation_type        VARCHAR(255)    NOT NULL,
    affiliation             VARCHAR(255)    NOT NULL,
    job_skills              VARCHAR(2047)   NOT NULL,
    introduction            VARCHAR(2047)   NOT NULL,
    portfolio_link          VARCHAR(255)    NULL,
    portfolio_file_url      VARCHAR(255)    NULL,
    profile_image_url       VARCHAR(255)    NULL,
    date_created            DATETIME        NOT NULL COMMENT '생성일자',
    date_updated            DATETIME        NOT NULL COMMENT '수정일자',
    date_deleted            DATETIME        NULL COMMENT '삭제일자',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COMMENT='회원 프로필';

CREATE TABLE IF NOT EXISTS work_experiences
(
    id                      BIGINT          NOT NULL AUTO_INCREMENT,
    profile_id              BIGINT          NOT NULL,
    job_group               VARCHAR(255)    NOT NULL,
    job                     VARCHAR(255)    NOT NULL,
    company                 VARCHAR(255)    NOT NULL,
    start_date              DATETIME        NOT NULL,
    end_date                DATETIME        NOT NULL,
    date_created            DATETIME        NOT NULL COMMENT '생성일자',
    date_updated            DATETIME        NOT NULL COMMENT '수정일자',
    date_deleted            DATETIME        NULL COMMENT '삭제일자',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
    COMMENT='경력';

CREATE TABLE IF NOT EXISTS project_experiences
(
    id                      BIGINT          NOT NULL AUTO_INCREMENT,
    profile_id              BIGINT          NOT NULL,
    job_group               VARCHAR(255)    NOT NULL,
    job                     VARCHAR(255)    NOT NULL,
    project_name            VARCHAR(255)    NOT NULL,
    start_date              DATETIME        NOT NULL,
    end_date                DATETIME        NOT NULL,
    date_created            DATETIME        NOT NULL COMMENT '생성일자',
    date_updated            DATETIME        NOT NULL COMMENT '수정일자',
    date_deleted            DATETIME        NULL COMMENT '삭제일자',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COMMENT='프로젝트 경험';

CREATE TABLE IF NOT EXISTS other_experiences
(
    id                      BIGINT          NOT NULL AUTO_INCREMENT,
    profile_id              BIGINT          NOT NULL,
    activity_name           VARCHAR(255)    NOT NULL,
    role                    VARCHAR(255)    NOT NULL,
    start_date              DATETIME        NOT NULL,
    end_date                DATETIME        NOT NULL,
    date_created            DATETIME        NOT NULL COMMENT '생성일자',
    date_updated            DATETIME        NOT NULL COMMENT '수정일자',
    date_deleted            DATETIME        NULL COMMENT '삭제일자',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COMMENT='기타 경험';
