package com.github.cores.service.impl;

import com.github.cores.domain.Tabs2permissions;
import com.github.exception.EntityExistException;
import com.github.utils.ValidationUtil;
import com.github.utils.FileUtil;
import com.github.cores.repository.Tabs2permissionsRepository;
import com.github.cores.service.Tabs2permissionsService;
import com.github.cores.service.dto.Tabs2permissionsDTO;
import com.github.cores.service.dto.Tabs2permissionsQueryCriteria;
import com.github.cores.service.mapper.Tabs2permissionsMapper;
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
* @date 2020-01-14
*/
@Service
@CacheConfig(cacheNames = "Tabs2permissions")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class Tabs2permissionsServiceImpl implements Tabs2permissionsService {

    private final Tabs2permissionsRepository Tabs2permissionsRepository;

    private final Tabs2permissionsMapper Tabs2permissionsMapper;

    public Tabs2permissionsServiceImpl(Tabs2permissionsRepository Tabs2permissionsRepository, Tabs2permissionsMapper Tabs2permissionsMapper) {
        this.Tabs2permissionsRepository = Tabs2permissionsRepository;
        this.Tabs2permissionsMapper = Tabs2permissionsMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(Tabs2permissionsQueryCriteria criteria, Pageable pageable){
        Page<Tabs2permissions> page = Tabs2permissionsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(Tabs2permissionsMapper::toDto));
    }

    @Override
    @Cacheable
    public List<Tabs2permissionsDTO> queryAll(Tabs2permissionsQueryCriteria criteria){
        return Tabs2permissionsMapper.toDto(Tabs2permissionsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public Tabs2permissionsDTO findById(Long id) {
        Tabs2permissions Tabs2permissions = Tabs2permissionsRepository.findById(id).orElseGet(Tabs2permissions::new);
        ValidationUtil.isNull(Tabs2permissions.getId(),"Tabs2permissions","id",id);
        return Tabs2permissionsMapper.toDto(Tabs2permissions);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public Tabs2permissionsDTO create(Tabs2permissions resources) {
        return Tabs2permissionsMapper.toDto(Tabs2permissionsRepository.saveAndFlush(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public Tabs2permissionsDTO update(Long id,Tabs2permissions resources) {
        Tabs2permissions Tabs2permissions = Tabs2permissionsRepository.findById(id).orElseGet(Tabs2permissions::new);
        ValidationUtil.isNull( Tabs2permissions.getId(),"Tabs2permissions","id",resources.getId());
        Tabs2permissions.copy(resources);
		return Tabs2permissionsMapper.toDto(Tabs2permissionsRepository.saveAndFlush(Tabs2permissions));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Tabs2permissionsRepository.deleteById(id);
    }
	
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void makedelete(Long id) {
         Tabs2permissions Tabs2permissions = Tabs2permissionsRepository.findById(id).orElseGet(Tabs2permissions::new);
		 ValidationUtil.isNull( Tabs2permissions.getId(),"Tabs2permissions","id",id);
		 Tabs2permissions.setDeleted(1);
		 Tabs2permissionsRepository.save(Tabs2permissions);
    }


    @Override
    public void download(List<Tabs2permissionsDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Tabs2permissionsDTO Tabs2permissions : all) {
            Map<String,Object> map = new LinkedHashMap<>();
		    map.put("ID", Tabs2permissions.getId());
		    map.put("创建日期", Tabs2permissions.getPublished());
		    map.put("更新日期", Tabs2permissions.getUpdated());
		    map.put("创建者", Tabs2permissions.getAuthor());
		    map.put("删除标记", Tabs2permissions.getDeleted());
            map.put("saasid",  Tabs2permissions.getSaasid());
            map.put("权限ID", Tabs2permissions.getPermissionid());
            map.put("模块ID", Tabs2permissions.getTabid());
            map.put("全部权限", Tabs2permissions.getAll());
            map.put("编辑权限", Tabs2permissions.getEdit());
            map.put("删除权限", Tabs2permissions.getDelete());
            map.put("查询权限", Tabs2permissions.getQuery());
            map.put("新增权限", Tabs2permissions.getAdd());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}