package com.github.cores.service.impl;

import com.github.cores.domain.Suppliers;
import com.github.exception.EntityExistException;
import com.github.utils.ValidationUtil;
import com.github.utils.FileUtil;
import com.github.cores.repository.SuppliersRepository;
import com.github.cores.service.SuppliersService;
import com.github.cores.service.dto.SuppliersDTO;
import com.github.cores.service.dto.SuppliersQueryCriteria;
import com.github.cores.service.mapper.SuppliersMapper;
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
@CacheConfig(cacheNames = "Suppliers")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SuppliersServiceImpl implements SuppliersService {

    private final SuppliersRepository SuppliersRepository;

    private final SuppliersMapper SuppliersMapper;

    public SuppliersServiceImpl(SuppliersRepository SuppliersRepository, SuppliersMapper SuppliersMapper) {
        this.SuppliersRepository = SuppliersRepository;
        this.SuppliersMapper = SuppliersMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(SuppliersQueryCriteria criteria, Pageable pageable){
        Page<Suppliers> page = SuppliersRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(SuppliersMapper::toDto));
    }

    @Override
    @Cacheable
    public List<SuppliersDTO> queryAll(SuppliersQueryCriteria criteria){
        return SuppliersMapper.toDto(SuppliersRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public SuppliersDTO findById(Long id) {
        Suppliers Suppliers = SuppliersRepository.findById(id).orElseGet(Suppliers::new);
        ValidationUtil.isNull(Suppliers.getId(),"Suppliers","id",id);
        return SuppliersMapper.toDto(Suppliers);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public SuppliersDTO create(Suppliers resources) {
        return SuppliersMapper.toDto(SuppliersRepository.saveAndFlush(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public SuppliersDTO update(Long id,Suppliers resources) {
        Suppliers Suppliers = SuppliersRepository.findById(id).orElseGet(Suppliers::new);
        ValidationUtil.isNull( Suppliers.getId(),"Suppliers","id",resources.getId());
        Suppliers.copy(resources);
		return SuppliersMapper.toDto(SuppliersRepository.saveAndFlush(Suppliers));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SuppliersRepository.deleteById(id);
    }
	
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void makedelete(Long id) {
         Suppliers Suppliers = SuppliersRepository.findById(id).orElseGet(Suppliers::new);
		 ValidationUtil.isNull( Suppliers.getId(),"Suppliers","id",id);
		 Suppliers.setDeleted(1);
		 SuppliersRepository.save(Suppliers);
    }


    @Override
    public void download(List<SuppliersDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SuppliersDTO Suppliers : all) {
            Map<String,Object> map = new LinkedHashMap<>();
		    map.put("ID", Suppliers.getId());
		    map.put("创建日期", Suppliers.getPublished());
		    map.put("更新日期", Suppliers.getUpdated());
		    map.put("创建者", Suppliers.getAuthor());
		    map.put("删除标记", Suppliers.getDeleted());
            map.put("名称", Suppliers.getName());
            map.put("公司名称", Suppliers.getCompanyname());
            map.put("短名称", Suppliers.getShortname());
            map.put("省份", Suppliers.getProvince());
            map.put("城市", Suppliers.getCity());
            map.put("创建人", Suppliers.getProfileid());
            map.put("联系人", Suppliers.getContact());
            map.put("联系电话", Suppliers.getMobile());
            map.put("审批状态", Suppliers.getApprovalstatus());
            map.put("审批人", Suppliers.getApprover());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}