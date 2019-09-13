# MMALL商城项目说明

## 结构说明
```java
com.mmall
---------common 常量 枚举 日志 全局异常等
---------controller 前端交互的控制器
---------dao持久层
---------service 服务层
---------pojo对应持久层表结构
---------vo对pojo的分装
---------util常用工具类
```
## 接口文档说明
### 前台用户相关接口
>* 用户登录接口  /user/login.do   HTTP     POST
入参名称 | 入参说明
-|-
userName|登录用户名|
password|密码|

>* success
```xml
{
"status": 0,
"data":{
"id": 22,
"username": "test1",
"password": "",
"email": "mr_hao_97@163.com",
"phone": "17621937312",
"question": "12345",
"answer": "1234",
"role": 0,
"createTime": 1568369669000,
"updateTime": 1568373248000
}
}
```
>* error
```xml
{
"status": 300,
"msg": "NEED_LOGIN"
}
```
>* 用户登出接口  user/loginOut.do HTTP     POST

入参名称 | 入参说明
-|-
无入参|要求在登录状态下即sessioin中有用户信息
>success
```xml
{
"status": 0,
"data":{
"id": 22,
"username": "test1",
"password": "",
"email": "mr_hao_97@163.com",
"phone": "17621937312",
"question": "12345",
"answer": "1234",
"role": 0,
"createTime": 1568369669000,
"updateTime": 1568373248000
}
}
```
>* error
```xml
{
"status": 100,
"msg":'error'
}
```
>* 用户注册接口  user/registor.do HTTP     POST

入参名称 | 入参说明
-|-
username|用户账号
password|密码|
email|用户邮箱|
phone|手机号|
question|密码丢失验证问题|
answer|密码地市验证答案|
role|角色 0-普通用户 1-管理员|
>success
```xml
{
"status": 0,
"msg": "注册成功"
}
```
>* error
```xml
{
"status": 100,
"msg": "用户名已存在"
}
```
>* 用户校验接口  user/checkValid.do HTTP     POST

入参名称 | 入参说明
-|-
str|用户账号/邮箱
type|username-用户账号 email-用户邮箱|
>success
```xml
{
"status": 0,
"data": "校验成功"
}
```
>* error
```xml
{
"status": 100,
"msg": "用户名已存在"
}
```
>* 获取用户信息  user/getUser.do HTTP     POST

入参名称 | 入参说明
-|-
无入参|要求在登录状态下即sessioin中有用户信息
>success
```xml
{
"status": 0,
"data":{
"id": 22,
"username": "test1",
"password": "",
"email": "mr_hao_97@163.com",
"phone": "17621937312",
"question": "12345",
"answer": "1234",
"role": 0,
"createTime": 1568369669000,
"updateTime": 1568373408000
}
}
```
>* error
```xml
{
"status": 100,
"msg": "没有对应的用户信息"
}
```

>* 根据用户名查找用户问题接口  user/forgetGetQuestion.do HTTP     POST

入参名称 | 入参说明
-|-
username|用户账号
>success
```xml
{
"status": 0,
"data": "问题"
}
```
>* error
```xml
{
"status": 100,
"msg": "用户名不存在"
}
```

>* 验证答案接口  user/forgetCheckAnswer.do HTTP     POST

入参名称 | 入参说明
-|-
username|用户账号
question|问题
answer|答案
>* 返回data 说明 次接口调用成功会返回token 作为重置密码的重要入参
>* success
```xml
{
"status": 0,
"data": "b9f08a28-18d9-4a7f-b27f-06b7ab1ea918"
}
```
>* error
```xml
{
"status": 100,
"msg": "验证答案错误"
}
```
>*离线 重置密码接口  user/forgetRestPassword.do HTTP     POST

入参名称 | 入参说明
-|-
username|用户账号
password|密码
token|token
>* 返回data 说明 次接口调用成功会返回token 作为重置密码的重要入参
>* success
```xml
{
"status": 0,
"msg": "密码重置成功"
}
```
>* error
```xml
{
"status": 0,
"msg": "用户未登录"
}
```

>* 在线重置密码接口  user/resetPassword.do HTTP     POST

入参名称 | 入参说明
-|-
passwordOld|旧密码
passwordNew|新密码

>* success
```xml
{
"status": 0,
"msg": "密码重置成功"
}
```
>* error
```xml
{
"status": 0,
"msg": "旧密码错误"
}
```

>* 用户信息更新  user/updateUserInfo.do HTTP     POST

入参名称 | 入参说明
-|-
email|用户邮箱|
phone|手机号|
question|密码丢失验证问题|
answer|密码地市验证答案|

>* success
```xml
{
"status": 100,
"msg": "更新成功",
"data":{
"id": 24,
"username": null,
"password": null,
"email": null,
"phone": null,
"question": "12345",
"answer": "1234",
"role": null,
"createTime": null,
"updateTime": null
}
}
```
>* error
```xml
{
"status": 0,
"msg": "用户未登录"
}
```


## 其他说明
>* org.mybatis.generator
```xml
mybatis三剑客之一（generator/plugin/pageHelper），作用 根据表生成pojo和dao还有对应的xml文件
```
>* spring 相关git
```xml
springmvc showcase 配置相关
spring-petclinic 官方demo
spring greenhouse 官方应用
spring boot 官方demo
```
## mmall学习笔记
>* JSON 控制器
### Mybatis
>* @Param 注解 当有这个注解时 value值 就是 对应xml的入参值
### 工具类
>* 字符串工具类 org.apache.commons.lang3;
## 问题
>* spring 依赖注入 三层的注解 分别对应什么？ 为什么？
>* com.google.common.cache 不会相关技术
>* logback日志
>* mybatis需要重新巩固
```xml
  <select id="checkEmailNotByUserId" resultType="int" parameterType="map">
    select count(1) from mmall_user
    where email = #{email}
    and id != #{userId}
  </select>
  对应dao文件中 @Param在程序中为什么必写
```
