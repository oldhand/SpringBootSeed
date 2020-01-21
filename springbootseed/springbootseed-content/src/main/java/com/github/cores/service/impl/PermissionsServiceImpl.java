package com.github.cores.service.impl;

import com.github.cores.domain.Permissions;
import com.github.exception.EntityExistException;
import com.github.utils.ValidationUtil;
import com.github.utils.FileUtil;
import com.github.cores.repository.PermissionsRepository;
import com.github.cores.service.PermissionsService;
import com.github.cores.service.dto.PermissionsDTO;
import com.github.cores.service.dto.PermissionsQueryCriteria;
import com.github.cores.service.mapper.PermissionsMapper;
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
* @date 2020-01-02
*/
@Service
@CacheConfig(cacheNames = "Permissions")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PermissionsServiceImpl implements PermissionsService {

    private final PermissionsRepository PermissionsRepository;

    private final PermissionsMapper PermissionsMapper;

    public PermissionsServiceImpl(PermissionsRepository PermissionsRepository, PermissionsMapper PermissionsMapper) {
        this.PermissionsRepository = PermissionsRepository;
        this.PermissionsMapper = PermissionsMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(PermissionsQueryCriteria criteria, Pageable pageable){
        Page<Permissions> page = PermissionsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(PermissionsMapper::toDto));
    }

    @Override
    @Cacheable
    public List<PermissionsDTO> queryAll(PermissionsQueryCriteria criteria){
        return PermissionsMapper.toDto(PermissionsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public PermissionsDTO findById(Long id) {
        Permissions Permissions = PermissionsRepository.findById(id).orElseGet(Permissions::new);
        ValidationUtil.isNull(Permissions.getId(),"Permissions","id",id);
        return PermissionsMapper.toDto(Permissions);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public PermissionsDTO create(Permissions resources) {
        return PermissionsMapper.toDto(PermissionsRepository.saveAndFlush(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public PermissionsDTO update(Long id,Permissions resources) {
        Permissions Permissions = PermissionsRepository.findById(id).orElseGet(Permissions::new);
        ValidationUtil.isNull( Permissions.getId(),"Permissions","id",resources.getId());
        Permissions.copy(resources);
		return PermissionsMapper.toDto(PermissionsRepository.saveAndFlush(Permissions));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PermissionsRepository.deleteById(id);
    }
	
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void makedelete(Long id) {
         Permissions Permissions = PermissionsRepository.findById(id).orElseGet(Permissions::new);
		 ValidationUtil.isNull( Permissions.getId(),"Permissions","id",id);
		 Permissions.setDeleted(1);
		 PermissionsRepository.save(Permissions);
    }


    @Override
    public void download(List<PermissionsDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PermissionsDTO Permissions : all) {
            Map<String,Object> map = new LinkedHashMap<>();
		    map.put("ID", Permissions.getId());
		    map.put("创建日期", Permissions.getPublished());
		    map.put("更新日期", Permissions.getUpdated());
		    map.put("创建者", Permissions.getAuthor());
		    map.put("删除标记", Permissions.getDeleted());
            map.put("name",  Permissions.getName());
            map.put("description",  Permissions.getDescription());
            map.put("allowdeleted",  Permissions.getAllowdeleted());
            map.put("globalAllView",  Permissions.getGlobalAllView());
            map.put("globalAllEdit",  Permissions.getGlobalAllEdit());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}