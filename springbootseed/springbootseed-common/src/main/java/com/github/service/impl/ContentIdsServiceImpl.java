package com.github.service.impl;

import com.github.domain.ContentIds;
import com.github.repository.ContentIdsRepository;
import com.github.service.ContentIdsService;
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
public class ContentIdsServiceImpl implements ContentIdsService {

    private final ContentIdsRepository contentIdsRepository;

    public ContentIdsServiceImpl(ContentIdsRepository contentIdsRepository) {
        this.contentIdsRepository = contentIdsRepository;
    }

    @Override
    @Cacheable(key = "#p0")
    public String findById(Long id) {
        ContentIds contentIds = contentIdsRepository.findById(id).orElseGet(ContentIds::new);
        ValidationUtil.isNull(contentIds.getId(),"ContentIds","id",id);
        return contentIds.getXnType();
    }

    @Override
    public Long create(String xnType) {
        ContentIds resources = new ContentIds();
        resources.setXnType(xnType);
        return contentIdsRepository.save(resources).getId();
    }

    @Override
    public void delete(Long id) {
        contentIdsRepository.deleteById(id);
    }


}