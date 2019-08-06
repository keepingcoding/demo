package com.example.demo.db.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author zhaozisheng
 * @version 1.0
 * @date 2019-05-10 11:22
 */
@Configuration
public class MySqlConfig {

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        return DruidDataSourceBuilder.create().build();
        //return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
