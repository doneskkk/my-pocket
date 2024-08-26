--liquibase formatted sql

--changeset DonesChiril:create-moneytracker-schema
--comment create new schema
create schema moneytracker;
--rollback drop schema moneytracker;