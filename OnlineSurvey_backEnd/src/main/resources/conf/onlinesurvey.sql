# 创建数据库
create database if not exists 'onlinesurvey' set utf8 COLLATE utf8_general_ci;

# 使用数据库
use onlinesurvey;

# 用户表
drop table if exists 'User';
create table 'User' (
    'userId' int not null auto_increment primary key,
    'email' varchar(30) not null,
    'password' varchar(20),
    'username' varchar(20),
    'statusType' int(1) default 0
);

# 问卷
drop table if exists 'Questionnaire';
create table'Questionnaire' (
    'questionnaireId' int not null auto_increment primary key,
    'userId'int,
    'title' varchar(50),
    'description' varchar(200),
    'statusType' int(1) default 0,
    'createTime' timestamp,
    'stopTime' timestamp,
    'distribution' int default 0,
    'deleteFlag' int(1) default 0
);

# 题目
##questionType 1: 单选，2：多选，3：填空
drop table if exists 'Question';
create table 'Question' (
    'questionId' int not null auto_increment primary key,
    'questionnaireId' int,
    'questionTitle' varchar(100),
    'questionType' int,
    'required' int(1),
    'row' int(1)
);

# 选项
drop table if exists 'Option';
create table 'Option' (
    'optionId' int not null auto_increment primary key,
    'questionId' int,
    'content' varchar(50)
);

# 提交信息
drop table if exists 'SubmitInfo';
create table 'SubmitInfo' (
    'submitId' int not null auto_increment primary key,
    'questionnaireId' int,
    'userId' int,
    'submitIP' varchar(50),
    'submitTime' timestamp,
    'useTime' int
);

# 回答
##questionType 1: 单选，2：多选，3：填空
drop table if exists 'Answer';
create table 'Answer' (
    'answerId' int not null auto_increment primary key,
    'questionnaireId' int,
    'questionId' int,
    'submitId' int,
    'questionType' int,
    'answer' int,
    'answerText' varchar(100)
);




