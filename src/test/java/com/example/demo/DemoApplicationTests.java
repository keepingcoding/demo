package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * DataSource测试
     *
     * @throws Exception
     */
    @Test
    public void contextLoads() throws Exception {
        System.err.println(dataSourceProperties.getUrl());
        System.err.println(dataSourceProperties.getUsername());
        System.err.println(dataSourceProperties.getPassword());
        System.err.println(dataSourceProperties.getDriverClassName());


        DataSource ds = applicationContext.getBean(DataSource.class);
        System.err.println(ds.getClass().getSimpleName());

        JdbcTemplate jt = applicationContext.getBean(JdbcTemplate.class);
        List<Map<String, Object>> maps = jt.queryForList("select * from logs");
        for (Map<String, Object> map : maps) {
            System.err.println(map);
        }
    }

    @Test
    public void contextLoads2() throws Exception {
        System.err.println(dataSource);
        System.err.println(dataSource.getClass());

        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from logs");
        for (Map<String, Object> map : maps) {
            System.err.println(map);
        }
    }

}
