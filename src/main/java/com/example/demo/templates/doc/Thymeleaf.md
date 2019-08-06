# SpringBoot整合Thymeleaf
## 添加Thymeleaf依赖
```xml
<!-- thymeleaf -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```
Spring Boot默认存放模板页面的路径在src/main/resources/templates或者src/main/view/templates，这个无论是使用什么模板语言都一样，当然默认路径是可以自定义的，不过一般不推荐这样做。另外Thymeleaf默认的页面文件后缀是.html。

## 添加配置

```properties
#thymeleaf start
#默认严格检查
spring.thymeleaf.mode=HTML5
#非严格检查
#spring.thymeleaf.mode=LEGACYHTML5
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.servlet.content-type=text/html
#开发时关闭缓存,不然没法看到实时页面
spring.thymeleaf.cache=false
#spring.thymeleaf.prefix=classpath:/templates/
## Suffix that gets appended to view names when building a URL.
#spring.thymeleaf.suffix=.html
#thymeleaf end
```


## 使用

见com.example.demo.templates.thymeleaf.controller.ThymeleafController.java
和
templates/thymeleafPage.html
