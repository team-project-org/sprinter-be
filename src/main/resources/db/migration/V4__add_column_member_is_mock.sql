alter table sprinter.members
    add column `is_mock` bigint default false not null comment 'Mock 회원 여부';
