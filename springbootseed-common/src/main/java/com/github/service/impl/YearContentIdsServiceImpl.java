package com.github.service.impl;

import com.github.domain.YearContentIds;
import com.github.repository.YearContentIdsRepository;
import com.github.service.YearContentIdsService;
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
public class YearContentIdsServiceImpl implements YearContentIdsService {

    private final YearContentIdsRepository yearContentIdsRepository;

    public YearContentIdsServiceImpl(YearContentIdsRepository yearContentIdsRepository) {
        this.yearContentIdsRepository = yearContentIdsRepository;
    }

    @Override
    @Cacheable(key = "#p0")
    public String findById(Long id) {
        YearContentIds yearContentIds = yearContentIdsRepository.findById(id).orElseGet(YearContentIds::new);
        ValidationUtil.isNull(yearContentIds.getId(),"YearContentIds","id",id);
        return yearContentIds.getXnType();
    }

    @Override
    public Long create(String xnType) {
        YearContentIds resources = new YearContentIds();
        resources.setXnType(xnType);
        return yearContentIdsRepository.save(resources).getId();
    }

    @Override
    public void delete(Long id) {
        yearContentIdsRepository.deleteById(id);
    }


}