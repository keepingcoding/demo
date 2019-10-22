有时候我们需要一个项目中连接多个数据源，这里我们将展示spring boot以及xml方式配置多数据源的方式。

### 一、Springboot配置多数据源

spring boot配置多数据源，只需要先将数据库连接信息写入配置文件，然后编写相应的数据源配置类，最后在spring boot启动中排除默认配置的数据源即可。

#### 1.添加数据源连接信息

在application.properties中加入如下配置：（示例中其实是同一个数据源不同的库）

```properties
### 主数据源
spring.datasource.primary.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.primary.url=jdbc:mysql:///zzs?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&useSSL=false&verifyServerCertificate=false&serverTimezone=GMT
spring.datasource.primary.username=root
spring.datasource.primary.password=root

### 第二数据源
spring.datasource.secondary.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.secondary.url=jdbc:mysql:///weblog?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&useSSL=false&verifyServerCertificate=false&serverTimezone=GMT
spring.datasource.secondary.username=root
spring.datasource.secondary.password=root
```

#### 2.编写每一个数据源对应的配置类

主数据源对应的配置类：

以下加入了JdbcTemplate以及Mybatis以及事务等的支持，如果不需要，去掉相应的方法即可

```java
package com.example.demo.config.ds;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author zzs
 * @date 2019/9/10 20:39
 */
@Configuration
//@MapperScan指定mybatis的扫描路径
@MapperScan(basePackages = "com.example.demo.db.primary.dao", sqlSessionTemplateRef  = "primarySqlSessionTemplate")
public class PrimaryDataSourceConfig {

    /**
     * 这里的@Primary注解是指存在多个相同类型的bean时，首选哪个进行注入
     *
     * 因为配置了多个相同的bean，因此@Bean注解要指定bean名称，后面注入时也需要@Qualifier指定哪个bean
     */
    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        //这里根据配置中的spring.datasource.primary前缀实例化数据源
        return DruidDataSourceBuilder.create().build();
    }

    /** JdbcTemplate支持 **/
    @Primary
    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /** Mybatis支持 **/
    @Primary
    @Bean(name = "primarySqlSessionFactory")
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        //Mybatis配置文件
        //bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml"));
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:com/example/demo/db/primary/**/*.xml"));
        return bean.getObject();
    }

    /** 事务 **/
    @Primary
    @Bean(name = "primaryTransactionManager")
    public DataSourceTransactionManager primaryTransactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "primarySqlSessionTemplate")
    public SqlSessionTemplate primarySqlSessionTemplate(@Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
```

第二个数据源的配置类，也是一样的：

```java
package com.example.demo.config.ds;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author zzs
 * @date 2019/9/10 21:13
 */
@Configuration
@MapperScan(basePackages = "com.example.demo.db.secondary.dao", sqlSessionTemplateRef  = "secondarySqlSessionTemplate")
public class SecondaryDataSourceConfig {

    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "secondaryJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(@Qualifier("secondaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "secondarySqlSessionFactory")
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("secondaryDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        //bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml"));
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:com/example/demo/db/secondary/**/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "secondaryTransactionManager")
    public DataSourceTransactionManager primaryTransactionManager(@Qualifier("secondaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "secondarySqlSessionTemplate")
    public SqlSessionTemplate primarySqlSessionTemplate(@Qualifier("secondarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
```



#### 3.排除Springboot默认的数据源

在启动类上添加如下：

```java
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
public class SingleDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SingleDemoApplication.class, args);
    }
}
```



#### 4.测试

这里就不展示了。



以上就是Springboot配置多数据源的方式，下面我们继续探索传统的xml配置方式



### 二、xml方式配置多数据源

xml比较简单，直接加入另一个数据源连接信息，然后指定其中一个为默认的即可：

这里有两个xml文件，spring-dao.xml和spring-beanContext.xml，第一个spring-dao.xml是数据源的配置，包括数据库连接信息、事务等，第二个spring-beanContext.xml是mybatis的相关配置。



#### spring-dao.xml

这里的事务只配置了一个

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop.xsd">

    <bean id="parentDataSource" class="com.alibaba.druid.pool.DruidDataSource" abstract="true">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
        <property name="maxActive" value="10"/>
        <property name="minIdle" value="1"/>
        <property name="initialSize" value="3"/>
        <property name="maxWait" value="3000"/>
        <property name="testWhileIdle" value="true" />
        <property name="timeBetweenEvictionRunsMillis" value="180000" />
        <property name="validationQuery" value="SELECT 1"/>
        <property name="validationQueryTimeout" value="3"/>
        <property name="filters" value="config"/>
    </bean>
    
    <!-- 其中primary="true"即表示该数据源为默认数据源 -->
    <!-- primary -->
    <bean id="primaryDataSource" parent="parentDataSource" init-method="init" destroy-method="close" primary="true">
        <property name="url" value=""/>
        <property name="username" value=""/>
        <property name="password" value=""/>

    </bean>

    <!-- secondary -->
    <bean id="secondaryDataSource" parent="parentDataSource" init-method="init" destroy-method="close">
        <property name="url" value=""/>
        <property name="username" value=""/>
        <property name="password" value=""/>
    </bean>


    <!-- 配置jdbcTemplate -->
    <bean name="primaryJdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="primaryDataSource"/>
    </bean>

    <bean name="secondaryJdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="secondaryDataSource"/>
    </bean>


    <!-- 库事务管理配置  开始 -->
    <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="primaryDataSource"/>
    </bean>

    <!-- ①使用注解 -->
    <!--<tx:annotation-driven  transaction-manager="txManager" />-->

    <!-- ②使用AOP切面的方式 -->
    <tx:advice id="txAdvice" transaction-manager="txManager">
        <tx:attributes>
            <tx:method name="create*" propagation="REQUIRED" rollback-for="java.lang.Exception"/>
            <tx:method name="add*" propagation="REQUIRED" rollback-for="java.lang.Exception"/>
            <tx:method name="save*" propagation="REQUIRED" rollback-for="java.lang.Exception" isolation="READ_COMMITTED"/>
            <tx:method name="insert*" propagation="REQUIRED" rollback-for="java.lang.Exception"/>
            <tx:method name="del*" propagation="REQUIRED" rollback-for="java.lang.Exception"/>
            <tx:method name="update*" propagation="REQUIRED" rollback-for="java.lang.Exception"/>
            
            <!--<tx:method name="*" read-only="true"/>-->
        </tx:attributes>
    </tx:advice>

    <aop:config>
        <aop:pointcut id="allManagerMethod1"
                      expression="execution(* com.example..*.service..*(..))"/>
        <aop:advisor advice-ref="txAdvice" pointcut-ref="allManagerMethod1"/>
    </aop:config>

    <!-- 主库事务管理配置  结束  -->
</beans>
```



#### spring-beanContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- mybatis start -->

    <!-- primary -->
    <bean id="primarySqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="primaryDataSource"/>
        <property name="mapperLocations"
                  value="classpath:com/example/demo/db/primary/**/*.xml"/>
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
    </bean>
    <bean id="primarySqlSessionTemplate" class="org.mybatis.spring.SqlSessionTemplate">
        <constructor-arg index="0" ref="primarySqlSessionFactory" />
    </bean>
    <bean id="primaryDb" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.example.demo.db.primary.dao"/>
        <property name="sqlSessionTemplateBeanName" value="primarySqlSessionTemplate"/>
    </bean>

    <!-- secondary -->
    <bean id="secondarySqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="secondaryDataSource"/>
        <property name="mapperLocations"
                  value="classpath:com/example/demo/db/secondary/**/*.xml"/>
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
    </bean>
    <bean id="secondarySqlSessionTemplate" class="org.mybatis.spring.SqlSessionTemplate">
        <constructor-arg index="0" ref="secondarySqlSessionFactory" />
    </bean>
    <bean id="secondaryDb" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.example.demo.db.secondary.dao"/>
        <property name="sqlSessionTemplateBeanName" value="secondarySqlSessionTemplate"/>
    </bean>

    <!-- mybatis end -->

</beans>
```



最后，在applicationContext.xml中引入这两个文件即可。