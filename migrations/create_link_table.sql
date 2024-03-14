--liquibase formatted sql

--changeset CREATE:1
create table if not exists link
(
    url          text primary key,
    star_count   integer,
    answer_count integer,
    updated_date timestamp without time zone not null,
    checked_date timestamp without time zone not null
);
