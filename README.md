### dui-skill
#### 项目说明
* dui-skill 基于思必驰DUI开放平台的Skill demo示例程序
* [思必驰DUI官网](https://www.dui.ai/)
* [思必驰 DSK 协议](https://www.dui.ai/docs/ct_dsk_protocol)

|模块名称 | 依赖关系 | 模块说明|
|----|----|----        |
|dui-skill|none|统一的pom管理|  
|zm-utils|none|工具包|  
|spring-boot-start-dui|none|skill SDK spring-boot版|  
|demo-skill|spring-boot-start-dui、zm-utils|示例技能|  

#### 如何使用
##### 准备工作
* Java版本要求：1.7+
* Maven版本要求：3.X
* MySQL版本要求：5.6+
* Redis版本要求：3.2+
##### 下载安装
* git clone到本地
* 安装utils
```bash
cd dui-skill
mvn clean install -DskipTests -pl zm-utils
```
* spring-boot版本SDK安装
```bash
cd spring-boot-starter-dui
mvn clean install -DskipTests -pl spring-boot-starter-dui
```


#### End