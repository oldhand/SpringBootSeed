package com.github.cores.service.impl;

import com.github.cores.domain.Modentityno;
import com.github.exception.EntityExistException;
import com.github.utils.ValidationUtil;
import com.github.utils.FileUtil;
import com.github.cores.repository.ModentitynoRepository;
import com.github.cores.service.ModentitynoService;
import com.github.cores.service.dto.ModentitynoDTO;
import com.github.cores.service.dto.ModentitynoQueryCriteria;
import com.github.cores.service.mapper.ModentitynoMapper;
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
@CacheConfig(cacheNames = "Modentityno")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ModentitynoServiceImpl implements ModentitynoService {

    private final ModentitynoRepository ModentitynoRepository;

    private final ModentitynoMapper ModentitynoMapper;

    public ModentitynoServiceImpl(ModentitynoRepository ModentitynoRepository, ModentitynoMapper ModentitynoMapper) {
        this.ModentitynoRepository = ModentitynoRepository;
        this.ModentitynoMapper = ModentitynoMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(ModentitynoQueryCriteria criteria, Pageable pageable){
        Page<Modentityno> page = ModentitynoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(ModentitynoMapper::toDto));
    }

    @Override
    @Cacheable
    public List<ModentitynoDTO> queryAll(ModentitynoQueryCriteria criteria){
        return ModentitynoMapper.toDto(ModentitynoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ModentitynoDTO findById(Long id) {
        Modentityno Modentityno = ModentitynoRepository.findById(id).orElseGet(Modentityno::new);
        ValidationUtil.isNull(Modentityno.getId(),"Modentityno","id",id);
        return ModentitynoMapper.toDto(Modentityno);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ModentitynoDTO create(Modentityno resources) {
        return ModentitynoMapper.toDto(ModentitynoRepository.saveAndFlush(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ModentitynoDTO update(Long id,Modentityno resources) {
        Modentityno Modentityno = ModentitynoRepository.findById(id).orElseGet(Modentityno::new);
        ValidationUtil.isNull( Modentityno.getId(),"Modentityno","id",resources.getId());
        Modentityno.copy(resources);
		return ModentitynoMapper.toDto(ModentitynoRepository.saveAndFlush(Modentityno));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ModentitynoRepository.deleteById(id);
    }
	
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void makedelete(Long id) {
         Modentityno Modentityno = ModentitynoRepository.findById(id).orElseGet(Modentityno::new);
		 ValidationUtil.isNull( Modentityno.getId(),"Modentityno","id",id);
		 Modentityno.setDeleted(1);
		 ModentitynoRepository.save(Modentityno);
    }


    @Override
    public void download(List<ModentitynoDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ModentitynoDTO Modentityno : all) {
            Map<String,Object> map = new LinkedHashMap<>();
		    map.put("ID", Modentityno.getId());
		    map.put("创建日期", Modentityno.getPublished());
		    map.put("更新日期", Modentityno.getUpdated());
		    map.put("创建者", Modentityno.getAuthor());
		    map.put("删除标记", Modentityno.getDeleted());
            map.put("模块ID", Modentityno.getTabid());
            map.put("编号前缀", Modentityno.getPrefix());
            map.put("当前编号", Modentityno.getCurId());
            map.put("起始编号", Modentityno.getStartId());
            map.put("是否激活", Modentityno.getActive());
            map.put("编号长度", Modentityno.getLength());
            map.put("是否包含日期", Modentityno.getIncludeDate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}