# spring-boot-demo

spring相关学习记录以及一些工具类

## 构建与本地验证

当前基线为 **JDK 17、Spring Boot 4.0.9、Spring Framework 7、Jackson 3**。
Maven Wrapper 固定 Maven 3.9.11；先设置 `JAVA_HOME` 指向 JDK 17，IDE 的 Project SDK、Maven Runner 也应使用 JDK 17。
项目以 `--release 17` 编译，并保留方法参数名供公共库的 JSON 参数解析使用。

本项目依赖同级目录 `base-iminling-core` 的 `3.0.0-SNAPSHOT`，该版本尚未发布到公共 Maven 仓库。首次构建或修改公共库后，先安装其当前源码：

```sh
cd ../base-iminling-core
./mvnw clean install
cd ../spring-boot-practice
./mvnw clean verify
```

Windows 使用 `mvnw.cmd`。首次执行需要联网下载 Maven 和依赖。
普通 `verify` 编译全部模块及测试、执行本地回归并打包；不再全局跳过测试。
Web/MVC、Cloud provider、Shiro、工具模块的启动测试使用本机随机端口，JPA/QueryDSL 使用隔离 H2，Redis使用 mock；这些结果不代表外部服务联调通过。

依赖真实服务的示例使用 `integration` 标签，依赖本机文件、手工操作或长时间运行的示例使用 `manual` 标签，默认均不运行。
需要运行时，先为选定模块准备隔离环境并覆盖 `application.yml` 中的旧连接配置，然后显式选择测试：

```sh
./mvnw -pl zookeeper-api -am -Pintegration -Dtest=ApiTest -Dsurefire.failIfNoSpecifiedTests=false test
# manual 示例同样应选定具体类或方法，并先调整文件路径等环境参数
./mvnw -pl tools -am -DexcludedTestGroups=integration -Dtest=V4Test -Dsurefire.failIfNoSpecifiedTests=false test
```

请勿直接面向共享服务运行所有 integration 示例：其中包含写入数据、发送消息、操作快照和持续消费的学习代码。

### 本轮依赖迁移

| 组件 | 当前版本或调整 |
| --- | --- |
| 公共库 | `iminling-common` / `iminling-core` `3.0.0-SNAPSHOT` |
| Spring Cloud / Alibaba | `2025.1.4` / `2025.1.0.0`；Sleuth 改为 Micrometer Tracing，Nacos 配置使用 Config Data |
| Web | Jakarta API、Boot 4 模块化 starter、Tomcat；移除旧 Undertow starter |
| JPA | Hibernate 7、MySQL 8+ 驱动/方言、QueryDSL 5.1 Jakarta；见 [JPA 说明](jpa/README.md) |
| Shiro / ZooKeeper / RocketMQ Spring | `3.0.1` / `3.9.7` / `2.3.6` |
| Elasticsearch | 两个 ES 示例目录均统一为 `7.17.29`；保留 TCP TransportClient 示例，Netty 局部使用 `4.1.140.Final` |
| 工具依赖 | Druid `1.2.28`、Guava `33.8.0-jre`、Commons IO `2.21.0`、OkHttp `4.12.0`、维护版 JSch `2.28.8`；Groovy/Mockito/JUnit 等由 Boot 管理 |
| CGLIB 示例 | 使用 Spring 维护的 CGLIB，在 JDK 17 下无需额外 `--add-opens` |

Elasticsearch 示例现要求 ES 7 服务端，不再兼容 ES 6 集群；TransportClient 和 Jest `6.3.1` 仍是旧 API 学习示例，并非现代 ES 服务端接入模板，升级 ES 8/9 需要另行迁移客户端 API。
Jest 配置示例原有 `@Configuration` 仍被注释，真实集成测试前需自行补齐客户端 Bean。Shiro 的 JWT 用户名提取和验签也仍是未完成的学习示例；当前回归覆盖 Realm 登录与权限、匿名路由和缺 token 拒绝，不代表完整 JWT 认证可用。
RocketMQ Spring 内部仍使用 Jackson 2 转换消息，与应用的 Jackson 3 并存；隔离测试检查了原有消息字段的往返兼容。
公共库 3.x 已移除 Swagger/Knife4j 自动集成，仓库原有注释示例与旧 `knife4j.info` 配置不会生成文档端点。
外部 MySQL、Elasticsearch、Nacos、ZooKeeper、Kafka、RocketMQ、Redis 的服务端兼容性需在各自环境另行验证。



# 各模块介绍

## cloud

spring-cloud的学习模块,主要包括

1. 服务注册与发现
   - eureka
   - nacos
2. 服务负载与调用
   - Spring Cloud LoadBalancer
   - openFeign
3. 服务熔断与降级
   - 旧 Hystrix 降级示例仅保留注释，当前未接入断路器
   - ~~sentinel(暂无研究)~~
4. 配置中心
   - nacos
5. 服务网关
   - gateway

## data

data为数据结构和算法模块,现在正在学习中.

### algorithm为算法

- [x] 排序算法

- [x] 查找算法

### structure为数据结构

- [x] 队列
- [x] 链表
- [x] 稀疏数组
- [x] 栈
- [x] 自定义hash表
- [x] 普通树和二叉树

## design-pattern
设计模式学习
### created-mode
创建型设计模式
### structural-mode
结构型设计模式

## elasticsearch

原 ES 6.x API 实践已迁移至 ES 7.17.29，保留 Transport/REST 客户端和索引、映射、查询示例；连接目标需使用 ES 7 集群。

## elasticsearch-7.6

目录沿用旧名称，当前使用 ES 7.17.29 客户端，包含加密连接与普通 API 实践,关于加密的详细介绍请看这篇文章介绍[Elasticsearch7.6开启xpack以及java代码配置客户端](https://segmentfault.com/a/1190000022102940).

## JDK
JDK相关工具使用：JUC、reflect

## kafka

kafka肤浅学习

## netty-study

netty肤浅学习

## shiro

本模块主要是研究shiro,已经实现的有对rest风格的url的支持,实例还不是很完整,但是核心代码基本都已齐全.

## spring

主要是用来研究spring的ioc和aop功能

## tools

工具模块,主要有两个模块

1. ip读取

   ip读取分为ipv4和ipv6,ipv4使用的是纯真的库

   ipv6是基于zxinc网站提供的数据来做的,详细的使用说明见文章[基于zxinc网站ipv6静态数据文件的JAVA查询实现](https://segmentfault.com/a/1190000022961245).

2. jdbc连接

   jdbc连接包括常见的数据库的连接使用

## web-mvc

研究spring-mvc功能.包括

- [x] 自定义参数处理器
- [x] 自定义返回结果处理器
- [x] 自定义全局异常处理器

更多详细spring-mvc的功能我已经封装成了一个公共的jar,提取出来,也方便自己使用,已开源,地址:[base-iminling-core](https://github.com/konghanghang/base-iminling-core),具体使用见仓库说明.

## zookeeper-api

zk的api实践.

## zookeeper-election-master

利用zk进行master的选举实践.


## Stargazers over time

[![Stargazers over time](https://starchart.cc/konghanghang/spring-boot-demo.svg)](https://starchart.cc/konghanghang/spring-boot-demo)
