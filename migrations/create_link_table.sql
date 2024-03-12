--liquibase formatted sql

--changeset CREATE:1
create table if not exists link
(
    link_id           bigserial primary key,
    link         text      not null,
    checked_date timestamp not null
);
