--liquibase formatted sql

--changeset CREATE:1
create table if not exists link_chat
(
    link_id bigint,
    chat_id bigint,
    primary key (link_id, chat_id),
    foreign key (link_id) references link (id),
    foreign key (chat_id) references chat (chat_id)
)
