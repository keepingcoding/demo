发送邮件包含四种格式：
①.普通文本邮件
②.带图片的邮件
③.带附件的邮件
④.带图片和附件的复杂邮件

使用方式见：
com.example.demo.mail.TestMail.java

Spring方式使用则是将前期的配置信息写入properties或者xml文件中，然后通过spring返回的JavaMailSender来发送邮件

```properties
################################### SpringMail ########################################
### SpringMail
spring.mail.host=smtp.163.com
spring.mail.username=xxx@163.com
spring.mail.password=xxx
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.protocol=smtp
spring.mail.default-encoding=UTF-8
```

详见：
com.example.demo.mail.boot.TestSpringMail.java