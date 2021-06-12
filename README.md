# spring-boot-demo

spring相关学习记录以及一些工具类



# 各模块介绍

## cloud

spring-cloud的学习模块,主要包括

1. 服务注册与发现
   - eureka
   - nacos
2. 服务负载与调用
   - ribbon
   - openFeign
3. 服务熔断与降级
   - hystrix
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

es6.6版本的api实践

## elasticsearch-7.6

里边分为es7.6版本的加密介绍以及7.6版本正常的api实践,关于加密的详细介绍请看这篇文章介绍[Elasticsearch7.6开启xpack以及java代码配置客户端](https://segmentfault.com/a/1190000022102940).

## JDK
JDK相关工具使用：JUC、reflect

## kafka

kafka肤浅学习

## netty-study

netty肤浅学习

## shiro

本模块主要是研究shiro,已经实现的有对rest风格的url的支持,实例还不是很完整,但是核心代码基本都已齐全.

## spark

spark肤浅学习

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