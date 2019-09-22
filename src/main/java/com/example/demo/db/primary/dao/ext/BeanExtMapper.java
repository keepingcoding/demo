package com.example.demo.db.primary.dao.ext;

import com.example.demo.db.primary.model.PersonModel;

import java.util.List;

public interface BeanExtMapper {

    int batchInsert(List<PersonModel> list);
}
