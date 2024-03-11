--liquibase formatted sql

--changeset CREATE:1
create table if not exists chat
(
    chat_id bigint primary key
)
