# 后端模块

## 技术图谱

语言：JAVA1.8

使用框架：SpringBoot<version: 2.4.2>  MybatisPlus<version: >

数据库：MySQL<version: 5.7>

应用服务器：Tomcat<version: 9.0.41>

## 基础功能E-R图

![基础功能ER图](./Readme.assets/基础功能ER图.png)

### 用户

![](./Readme.assets/用户ER图.png)

### 问卷

![问卷ER图](./Readme.assets/问卷ER图.png)

### 题目

![题目ER图](./Readme.assets/题目ER图.png)

### 选项

![选项ER图](./Readme.assets/选项ER图.png)

### 提交信息

![提交信息ER图](./Readme.assets/提交信息ER图.png)

### 回答

![回答ER图](./Readme.assets/回答ER图.png)

## 基础功能用例图

![基础功能用例图](./Readme.assets/基础功能用例图.png)

### 基础功能接口设计



### 邮箱接口类

```java
/**
* @description 邮箱接口类
*/
public interfacve EmailServiceInterface {
    
    /**
    * @description 发送邮件验证码
    */
    Result sendVerifyCode(String email, String verifyCode);
    
    /**
    * @description 发送问卷填写邮件通知
    */
    Result sendQuestionnaire(String email, Integer questionnaireid);
}
```

## Future

### 内容分析模块

### 偏好问卷推荐模块