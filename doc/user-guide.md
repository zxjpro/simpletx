# 使用指南

客户端需要加入依赖

```xml
        <dependency>
            <groupId>com.xiaojiezhu.simpletx</groupId>
            <artifactId>simpletx-client</artifactId>
        </dependency>
        
        <!-- 这是切面的依赖 -->
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
        </dependency>
```

同时，在启动类里面，需要加上启动注解``@EnableSimpletxTransaction``

同时，还必须指定``appName``,

在``application.yml``中

```yaml
spring:
  application:
    name: sample-http-balance
```

或者在``application.properties``中

```properties
spring.application.name=sample-http-balance
```


每一个工程，都至少包含一个事务管理器，如果是spring boot工程，则会由spring boot自动创建一个事务管理器，
如果应用程序没有连接数据库，而只是管理全局http请求，那么可以注入一个空的事务管理器

```xml
<bean class="com.xiaojiezhu.simpletx.core.transaction.EmptyTransactionManager"></bean>
```

```java

```