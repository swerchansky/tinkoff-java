--liquibase formatted sql

--changeset CREATE:1
create table if not exists link
(
    url          text primary key,
    checked_date timestamp not null
);
