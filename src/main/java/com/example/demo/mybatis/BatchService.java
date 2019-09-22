package com.example.demo.mybatis;

import com.example.demo.db.primary.dao.PersonModelMapper;
import com.example.demo.db.primary.dao.ext.BeanExtMapper;
import com.example.demo.db.primary.model.PersonModel;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author zzs
 * @date 2019/9/22 20:02
 */
@Service
public class BatchService {

    @Autowired
    private BeanExtMapper beanExtMapper;

    @Autowired
    @Qualifier("primarySqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * mybatis批量提交
     *      一：foreach标签
     *      二：如下
     */
    public void batchFun1() {
        this.beanExtMapper.batchInsert(new ArrayList<PersonModel>());
    }

    public void batchFun2() {
        //新获取一个模式为BATCH，自动提交为false的session
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        PersonModelMapper mapper = sqlSession.getMapper(PersonModelMapper.class);
        try {
            for (int i = 0; i < 10; i++) {
                //或者使用
                //sqlSession.insert("com.example.demo.db.primary.dao.PersonModelMapper.insertSelective", new PersonModel());
                //主意这时候不能正确返回影响条数了
                mapper.insertSelective(new PersonModel());
            }
            sqlSession.commit();
            //清理缓存，防止溢出
            //sqlSession.clearCache();
        } catch (Exception e) {
            //异常回滚
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }
}
