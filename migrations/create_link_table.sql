--liquibase formatted sql

--changeset CREATE:1
create table if not exists link
(
    id           bigserial primary key,
    link         text      not null,
    type_id      integer   not null,
    checked_date timestamp not null
);
