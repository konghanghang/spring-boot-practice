# JPA 学习示例

当前基线为 Java 17、Spring Boot 4、Hibernate 7。运行应用需要 MySQL 8+；`application.yml` 中的数据库地址和账户请按本机环境配置。驱动使用 `com.mysql.cj.jdbc.Driver`。

`MySQL5DialectUtf8mb4` 保留旧类名以兼容配置引用，实际继承 Hibernate 7 的 `MySQLDialect`，继续为建表语句设置 InnoDB、utf8mb4 和 `utf8mb4_unicode_ci`。它不再提供 MySQL 5.7 兼容性。

`jpa-simple` 保留 Student 的字段映射和派生查询、HQL、原生 SQL 示例。旧的 `org.hibernate.cfg.PropertyContainer` 同包覆盖代码已移除；其属性顺序定制改用 Hibernate 的 `ColumnOrderingStrategy` 扩展，生成 Student 表时仍按 `id / number / name / sex / email / remark / create_time` 排列列。扩展仅作用于示例 Student 表，不调整已有数据库的列顺序。

`jpa-dsl` 使用 QueryDSL 5.1 的 Jakarta 制品，由 Maven Compiler Plugin 运行注解处理器，替代旧 `apt-maven-plugin`。测试中通过生成的 Q 类型执行实际 H2 查询。

在仓库根目录运行：

```sh
./mvnw -pl jpa/jpa-simple,jpa/jpa-dsl -am test
```

测试自动启用 `test` profile，使用各模块独立的 H2 内存数据库和事务回滚，不连接应用配置中的 MySQL。覆盖实体字段和中文内容回读、自动 ID、条件查询、排序、列顺序以及 QueryDSL 注解处理与查询执行。H2 的 MySQL 模式不等于 MySQL 服务器联调。
