package com.github.sms.service.impl;

import com.github.sms.domain.Smslog;
import com.github.sms.repository.SmslogRepository;
import com.github.sms.service.SmslogService;
import com.github.sms.service.dto.SmslogDTO;
import com.github.sms.service.dto.SmslogQueryCriteria;
import com.github.sms.service.mapper.SmslogMapper;
import com.github.utils.FileUtil;
import com.github.utils.PageUtil;
import com.github.utils.QueryHelp;
import com.github.utils.ValidationUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
* @author oldhand
* @date 2020-01-21
*/
@Service
@CacheConfig(cacheNames = "smslog")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SmslogServiceImpl implements SmslogService {

    private final SmslogRepository smslogRepository;

    private final SmslogMapper smslogMapper;

    public SmslogServiceImpl(SmslogRepository smslogRepository, SmslogMapper smslogMapper) {
        this.smslogRepository = smslogRepository;
        this.smslogMapper = smslogMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(SmslogQueryCriteria criteria, Pageable pageable){
        Page<Smslog> page = smslogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(smslogMapper::toDto));
    }

    @Override
    @Cacheable
    public List<SmslogDTO> queryAll(SmslogQueryCriteria criteria){
        return smslogMapper.toDto(smslogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public SmslogDTO findById(Long id) {
        Smslog smslog = smslogRepository.findById(id).orElseGet(Smslog::new);
        ValidationUtil.isNull(smslog.getId(),"Smslog","id",id);
        return smslogMapper.toDto(smslog);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public SmslogDTO create(Smslog resources) {
        return smslogMapper.toDto(smslogRepository.saveAndFlush(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public SmslogDTO update(Long id,Smslog resources) {
        Smslog smslog = smslogRepository.findById(id).orElseGet(Smslog::new);
        ValidationUtil.isNull( smslog.getId(),"Smslog","id",resources.getId());
        smslog.copy(resources);
		return smslogMapper.toDto(smslogRepository.saveAndFlush(smslog));
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, int status,String result) {
        Smslog smslog = smslogRepository.findById(id).orElseGet(Smslog::new);
        ValidationUtil.isNull( smslog.getId(),"Smslog","id", id);
        smslog.setStatus(status);
        smslog.setResult(result);
        smslogRepository.saveAndFlush(smslog);
    }

    @Override
    @Cacheable(key = "T(String).valueOf('search::').concat(#p1).concat(#p0)")
    public Map<String, Object> search(String mobile, String regioncode) {
        Map<String,Object> result = new HashMap<>();
        Smslog smslog = new Smslog();
        smslog.setMobile(mobile);
        smslog.setRegioncode(regioncode);
        smslog.setStatus(1);
        Example<Smslog> smslogexample = Example.of(smslog);
        Pageable pageable =  PageRequest.of(0,1,  Sort.by(Sort.Direction.DESC,"id"));
        Page<Smslog> smslogs = smslogRepository.findAll(smslogexample,pageable);
        if (smslogs.getTotalElements() > 0) {
            Smslog sms = smslogs.getContent().get(0);
            Timestamp published = sms.getPublished();
            result.put("remain",published.getTime());
            result.put("uuid",sms.getUuid());
        }
        else {
            result.put("remain",0);
            result.put("uuid","");
        }
        return result;
    }


    @Override
    public void download(List<SmslogDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SmslogDTO smslog : all) {
            Map<String,Object> map = new LinkedHashMap<>();
		    map.put("ID", smslog.getId());
		    map.put("创建日期", smslog.getPublished());
		    map.put("发送者", smslog.getProfileid());
            map.put("云服务ID", smslog.getSaasid());
            map.put("手机号码", smslog.getMobile());
            map.put("国际区号", smslog.getRegioncode());
            map.put("验证码", smslog.getVerifycode());
            map.put("UUID", smslog.getUuid());
            map.put("状态", smslog.getStatus());
            map.put("返回值", smslog.getResult());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}