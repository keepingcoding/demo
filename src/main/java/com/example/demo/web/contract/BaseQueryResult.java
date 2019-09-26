package com.example.demo.web.contract;

import lombok.Data;

import java.util.List;

@Data
public class BaseQueryResult<T> {

    private PageInfo pageInfo;

    private List<T> list;
}
