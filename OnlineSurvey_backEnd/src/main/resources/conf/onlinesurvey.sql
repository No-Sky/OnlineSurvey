# 创建数据库
create database if not exists `onlinesurvey` default charset utf8 COLLATE utf8_general_ci;

# 使用数据库
use onlinesurvey;

# 用户表
drop table if exists `User`;
create table `User` (
    `userId` int not null auto_increment primary key,
    `email` varchar(30) not null,
    `password` varchar(20),
    `username` varchar(20),
    `statusType` int(1) default 0,
    `score` int default 0
);
insert into `User` (`email`, `password`, `username`) values ('xfw_nosky@163.com', 'test123456', 'xfw');
insert into `User` (`email`, `password`, `username`) values ('xfw.nosky@outlook.com', 'test123456', 'test001');

drop table if exists `User_Score`;
create table `User_Score` (
	`usId` int not null auto_increment primary key,
    `userId` int,
    `record` int,
    `desc` varchar(30)
);

# 问卷
drop table if exists `Questionnaire`;
create table`Questionnaire` (
    `questionnaireId` int not null auto_increment primary key,
    `userId` int,
    `title` varchar(50),
    `description` varchar(200),
    `statusType` int(1) default 0,
    `createTime` datetime,
    `stopTime` datetime,
    `distribution` int default 0,
    `deleteFlag` int(1) default 0
);

# 题目
##questionType 1: 单选，2：多选，3：填空
drop table if exists `Question`;
create table `Question` (
    `questionId` int not null auto_increment primary key,
    `questionnaireId` int,
    `questionTitle` varchar(100),
    `questionType` int,
    `required` int(1),
    `row` int(1)
);

# 选项
drop table if exists `Option`;
create table `Option` (
    `optionId` int not null auto_increment primary key,
    `questionId` int,
    `content` varchar(50)
);

# 提交信息
drop table if exists `SubmitInfo`;
create table `SubmitInfo` (
    `submitId` int not null auto_increment primary key,
    `questionnaireId` int,
    `userId` int,
    `submitIP` varchar(50),
    `submitTime` datetime,
    `useTime` int
);

# 回答
##questionType 1: 单选，2：多选，3：填空
drop table if exists `Answer`;
create table `Answer` (
    `answerId` int not null auto_increment primary key,
    `questionnaireId` int,
    `questionId` int,
    `submitId` int,
    `questionType` int,
    `answer` int,
    `answerText` varchar(100)
);

# 标签
drop table if exists `Tag`;
create table `Tag` (
   `tagId` int not null auto_increment primary key,
   `tag` varchar(15) not null,
);

drop table if exists `User_Tag`;
create table `User_Tag` (
    `utId` int not null primary key,
    `userId` int,
    `tagId` int,
    `value` int
);

drop table if exists `Questionnaire_Tag`;
create table `Questionnaire_Tag` (
    `qtId` int not null primary key,
    `questionnaireId` int,
    `tagId` int
);

select A.optionId,count(B.submitId) as submitCount,A.content 
	from (select * from `Option` where questionId = 1) A 
    left join Answer B on A.optionId=B.answer 
    group by A.optionId

        