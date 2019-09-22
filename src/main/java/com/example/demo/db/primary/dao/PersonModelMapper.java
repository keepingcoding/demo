package com.example.demo.db.primary.dao;

import com.example.demo.db.primary.model.PersonModel;
import com.example.demo.db.primary.model.example.PersonModelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PersonModelMapper {
    long countByExample(PersonModelExample example);

    int deleteByExample(PersonModelExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PersonModel record);

    int insertSelective(PersonModel record);

    List<PersonModel> selectByExample(PersonModelExample example);

    PersonModel selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PersonModel record, @Param("example") PersonModelExample example);

    int updateByExample(@Param("record") PersonModel record, @Param("example") PersonModelExample example);

    int updateByPrimaryKeySelective(PersonModel record);

    int updateByPrimaryKey(PersonModel record);
}