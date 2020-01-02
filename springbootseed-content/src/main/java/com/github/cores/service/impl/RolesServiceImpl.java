package com.github.cores.service.impl;

import com.github.cores.domain.Roles;
import com.github.exception.EntityExistException;
import com.github.utils.ValidationUtil;
import com.github.utils.FileUtil;
import com.github.cores.repository.RolesRepository;
import com.github.cores.service.RolesService;
import com.github.cores.service.dto.RolesDTO;
import com.github.cores.service.dto.RolesQueryCriteria;
import com.github.cores.service.mapper.RolesMapper;
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
@CacheConfig(cacheNames = "Roles")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RolesServiceImpl implements RolesService {

    private final RolesRepository RolesRepository;

    private final RolesMapper RolesMapper;

    public RolesServiceImpl(RolesRepository RolesRepository, RolesMapper RolesMapper) {
        this.RolesRepository = RolesRepository;
        this.RolesMapper = RolesMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(RolesQueryCriteria criteria, Pageable pageable){
        Page<Roles> page = RolesRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(RolesMapper::toDto));
    }

    @Override
    @Cacheable
    public List<RolesDTO> queryAll(RolesQueryCriteria criteria){
        return RolesMapper.toDto(RolesRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public RolesDTO findById(Long id) {
        Roles Roles = RolesRepository.findById(id).orElseGet(Roles::new);
        ValidationUtil.isNull(Roles.getId(),"Roles","id",id);
        return RolesMapper.toDto(Roles);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public RolesDTO create(Roles resources) {
        return RolesMapper.toDto(RolesRepository.saveAndFlush(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public RolesDTO update(Long id,Roles resources) {
        Roles Roles = RolesRepository.findById(id).orElseGet(Roles::new);
        ValidationUtil.isNull( Roles.getId(),"Roles","id",resources.getId());
        Roles.copy(resources);
		return RolesMapper.toDto(RolesRepository.saveAndFlush(Roles));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        RolesRepository.deleteById(id);
    }
	
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void makedelete(Long id) {
         Roles Roles = RolesRepository.findById(id).orElseGet(Roles::new);
		 ValidationUtil.isNull( Roles.getId(),"Roles","id",id);
		 Roles.setDeleted(1);
		 RolesRepository.save(Roles);
    }


    @Override
    public void download(List<RolesDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RolesDTO Roles : all) {
            Map<String,Object> map = new LinkedHashMap<>();
		    map.put("ID", Roles.getId());
		    map.put("创建日期", Roles.getPublished());
		    map.put("更新日期", Roles.getUpdated());
		    map.put("创建者", Roles.getAuthor());
		    map.put("删除标记", Roles.getDeleted());
            map.put("name",  Roles.getName());
            map.put("description",  Roles.getDescription());
            map.put("profileids",  Roles.getProfileids());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}