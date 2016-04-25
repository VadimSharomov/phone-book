CREATE DATABASE IF NOT EXISTS phone_book;

CREATE USER 'user'@'localhost' IDENTIFIED BY 'password';

CREATE TABLE IF NOT EXISTS users (id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT, fullname varchar(45), login varchar(45), password varchar(45), idsession BIGINT, INDEX(login));

CREATE TABLE IF NOT EXISTS contacts (id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT, iduser BIGINT NOT NULL, lastname varchar(45), name varchar(45), middlename varchar(45), mobilephone varchar(45), homephone varchar(45), address varchar(45), email varchar(45), INDEX(lastName));