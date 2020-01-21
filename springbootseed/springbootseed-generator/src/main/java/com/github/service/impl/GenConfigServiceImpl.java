package com.github.service.impl;

import com.github.domain.GenConfig;
import com.github.exception.BadRequestException;
import com.github.repository.GenConfigRepository;
import com.github.service.GenConfigService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.List;
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
        GenConfig genconfig = new GenConfig();
        Example<GenConfig> genconfigexample = Example.of(genconfig);
        List<GenConfig> genconfigs = genConfigRepository.findAll(genconfigexample);
        if (genconfigs.size() > 0) {
            return genconfigs.get(0);
        }
        Optional<GenConfig> genConfig = genConfigRepository.findById(1L);
        return genConfig.orElseGet(GenConfig::new);
    }

    @Override
    @CacheEvict(allEntries = true)
    public GenConfig update(GenConfig resources) {
        GenConfig query = new GenConfig();
        Example<GenConfig> genconfigexample = Example.of(query);
        List<GenConfig> genconfigs = genConfigRepository.findAll(genconfigexample);
        if (genconfigs.size() == 0) {
            throw new BadRequestException("GenConfig为空表！");
        }
        GenConfig genConfig = genconfigs.get(0);
        genConfig.copy(resources);
        return genConfigRepository.save(genConfig);
    }
}
