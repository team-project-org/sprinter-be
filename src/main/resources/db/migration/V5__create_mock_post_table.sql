CREATE TABLE IF NOT EXISTS sprinter.mock_posts
(
    id                  BIGINT              NOT NULL AUTO_INCREMENT,
    email               VARCHAR(255)        NOT NULL COMMENT '이메일',
    project_name         VARCHAR(255)        NOT NULL COMMENT '프로젝트명',
    link                VARCHAR(255)        NOT NULL COMMENT '프로젝트 설명 링크',
    image               VARCHAR(255)        NOT NULL COMMENT '노출될 이미지',
    open_chat_link      VARCHAR(255)        NOT NULL COMMENT '오픈챗 링크',
    date_created        DATETIME            NOT NULL COMMENT '생성일자',
    date_updated        DATETIME            NOT NULL COMMENT '수정일자',
    date_deleted        DATETIME            NULL COMMENT '삭제일자',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COMMENT='게시글 테이블';