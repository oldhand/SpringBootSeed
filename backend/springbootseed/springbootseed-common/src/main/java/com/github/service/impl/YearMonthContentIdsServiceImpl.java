package com.github.service.impl;

import com.github.domain.YearMonthContentIds;
import com.github.repository.YearMonthContentIdsRepository;
import com.github.service.YearMonthContentIdsService;
import com.github.utils.ValidationUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
* @author oldhand
* @date 2020-01-01
*/
@Service
@CacheConfig(cacheNames = "contentIds")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YearMonthContentIdsServiceImpl implements YearMonthContentIdsService {

    private final YearMonthContentIdsRepository yearMonthContentIdsRepository;

    public YearMonthContentIdsServiceImpl(YearMonthContentIdsRepository yearMonthContentIdsRepository) {
        this.yearMonthContentIdsRepository = yearMonthContentIdsRepository;
    }

    @Override
    @Cacheable(key = "#p0")
    public String findById(Long id) {
        YearMonthContentIds yearMonthContentIds = yearMonthContentIdsRepository.findById(id).orElseGet(YearMonthContentIds::new);
        ValidationUtil.isNull(yearMonthContentIds.getId(),"YearMonthContentIds","id",id);
        return yearMonthContentIds.getXnType();
    }

    @Override
    public Long create(String xnType) {
        YearMonthContentIds resources = new YearMonthContentIds();
        resources.setXnType(xnType);
        return yearMonthContentIdsRepository.save(resources).getId();
    }

    @Override
    public void delete(Long id) {
        yearMonthContentIdsRepository.deleteById(id);
    }


}