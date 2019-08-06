package com.example.demo.hibernate;

import com.example.demo.hibernate.tradition.Person;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author zzs
 * @date 2019/6/18 13:33
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSpringBootHibernate {

    //@PersistenceContext
    //private EntityManager entityManager;


    /**
     * 我们都知道hibernate首先要获取sessionFactory，然后从sessionFactory中获取session进行持久化操作。
     * 那么如何获取这个Session呢？
     * 其实SpringBoot自动帮我们配置好了一个EntityManagerFactory，这个EntityManagerFactory里面就有我
     * 们需要的session。使用时，只需要@Autowired这个EntityManagerFactory，然后用openSession
     * 或者getCurrentSession方法即可拿到session。
     */
    @Test
    public void testSimpleSession() {

    }
}
