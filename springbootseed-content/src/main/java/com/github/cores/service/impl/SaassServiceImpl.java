package com.github.cores.service.impl;

import com.github.cores.domain.Saass;
import com.github.exception.EntityExistException;
import com.github.utils.ValidationUtil;
import com.github.utils.FileUtil;
import com.github.cores.repository.SaassRepository;
import com.github.cores.service.SaassService;
import com.github.cores.service.dto.SaassDTO;
import com.github.cores.service.dto.SaassQueryCriteria;
import com.github.cores.service.mapper.SaassMapper;
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
@CacheConfig(cacheNames = "Saass")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SaassServiceImpl implements SaassService {

    private final SaassRepository SaassRepository;

    private final SaassMapper SaassMapper;

    public SaassServiceImpl(SaassRepository SaassRepository, SaassMapper SaassMapper) {
        this.SaassRepository = SaassRepository;
        this.SaassMapper = SaassMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(SaassQueryCriteria criteria, Pageable pageable){
        Page<Saass> page = SaassRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(SaassMapper::toDto));
    }

    @Override
    @Cacheable
    public List<SaassDTO> queryAll(SaassQueryCriteria criteria){
        return SaassMapper.toDto(SaassRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public SaassDTO findById(Long id) {
        Saass Saass = SaassRepository.findById(id).orElseGet(Saass::new);
        ValidationUtil.isNull(Saass.getId(),"Saass","id",id);
        return SaassMapper.toDto(Saass);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public SaassDTO create(Saass resources) {
        return SaassMapper.toDto(SaassRepository.saveAndFlush(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public SaassDTO update(Long id,Saass resources) {
        Saass Saass = SaassRepository.findById(id).orElseGet(Saass::new);
        ValidationUtil.isNull( Saass.getId(),"Saass","id",resources.getId());
        Saass.copy(resources);
		return SaassMapper.toDto(SaassRepository.saveAndFlush(Saass));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SaassRepository.deleteById(id);
    }
	
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void makedelete(Long id) {
         Saass Saass = SaassRepository.findById(id).orElseGet(Saass::new);
		 ValidationUtil.isNull( Saass.getId(),"Saass","id",id);
		 Saass.setDeleted(1);
		 SaassRepository.save(Saass);
    }


    @Override
    public void download(List<SaassDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SaassDTO Saass : all) {
            Map<String,Object> map = new LinkedHashMap<>();
		    map.put("ID", Saass.getId());
		    map.put("创建日期", Saass.getPublished());
		    map.put("更新日期", Saass.getUpdated());
		    map.put("创建者", Saass.getAuthor());
		    map.put("删除标记", Saass.getDeleted());
            map.put("名称", Saass.getName());
            map.put("公司名称", Saass.getCompanyname());
            map.put("短名称", Saass.getShortname());
            map.put("省份", Saass.getProvince());
            map.put("城市", Saass.getCity());
            map.put("创建人", Saass.getProfileid());
            map.put("联系人", Saass.getContact());
            map.put("联系电话", Saass.getMobile());
            map.put("审批状态", Saass.getApprovalstatus());
            map.put("审批人", Saass.getApprover());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}