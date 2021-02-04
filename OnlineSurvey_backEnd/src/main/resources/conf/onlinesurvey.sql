# 创建数据库
create database if not exists 'onlinesurvey' set utf8 COLLATE utf8_general_ci;

# 使用数据库
use onlinesurvey;

# 用户表
drop table if exists 'User';
create table 'User' (
    'userId' int not null primary key,
    'email' varchar(30) not null,
    'password' varchar(20),
    'username' varchar(20),
    'statusType' int(1) default 0
);

# 问卷
drop table if exists 'Questionnaire';
create table'Questionnaire' (
    'questionnaireId' int not null primary key,
    'userId'int,
);




