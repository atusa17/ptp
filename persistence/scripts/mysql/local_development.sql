drop database if exists pandamonium;
create database pandamonium;
use pandamonium;
create table accounts (
id int not null auto_increment primary key unique,
username varchar(50) not null unique,
password varchar(256) not null,
administrator_status boolean default false,
last_login date,
version int default 1
);
insert into accounts (username, password, administrator_status)
values ('admin', 'secret', true),
('atusa', 'secret', true),
('dantanxiaotian', 'secret', true),
('BrittanyBi', 'secret', true),
('lanlanzeliu', 'secret', true),
('tramanh305', 'secret', true);

create table definitions (
id int not null auto_increment primary key unique,
name varchar(200) not null,
definition json not null,
notation json,
version int default 1
);