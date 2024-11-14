--liquibase formatted sql

--changeset CREATE:1
create table if not exists link_chat
(
    url     text,
    chat_id bigint,
    primary key (url, chat_id),
    foreign key (url) references link (url),
    foreign key (chat_id) references chat (chat_id)
)
