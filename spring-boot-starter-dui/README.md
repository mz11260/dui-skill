## spring-boot-starter-skill使用说明
#### 简述
* spring-boot-starter-dui是spring boot版的，基于思必驰DUI开放平台的Skill开发工具包
* [思必驰DUI官网](https://www.dui.ai/)
* [思必驰 DSK 协议](https://www.dui.ai/docs/ct_dsk_protocol)


#### 如何使用
* 安装SDK并引入项目
```bash
cd spring-boot-starter-dui
mvn clean install -DskipTests -pl spring-boot-starter-dui
```
```xml
<dependency>
    <groupId>com.zm</groupId>
    <artifactId>spring-boot-starter-dui</artifactId>
    <version>1.0.0</version>
</dependency>
```
###### 注解使用
* 在dispatcher包下定义自己的技能处理类
* 使用 @SkillRequestHandler 注解标注一个类为技能请求处理程序
* 使用 @SkillSubscribe 注解标注技能请求处理程序方法为请求处理订阅者方法。


###### 请求调度异常处理接口
* 当请求发生异常时触发，有默认实现
```
com.zm.kit.dispatcher.interfaces.SubscriberExceptionHandler
```

###### 签名认证服务接口
* 保证 Https 链接访问的合法性，接口默认实现返回true
```
com.zm.kit.dispatcher.service.SecurityService
```


```java

```