
# 一. 新手介绍

## 什么是Forest？

Forest是一个开源的Java HTTP客户端框架，它能够将HTTP的所有请求信息（包括URL、Header以及Body等信息）绑定到您自定义的Interface方法上，能够通过调用本地接口方法的方式发送HTTP请求。

## 为什么使用Forest?

使用Forest就像使用类似Dubbo那样的RPC框架一样，只需要定义接口，调用接口即可，不必关心具体发送HTTP请求的细节。同时将HTTP请求信息与业务代码解耦，方便您统一管理大量HTTP的URL、Header等信息。而请求的调用方完全不必在意HTTP的具体内容，即使该HTTP请求信息发生变更，大多数情况也不需要修改调用发送请求的代码。

## Forest的架构

![avater](media/architect.png)

我们讲HTTP发送请求的过程分为前端部分和后端部分，Forest本身是处理前端过程的框架，是对后端HTTP Api框架的进一步封装。

<b>前端部分：</b>

1. Forest配置： 负责管理HTTP发送请求所需的配置。
2. Forest注解： 用于定义HTTP发送请求的所有相关信息，一般定义在interface上和其方法上。
3. 动态代理： 用户定义好的HTTP请求的interface将通过动态代理产生实际执行发送请求过程的代理类。
4. 模板表达式： 模板表达式可以嵌入在几乎所有的HTTP请求参数定义中，它能够将用户通过参数或全局变量传入的数据动态绑定到HTTP请求信息中。
5. 数据转换： 此模块将字符串数据和JSON或XML形式数据进行互转。目前JSON转换器支持Jackson、Fastjson、Gson三种，XML仅支持JAXB一种。
6. 拦截器： 用户可以自定义拦截器，拦截指定的一个或一批请求的开始、成功返回数据、失败、完成等生命周期中的各个环节，以插入自定义的逻辑进行处理。
7. 过滤器： 用于动态过滤和处理传入HTTP请求的相关数据。
8. SSL： Forest支持单向和双向验证的HTTPS请求，此模块用于处理SSL相关协议的内容。

<b>后端部分：</b>

后端为实际执行HTTP请求发送过程的第三方HTTP Api，目前支持 Ok Http 3和Httpclient两种后端API。

<b>Spring Boot Starter Forest:</b>

提供对Spring Boot的支持


## 对应的Java版本

Forest 1.0.x和Forest 1.1.x基于JDK 1.7, Forest 1.2.x基于JDK 1.8

# 二. 安装

## 2.1 在SpringBoot项目中安装

若您的项目已经依赖了spring boot，那只要添加下面一个maven依赖便可。

```xml
<dependency>
    <groupId>com.dtflys.forest</groupId>
    <artifactId>spring-boot-starter-forest</artifactId>
    <version>1.2.0</version>
</dependency>
```
最新版本为<font color=red>*1.2.0*</font>，为稳定版本


## 2.2 在普通项目中安装

先添加后端HTTP API的依赖：okhttp3 或 httpclient 4.3.x.
以及JSON解析框架：jackson、fastjson或Gson
```xml
<dependency>
  <groupId>com.squareup.okhttp3</groupId>
  <artifactId>okhttp</artifactId>
  <version>3.3.0</version>
</dependency>

<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.47</version>
</dependency>
```

然后添加forest核心包依赖

```xml
<dependency>
    <groupId>com.dtflys.forest</groupId>
    <artifactId>forest-core</artifactId>
    <version>1.2.0</version>
</dependency>
```
最新版本为<font color=red>*1.2.0*</font>，为稳定版本

# 三. 配置

## 3.1 在SpringBoot项目中配置

若您的项目依赖spring boot，并加入了spring-boot-starter-forest依赖，就可以通过application.yml/application.properties方式定义配置。

### 3.1.1 配置forest启动开关
在application.yml中设置forest.enabled为true，便能开启forest。若设为false，springboot便不会再扫描forest。

```yaml
forest:
  enabled: true
```

### 3.1.2 配置后端HTTP API

```yaml
forest:
  enabled: true       
  backend: okhttp3         # 配置后端HTTP API为 okhttp3
```

目前Forest支持okhttp3和httpclient两种后端HTTP API，若不配置该属性，默认为okhttp3.
当然，您也可以改为httpclient

```yaml
forest:
  enabled: true       
  backend: httpclient         # 配置后端HTTP API为 httpclient
```

### 3.1.3 配置Bean ID 
Forest允许您在yaml文件中配置bean id，它对应着ForestConfiguration对象在spring上下文中的bean名称。

```yaml
forest:
  enabled: true
  bean-id: config0            # 在spring上下文中bean的id，默认值为forestConfiguration
```

然后便可以在spring中通过bean的名称引用到它

```java
@Resource(name = "config0")
private ForestConfiguration config0;
```

### 3.1.4 全局基本配置

在application.yaml / application.properties中配置的HTTP基本参数

```yaml
forest:
  enabled: true                           # forest开关
  bean-id: config0                        # 在spring上下文中bean的id, 默认值为forestConfiguration
  backend: okhttp3                        # 后端HTTP API： okhttp3
  max-connections: 1000                   # 连接池最大连接数，默认值为500
  max-route-connections: 500              # 每个路由的最大连接数，默认值为500
  timeout: 3000                           # 请求超时时间，单位为毫秒, 默认值为3000
  connect-timeout: 3000                   # 连接超时时间，单位为毫秒, 默认值为2000
  retry-count: 1                          # 请求失败后重试次数，默认为0次不重试
  ssl-protocol: SSLv3                     # 单向验证的HTTPS的默认SSL协议，默认为SSLv3
```

## 3.1 在普通项目中配置

若您的项目不是spring boot项目，或者没有依赖spring-boot-starter-forest，可以通过下面方式定义forest配置。

### 3.1.1 创建ForestConfiguration对象

ForestConfiguration为forest的全局配置对象类，所有的forest的全局基本配置信息由此类进行管理。

ForestConfiguration对象的创建方式：调用静态方法ForestConfiguration.configuration()，此方法会创建ForestConfiguration对象并初始化默认值。

```java
ForestConfiguration configuration = ForestConfiguration.configuration();
```

### 3.1.2 配置后端HTTP API

```java
configuration.setBackendName("okhttp3");
```

目前Forest支持okhttp3和httpclient两种后端HTTP API，若不配置该属性，默认为okhttp3.
当然，您也可以改为httpclient

```java
configuration.setBackendName("httpclient");
```

### 3.1.3 全局基本配置

```java
// 连接池最大连接数，默认值为500
configuration.setMaxConnections(123);
// 每个路由的最大连接数，默认值为500
configuration.setMaxRouteConnections(222);
// 请求超时时间，单位为毫秒, 默认值为3000
configuration.setTimeout(3000);
// 连接超时时间，单位为毫秒, 默认值为2000
configuration.setConnectTimeout(2000);
// 请求失败后重试次数，默认为0次不重试
configuration.setRetryCount(3);
// 单向验证的HTTPS的默认SSL协议，默认为SSLv3
configuration.setSslProtocol(SSLUtils.SSLv3);
```

# 四. 定义请求接口

## 4.1 简单请求定义

创建一个interface，并用@Request注解修饰接口方法。

```java
public interface MyClient {

    @Request(url = "http://localhost:5000/hello")
    String simpleRequest();

}
```

通过@Request注解，将上面的MyClient接口中的simpleRequest()方法绑定了一个HTTP请求，
其URL为http://localhost:5000/hello
，并默认使用GET方式，且将请求响应的数据以String的方式返回给调用者。


## 4.2 稍复杂点的请求定义

```java
public interface MyClient {

    @Request(
            url = "http://localhost:5000/hello/user",
            headers = {"Accept:text/plan"}
    )
    String sendRequest(@DataParam("uname") String username);
}
```
上面的sendRequest方法绑定的HTTP请求，定义了URL信息，以及把Accept:text/plan加到了请求头中，
方法的参数String username绑定了注解@DataParam("uname")，它的作用是将调用者传入入参username时，自动将username的值加入到HTTP的请求参数uname中。

如果调用方代码如下所示：
```java
MyClient client;
...
client.sendRequest("foo");
```
这段调用所实际产生的HTTP请求如下：

    GET http://localhost:5000/hello/user?uname=foo
    HEADER:
        Accept:text/plan

## 4.3 改变请求方式

使用POST方式

```java
public interface MyClient {

    @Request(
            url = "http://localhost:5000/hello",
            type = "POST"
    )
    String simplePost();
}
```
如果上面代码所示，可以通过@Request注解的type参数指定HTTP请求的方式。

除了POST，也可以指定成其他几种HTTP请求方式。

其中type属性的大小写不敏感，写成POST和post效果相同。

```java
public interface MyClient {

    // GET请求
    @Request(
            url = "http://localhost:5000/hello",
            type = "get"
    )
    String simpleGet();

    // POST请求
    @Request(
            url = "http://localhost:5000/hello",
            type = "post"
    )
    String simplePost();

    // PUT请求
    @Request(
            url = "http://localhost:5000/hello",
            type = "put"
    )
    String simplePut();

    // HEAD请求
    @Request(
            url = "http://localhost:5000/hello",
            type = "head"
    )
    String simpleHead();

    // Options请求
    @Request(
            url = "http://localhost:5000/hello",
            type = "options"
    )
    String simpleOptions();

    // Delete请求
    @Request(
            url = "http://localhost:5000/hello",
            type = "delete"
    )
    String simpleDelete();

}
```







