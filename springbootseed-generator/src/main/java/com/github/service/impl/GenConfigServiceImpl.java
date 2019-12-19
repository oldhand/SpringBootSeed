package com.github.service.impl;

import com.github.domain.GenConfig;
import com.github.repository.GenConfigRepository;
import com.github.service.GenConfigService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.Optional;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Service
@CacheConfig(cacheNames = "genConfig")
public class GenConfigServiceImpl implements GenConfigService {

    private final GenConfigRepository genConfigRepository;

    public GenConfigServiceImpl(GenConfigRepository genConfigRepository) {
        this.genConfigRepository = genConfigRepository;
    }

    @Override
    @Cacheable(key = "'1'")
    public GenConfig find() {
        Optional<GenConfig> genConfig = genConfigRepository.findById(1L);
        return genConfig.orElseGet(GenConfig::new);
    }

    @Override
    @CacheEvict(allEntries = true)
    public GenConfig update(GenConfig genConfig) {
        genConfig.setId(1L);
        return genConfigRepository.save(genConfig);
    }
}
