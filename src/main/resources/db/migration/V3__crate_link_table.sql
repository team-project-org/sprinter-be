CREATE TABLE IF NOT EXISTS sprinter.links
(
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    url                 CHAR(255)       NOT NULL COMMENT 'MOCK 프로필 이미지 URL',
    profile_id          BIGINT          NOT NULL COMMENT '프로필 FK',
    date_created        DATETIME        NOT NULL COMMENT '생성일자',
    date_updated        DATETIME        NOT NULL COMMENT '수정일자',
    date_deleted        DATETIME        NULL COMMENT '삭제일자',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COMMENT='MOCK 회원 별 링크 목록 테이블';