package com.github.service;


/**
* @author oldhand
* @date 2020-01-01
*/
public interface YearMonthContentIdsService {

    String findById(Long id);

    Long create(String xnType);

    void delete(Long id);
}