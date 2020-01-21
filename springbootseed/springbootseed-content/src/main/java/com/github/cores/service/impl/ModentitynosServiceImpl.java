package com.github.cores.service.impl;

import com.github.cores.domain.Modentitynos;
import com.github.exception.BadRequestException;
import com.github.exception.EntityExistException;
import com.github.rabbitmq.repository.MqRepository;
import com.github.utils.*;
import com.github.cores.repository.ModentitynosRepository;
import com.github.cores.service.ModentitynosService;
import com.github.cores.service.dto.ModentitynosDTO;
import com.github.cores.service.dto.ModentitynosQueryCriteria;
import com.github.cores.service.mapper.ModentitynosMapper;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
@CacheConfig(cacheNames = "Modentitynos")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ModentitynosServiceImpl implements ModentitynosService {

    private final ModentitynosRepository ModentitynosRepository;

    private final MqRepository mqrepository;

    private final ModentitynosMapper ModentitynosMapper;

    public ModentitynosServiceImpl(ModentitynosRepository ModentitynosRepository, ModentitynosMapper ModentitynosMapper,MqRepository mqrepository) {
        this.ModentitynosRepository = ModentitynosRepository;
        this.ModentitynosMapper = ModentitynosMapper;
        this.mqrepository = mqrepository;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(ModentitynosQueryCriteria criteria, Pageable pageable){
        Page<Modentitynos> page = ModentitynosRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(ModentitynosMapper::toDto));
    }

    @Override
    @Cacheable
    public List<ModentitynosDTO> queryAll(ModentitynosQueryCriteria criteria){
        return ModentitynosMapper.toDto(ModentitynosRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ModentitynosDTO findById(Long id) {
        Modentitynos Modentitynos = ModentitynosRepository.findById(id).orElseGet(Modentitynos::new);
        ValidationUtil.isNull(Modentitynos.getId(),"Modentitynos","id",id);
        return ModentitynosMapper.toDto(Modentitynos);
    }

    @Override
    @Cacheable(key = "T(String).valueOf('tabid::').concat(#p0)")
    public ModentitynosDTO findByTabid(int tabid) {
        Modentitynos modentityno = ModentitynosRepository.findByTabid(tabid);
        if (modentityno == null) {
            throw new BadRequestException("编号不存在");
        }
        ValidationUtil.isNull(modentityno.getTabid(),"Modentitynos","tabid",tabid);
        return ModentitynosMapper.toDto(modentityno);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ModentitynosDTO create(Modentitynos resources) {
        return ModentitynosMapper.toDto(ModentitynosRepository.saveAndFlush(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ModentitynosDTO update(Long id,Modentitynos resources) {
        Modentitynos Modentitynos = ModentitynosRepository.findById(id).orElseGet(Modentitynos::new);
        ValidationUtil.isNull( Modentitynos.getId(),"Modentitynos","id",resources.getId());
        Modentitynos.copy(resources);
		return ModentitynosMapper.toDto(ModentitynosRepository.saveAndFlush(Modentitynos));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, int curid, String curdate) {
        Modentitynos Modentitynos = ModentitynosRepository.findById(id).orElseGet(Modentitynos::new);
        ValidationUtil.isNull( Modentitynos.getId(),"Modentitynos","id",id);
        Modentitynos.setCurId(curid);
        Modentitynos.setCurDate(curdate);
        Modentitynos.setUpdated(null);
        ModentitynosRepository.saveAndFlush(Modentitynos);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ModentitynosRepository.deleteById(id);
    }
	
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void makedelete(Long id) {
         Modentitynos Modentitynos = ModentitynosRepository.findById(id).orElseGet(Modentitynos::new);
		 ValidationUtil.isNull( Modentitynos.getId(),"Modentitynos","id",id);
		 Modentitynos.setDeleted(1);
		 ModentitynosRepository.save(Modentitynos);
    }


    @Override
    public void download(List<ModentitynosDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ModentitynosDTO Modentitynos : all) {
            Map<String,Object> map = new LinkedHashMap<>();
		    map.put("ID", Modentitynos.getId());
		    map.put("创建日期", Modentitynos.getPublished());
		    map.put("更新日期", Modentitynos.getUpdated());
		    map.put("创建者", Modentitynos.getAuthor());
		    map.put("删除标记", Modentitynos.getDeleted());
            map.put("云服务ID", Modentitynos.getSaasid());
            map.put("模块ID", Modentitynos.getTabid());
            map.put("编号前缀", Modentitynos.getPrefix());
            map.put("当前编号", Modentitynos.getCurId());
            map.put("起始编号", Modentitynos.getStartId());
            map.put("是否激活", Modentitynos.getActive());
            map.put("编号长度", Modentitynos.getLength());
            map.put("是否包含日期", Modentitynos.getIncludeDate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}