package com.github.cores.service.impl;

import com.github.cores.domain.Tabs;
import com.github.exception.EntityExistException;
import com.github.utils.ValidationUtil;
import com.github.utils.FileUtil;
import com.github.cores.repository.TabsRepository;
import com.github.cores.service.TabsService;
import com.github.cores.service.dto.TabsDTO;
import com.github.cores.service.dto.TabsQueryCriteria;
import com.github.cores.service.mapper.TabsMapper;
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
@CacheConfig(cacheNames = "Tabs")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TabsServiceImpl implements TabsService {

    private final TabsRepository TabsRepository;

    private final TabsMapper TabsMapper;

    public TabsServiceImpl(TabsRepository TabsRepository, TabsMapper TabsMapper) {
        this.TabsRepository = TabsRepository;
        this.TabsMapper = TabsMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(TabsQueryCriteria criteria, Pageable pageable){
        Page<Tabs> page = TabsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(TabsMapper::toDto));
    }

    @Override
    @Cacheable
    public List<TabsDTO> queryAll(TabsQueryCriteria criteria){
        return TabsMapper.toDto(TabsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public TabsDTO findById(Long id) {
        Tabs Tabs = TabsRepository.findById(id).orElseGet(Tabs::new);
        ValidationUtil.isNull(Tabs.getId(),"Tabs","id",id);
        return TabsMapper.toDto(Tabs);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public TabsDTO create(Tabs resources) {
        return TabsMapper.toDto(TabsRepository.saveAndFlush(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public TabsDTO update(Long id,Tabs resources) {
        Tabs Tabs = TabsRepository.findById(id).orElseGet(Tabs::new);
        ValidationUtil.isNull( Tabs.getId(),"Tabs","id",resources.getId());
        Tabs.copy(resources);
		return TabsMapper.toDto(TabsRepository.saveAndFlush(Tabs));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TabsRepository.deleteById(id);
    }
	
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void makedelete(Long id) {
         Tabs Tabs = TabsRepository.findById(id).orElseGet(Tabs::new);
		 ValidationUtil.isNull( Tabs.getId(),"Tabs","id",id);
		 Tabs.setDeleted(1);
		 TabsRepository.save(Tabs);
    }


    @Override
    public void download(List<TabsDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TabsDTO Tabs : all) {
            Map<String,Object> map = new LinkedHashMap<>();
		    map.put("ID", Tabs.getId());
		    map.put("创建日期", Tabs.getPublished());
		    map.put("更新日期", Tabs.getUpdated());
		    map.put("创建者", Tabs.getAuthor());
		    map.put("删除标记", Tabs.getDeleted());
            map.put("saasid",  Tabs.getSaasid());
            map.put("模块名称", Tabs.getTabname());
            map.put("模块标签", Tabs.getTablabel());
            map.put("排序号", Tabs.getSequence());
            map.put("模块ID", Tabs.getTabid());
            map.put("图标", Tabs.getIcon());
            map.put("存储类型", Tabs.getDatatype());
            map.put("是否可见", Tabs.getPresence());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}