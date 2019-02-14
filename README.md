# spring-demo
spring集成quartz（RAM形式）

在tag v1.0的基础上，pom.xml添加三个jar
```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context-support</artifactId>
    <version>${springframework.version}</version>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-tx</artifactId>
    <version>${springframework.version}</version>
</dependency>

<dependency>
    <groupId>org.quartz-scheduler</groupId>
    <artifactId>quartz</artifactId>
    <version>2.3.0</version>
</dependency>
```
为了能在job中使用`@Resource`注解，我们自定义了job工厂`CustomeJobFactory.java`文件。

参考：
https://blog.csdn.net/u012907049/article/details/73801122/

https://blog.csdn.net/upxiaofeng/article/details/79415108

[解决quartz的job无法注入spring对象](https://blog.csdn.net/mengruobaobao/article/details/79106343)