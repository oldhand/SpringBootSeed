package com.github.cores.service.impl;

import com.github.cores.domain.Settings;
import com.github.exception.BadRequestException;
import com.github.exception.EntityExistException;
import com.github.utils.ValidationUtil;
import com.github.utils.FileUtil;
import com.github.cores.repository.SettingsRepository;
import com.github.cores.service.SettingsService;
import com.github.cores.service.dto.SettingsDTO;
import com.github.cores.service.dto.SettingsQueryCriteria;
import com.github.cores.service.mapper.SettingsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.github.utils.PageUtil;
import com.github.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author oldhand
* @date 2020-01-15
*/
@Service
@CacheConfig(cacheNames = "Settings")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SettingsServiceImpl implements SettingsService {

    private final SettingsRepository SettingsRepository;

    private final SettingsMapper SettingsMapper;

    public SettingsServiceImpl(SettingsRepository SettingsRepository, SettingsMapper SettingsMapper) {
        this.SettingsRepository = SettingsRepository;
        this.SettingsMapper = SettingsMapper;
    }

    @Override
    @Cacheable
    public List<SettingsDTO> query(){
        return SettingsMapper.toDto(SettingsRepository.findAll());
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public SettingsDTO update(Settings resources) {
        List<Settings> settings = SettingsRepository.findAll();
        if (settings.size() == 0) {
            throw new BadRequestException("没有找到系统设置数据");
        }
        Settings setting = settings.get(0);
        setting.copy(resources);
		return SettingsMapper.toDto(SettingsRepository.saveAndFlush(setting));
    }

}