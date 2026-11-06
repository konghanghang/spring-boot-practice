# Cloud 示例（Java 17）

依赖组合为 Spring Boot 4.0.9、Spring Cloud 2025.1.4、Spring Cloud Alibaba 2025.1.0.0 和本地安装的 `base-iminling-core 3.0.0-SNAPSHOT`。父工程的 Maven Wrapper 负责构建。

## 配置迁移

- 原 `bootstrap.yml` 中的应用名和 Nacos 配置已移到 `application.yml`，不再使用 bootstrap starter。
- provider 和 consumer 保持原来的 Nacos 配置中心默认禁用状态。需要配置中心时，显式启用 `nacos-config` profile，例如 `--spring.profiles.active=dev,nacos-config`。此 profile 使用 `spring.config.import`，默认导入 `provider.yml` 或 `consumer.yml`；可用 `NACOS_CONFIG_DATA_ID`、`NACOS_GROUP` 指定实际配置。`optional:` 允许配置不存在，但并不等于禁用 Nacos 连接。
- `NACOS_SERVER_ADDR` 默认 `localhost:8848`，`NACOS_NAMESPACE` 默认为 consumer 的 `dev` 或其他模块的公共命名空间。请使用服务端真实的 namespace ID。`NACOS_DISCOVERY_ENABLED` 可覆盖发现开关；provider 原来默认关闭，consumer/gateway 默认开启。联调时需将 provider 注册与 consumer 配置到同一命名空间。
- Gateway 使用 `spring-cloud-starter-gateway-server-webflux` 和 `spring.cloud.gateway.server.webflux.*`。原 `/dept/list` 路由仍指向 `lb://provider-dept`；此服务名与当前 provider 示例的 `provider` 不同，需要目标服务真实存在。原全局过滤器仍要求 `username` 查询参数。
- Feign 的超时和日志配置移到 `spring.cloud.openfeign.client.config`。consumer 通过 `defaultConfiguration` 在每个 Feign 子容器中注册 Decoder，使用 Cloud 5 的消息转换器及 core 的 JsonMapper，保留返回包装解码、LoadBalancer 和 OkHttp。不能把 Decoder 注册到父容器后再查找只存在于 Feign 子容器的转换器。
- provider 实现带 `@FeignClient` 的共享接口，但自身没有 Feign 客户端，因而设置 `app.customize.feign=false`。这避免 core 的客户端 MVC 排除规则把 provider 的 Controller 一并排除。
- consumer 同样关闭 core 的默认 Feign 自动配置，使用上面的显式客户端配置；默认 Spring MVC 能区分 Feign 接口与真正的 Controller 实现类，从而保留本模块产品接口。
- Sleuth 已替换为 Micrometer Tracing + Brave，并保留 B3 多请求头传播格式。日志 MDC 使用 `traceId`、`spanId`。未配置远程 tracing exporter。
- 原 Eureka 和 Knife4j 配置没有对应运行依赖，随 bootstrap 迁移移除；本例当前使用 Nacos。

## 本地验证

从项目根目录运行：

```sh
./mvnw -pl cloud/provider,cloud/consumer,cloud/gateway-9527 -am verify
```

测试禁用 Nacos 注册和配置读取。provider 测试通过随机本地端口验证产品写入/读取及统一返回；consumer 通过本地 HTTP stub 验证真实 Feign 客户端及 core 的 MVC/JSON 接入，并验证新版 SpringDecoder 对包装、字符串和泛型对象的处理；gateway 测试验证路由属性绑定和全局过滤器。它们不证明真实 Nacos 注册、配置刷新、跨服务发现或远端调用已联调通过。
