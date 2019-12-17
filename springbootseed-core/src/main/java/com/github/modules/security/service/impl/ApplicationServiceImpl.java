package com.github.modules.security.service.impl;

import com.github.exception.EntityExistException;
import com.github.exception.EntityNotFoundException;
import com.github.modules.monitor.service.RedisService;
import com.github.modules.security.domain.Application;
import com.github.modules.security.repository.ApplicationRepository;
import com.github.modules.security.service.ApplicationService;
import com.github.modules.security.service.dto.ApplicationDTO;
import com.github.modules.security.service.dto.ApplicationQueryCriteria;
import com.github.modules.security.service.mapper.ApplicationMapper;
import com.github.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Service
@CacheConfig(cacheNames = "application")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final ApplicationMapper applicationMapper;

    private final RedisService redisService;


    public ApplicationServiceImpl(ApplicationRepository applicationRepository, ApplicationMapper applicationMapper, RedisService redisService) {
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
        this.redisService = redisService;
    }

    @Override
    @Cacheable(key = "#p0")
    public ApplicationDTO findByAppid(String appid) {
        Application application = applicationRepository.findByAppid(appid);
        ValidationUtil.isNull(application.getId(),"Application","appid",appid);
        return applicationMapper.toDto(application);
    }
}
