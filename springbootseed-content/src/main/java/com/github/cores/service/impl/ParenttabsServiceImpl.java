package com.github.cores.service.impl;

import com.github.cores.domain.Parenttabs;
import com.github.exception.EntityExistException;
import com.github.utils.ValidationUtil;
import com.github.utils.FileUtil;
import com.github.cores.repository.ParenttabsRepository;
import com.github.cores.service.ParenttabsService;
import com.github.cores.service.dto.ParenttabsDTO;
import com.github.cores.service.dto.ParenttabsQueryCriteria;
import com.github.cores.service.mapper.ParenttabsMapper;
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
@CacheConfig(cacheNames = "Parenttabs")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ParenttabsServiceImpl implements ParenttabsService {

    private final ParenttabsRepository ParenttabsRepository;

    private final ParenttabsMapper ParenttabsMapper;

    public ParenttabsServiceImpl(ParenttabsRepository ParenttabsRepository, ParenttabsMapper ParenttabsMapper) {
        this.ParenttabsRepository = ParenttabsRepository;
        this.ParenttabsMapper = ParenttabsMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(ParenttabsQueryCriteria criteria, Pageable pageable){
        Page<Parenttabs> page = ParenttabsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(ParenttabsMapper::toDto));
    }

    @Override
    @Cacheable
    public List<ParenttabsDTO> queryAll(ParenttabsQueryCriteria criteria){
        return ParenttabsMapper.toDto(ParenttabsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ParenttabsDTO findById(Long id) {
        Parenttabs Parenttabs = ParenttabsRepository.findById(id).orElseGet(Parenttabs::new);
        ValidationUtil.isNull(Parenttabs.getId(),"Parenttabs","id",id);
        return ParenttabsMapper.toDto(Parenttabs);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ParenttabsDTO create(Parenttabs resources) {
        return ParenttabsMapper.toDto(ParenttabsRepository.saveAndFlush(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ParenttabsDTO update(Long id,Parenttabs resources) {
        Parenttabs Parenttabs = ParenttabsRepository.findById(id).orElseGet(Parenttabs::new);
        ValidationUtil.isNull( Parenttabs.getId(),"Parenttabs","id",resources.getId());
        Parenttabs.copy(resources);
		return ParenttabsMapper.toDto(ParenttabsRepository.saveAndFlush(Parenttabs));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ParenttabsRepository.deleteById(id);
    }
	
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void makedelete(Long id) {
         Parenttabs Parenttabs = ParenttabsRepository.findById(id).orElseGet(Parenttabs::new);
		 ValidationUtil.isNull( Parenttabs.getId(),"Parenttabs","id",id);
		 Parenttabs.setDeleted(1);
		 ParenttabsRepository.save(Parenttabs);
    }


    @Override
    public void download(List<ParenttabsDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ParenttabsDTO Parenttabs : all) {
            Map<String,Object> map = new LinkedHashMap<>();
		    map.put("ID", Parenttabs.getId());
		    map.put("创建日期", Parenttabs.getPublished());
		    map.put("更新日期", Parenttabs.getUpdated());
		    map.put("创建者", Parenttabs.getAuthor());
		    map.put("删除标记", Parenttabs.getDeleted());
            map.put("saasid",  Parenttabs.getSaasid());
            map.put("父模块名称", Parenttabs.getTabname());
            map.put("父模块标签", Parenttabs.getTablabel());
            map.put("是否可见", Parenttabs.getPresence());
            map.put("排序号", Parenttabs.getSquence());
            map.put("图标", Parenttabs.getIcon());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}