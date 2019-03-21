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
create table theorems (
id int not null auto_increment primary key unique,
name varchar(512) not null,
theorem varchar(1024) not null,
theorem_type enum ('THEOREM', 'PROPOSITION', 'LEMMA', 'COROLLARY') not null,
branch varchar(512) not null,
referenced_definitions json,
referenced_theorems json,
proven_status boolean default false,
version int default 1
);
CREATE TABLE proofs
(
  id        INT NOT NULL AUTO_INCREMENT,
  theorem_name      VARCHAR(512) NOT NULL,
  branch    VARCHAR(512) NOT NULL,
  theorem   INT NOT NULL,
  FOREIGN KEY fk_theorem (theorem) REFERENCES theorems (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  referenced_definitions JSON,
  referenced_theorems JSON,
  date_added DATE,
  last_updated DATE,
  version INT DEFAULT 1,
  PRIMARY KEY (id)
);