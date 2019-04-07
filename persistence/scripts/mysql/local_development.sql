drop database if exists pandamonium;
create database pandamonium;
use pandamonium;
create table accounts (
id int not null auto_increment primary key unique,
username varchar(50) not null unique,
password varchar(256) not null,
administrator boolean default false,
last_login date,
version int default 1
);
insert into accounts (username, password, administrator)
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
create table theorems (
id int not null auto_increment primary key unique,
name varchar(512) not null,
theorem varchar(1024) not null,
theorem_type varchar(20) not null,
branch varchar(512) not null,
referenced_definitions json,
referenced_theorems json,
proven_status boolean default false,
version int default 1
);
create table proofs
(
  id int not null auto_increment primary key unique,
  theorem_name varchar(512) not null,
  proof varchar(4096) not null,
  branch varchar(512) not null,
  theorem int not null,
  referenced_definitions json,
  referenced_theorems json,
  date_added date,
  last_updated date,
  version int default 1
);