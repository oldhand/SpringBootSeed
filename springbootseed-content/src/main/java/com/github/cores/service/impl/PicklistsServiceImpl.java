package com.github.cores.service.impl;

import com.github.cores.domain.Picklists;
import com.github.exception.EntityExistException;
import com.github.utils.ValidationUtil;
import com.github.utils.FileUtil;
import com.github.cores.repository.PicklistsRepository;
import com.github.cores.service.PicklistsService;
import com.github.cores.service.dto.PicklistsDTO;
import com.github.cores.service.dto.PicklistsQueryCriteria;
import com.github.cores.service.mapper.PicklistsMapper;
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
* @date 2020-01-03
*/
@Service
@CacheConfig(cacheNames = "Picklists")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PicklistsServiceImpl implements PicklistsService {

    private final PicklistsRepository PicklistsRepository;

    private final PicklistsMapper PicklistsMapper;

    public PicklistsServiceImpl(PicklistsRepository PicklistsRepository, PicklistsMapper PicklistsMapper) {
        this.PicklistsRepository = PicklistsRepository;
        this.PicklistsMapper = PicklistsMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(PicklistsQueryCriteria criteria, Pageable pageable){
        Page<Picklists> page = PicklistsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(PicklistsMapper::toDto));
    }

    @Override
    @Cacheable
    public List<PicklistsDTO> queryAll(PicklistsQueryCriteria criteria){
        return PicklistsMapper.toDto(PicklistsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public PicklistsDTO findById(Long id) {
        Picklists Picklists = PicklistsRepository.findById(id).orElseGet(Picklists::new);
        ValidationUtil.isNull(Picklists.getId(),"Picklists","id",id);
        return PicklistsMapper.toDto(Picklists);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public PicklistsDTO create(Picklists resources) {
        return PicklistsMapper.toDto(PicklistsRepository.saveAndFlush(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public PicklistsDTO update(Long id,Picklists resources) {
        Picklists Picklists = PicklistsRepository.findById(id).orElseGet(Picklists::new);
        ValidationUtil.isNull( Picklists.getId(),"Picklists","id",resources.getId());
        Picklists.copy(resources);
		return PicklistsMapper.toDto(PicklistsRepository.saveAndFlush(Picklists));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PicklistsRepository.deleteById(id);
    }
	
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void makedelete(Long id) {
         Picklists Picklists = PicklistsRepository.findById(id).orElseGet(Picklists::new);
		 ValidationUtil.isNull( Picklists.getId(),"Picklists","id",id);
		 Picklists.setDeleted(1);
		 PicklistsRepository.save(Picklists);
    }


    @Override
    public void download(List<PicklistsDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PicklistsDTO Picklists : all) {
            Map<String,Object> map = new LinkedHashMap<>();
		    map.put("ID", Picklists.getId());
		    map.put("创建日期", Picklists.getPublished());
		    map.put("更新日期", Picklists.getUpdated());
		    map.put("创建者", Picklists.getAuthor());
		    map.put("删除标记", Picklists.getDeleted());
            map.put("SaaSId", Picklists.getSaasid());
            map.put("名称", Picklists.getName());
            map.put("可见", Picklists.getPresence());
            map.put("排序号", Picklists.getSequence());
            map.put("存储值", Picklists.getValue());
            map.put("显示值", Picklists.getDisplay());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}