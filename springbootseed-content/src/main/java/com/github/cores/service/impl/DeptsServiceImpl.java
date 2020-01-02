package com.github.cores.service.impl;

import com.github.cores.domain.Depts;
import com.github.exception.EntityExistException;
import com.github.utils.ValidationUtil;
import com.github.utils.FileUtil;
import com.github.cores.repository.DeptsRepository;
import com.github.cores.service.DeptsService;
import com.github.cores.service.dto.DeptsDTO;
import com.github.cores.service.dto.DeptsQueryCriteria;
import com.github.cores.service.mapper.DeptsMapper;
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
@CacheConfig(cacheNames = "Depts")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeptsServiceImpl implements DeptsService {

    private final DeptsRepository DeptsRepository;

    private final DeptsMapper DeptsMapper;

    public DeptsServiceImpl(DeptsRepository DeptsRepository, DeptsMapper DeptsMapper) {
        this.DeptsRepository = DeptsRepository;
        this.DeptsMapper = DeptsMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(DeptsQueryCriteria criteria, Pageable pageable){
        Page<Depts> page = DeptsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(DeptsMapper::toDto));
    }

    @Override
    @Cacheable
    public List<DeptsDTO> queryAll(DeptsQueryCriteria criteria){
        return DeptsMapper.toDto(DeptsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public DeptsDTO findById(Long id) {
        Depts Depts = DeptsRepository.findById(id).orElseGet(Depts::new);
        ValidationUtil.isNull(Depts.getId(),"Depts","id",id);
        return DeptsMapper.toDto(Depts);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public DeptsDTO create(Depts resources) {
        return DeptsMapper.toDto(DeptsRepository.saveAndFlush(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public DeptsDTO update(Long id,Depts resources) {
        Depts Depts = DeptsRepository.findById(id).orElseGet(Depts::new);
        ValidationUtil.isNull( Depts.getId(),"Depts","id",resources.getId());
        Depts.copy(resources);
		return DeptsMapper.toDto(DeptsRepository.saveAndFlush(Depts));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DeptsRepository.deleteById(id);
    }
	
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void makedelete(Long id) {
         Depts Depts = DeptsRepository.findById(id).orElseGet(Depts::new);
		 ValidationUtil.isNull( Depts.getId(),"Depts","id",id);
		 Depts.setDeleted(1);
		 DeptsRepository.save(Depts);
    }


    @Override
    public void download(List<DeptsDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DeptsDTO Depts : all) {
            Map<String,Object> map = new LinkedHashMap<>();
		    map.put("ID", Depts.getId());
		    map.put("创建日期", Depts.getPublished());
		    map.put("更新日期", Depts.getUpdated());
		    map.put("创建者", Depts.getAuthor());
		    map.put("删除标记", Depts.getDeleted());
            map.put("部门名称", Depts.getDeptname());
            map.put("部门ID", Depts.getDeptid());
            map.put("父部门ID", Depts.getParentid());
            map.put("排序号", Depts.getSequence());
            map.put("部门级别", Depts.getDepth());
            map.put("SaaS", Depts.getSupplierid());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}