有两种方式可以实现mybatis的批量提交，第一种便是foreach标签，第二种便是开始mybatis的批量模式。

## 一、通过foreach标签

foreach标签即是通过迭代传入的集合或者数组，进行拼接sql。

标签包含以下属性：

​	item：表示集合中每一个元素进行迭代时的别名

​	index：表示每次迭代到的位置

​	open：表示该语句以什么开始

​	separator：表示在每次进行迭代之间以什么符号作为分隔符

​	close：表示以什么结束

​	collection：表示要迭代的对象，可以是集合则属性值为list，也可以是数组则属性值为array



示例：

```xml
<insert id="batchInsert" parameterType="java.util.List">
    insert into person (
        id, `name`, age, phone, address, created_time, updated_time
    )
    values
    <foreach collection="list" item="item" index="index" separator=",">
    (
        #{item.id,jdbcType=BIGINT},
        #{item.name,jdbcType=VARCHAR},
        #{item.age,jdbcType=TINYINT},
        #{item.phone,jdbcType=VARCHAR},
        #{item.address,jdbcType=VARCHAR},
        #{item.createdTime,jdbcType=BIGINT},
        #{item.updatedTime,jdbcType=BIGINT}
    )
    </foreach>
</insert>
```



实际生成的sql是这样的：

```mysql
insert into person ( id, `name`, age, phone, address, created_time, updated_time ) values ( ?, ?, ?, ?, ?, ?, ? ) , ( ?, ?, ?, ?, ?, ?, ? )
```



## 二、通过Mybatis的ExecutorType.BATCH方式

Mybatis内置的ExecutorType有3种，默认的是simple，该模式下它为每个语句的执行创建一个新的预处理语句，单条提交sql；而batch模式重复使用已经预处理的语句，并且批量执行所有更新语句，显然batch性能将更优；

批量提交只能应用于 insert, update, delete
这里我们手动控制事务。



示例：

```java
@Autowired
private SqlSessionTemplate sqlSessionTemplate;

public void exeBatch() {
    //新获取一个模式为BATCH，自动提交为false的session
    SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
    PersonModelMapper mapper = sqlSession.getMapper(PersonModelMapper.class);
    try {
        for (int i = 0; i < 10; i++) {
            //或者使用
            //sqlSession.insert("com.example.demo.db.dao.PersonModelMapper.insertSelective", new PersonModel());
            //主意这时候不能正确返回影响条数了
            mapper.insertSelective(new PersonModel());
        }
        sqlSession.commit();
        // 清理缓存，防止溢出
        sqlSession.clearCache();
    } catch (Exception e) {
        //异常回滚
        sqlSession.rollback();
    } finally {
        sqlSession.close();
    }
}
```

