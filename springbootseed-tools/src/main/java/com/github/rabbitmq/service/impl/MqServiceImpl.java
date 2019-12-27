package com.github.rabbitmq.service.impl;

import com.github.rabbitmq.domain.Mq;
import com.github.exception.EntityExistException;
import com.github.rabbitmq.domain.MqMessage;
import com.github.utils.*;
import com.github.rabbitmq.repository.MqRepository;
import com.github.rabbitmq.service.MqService;
import com.github.rabbitmq.service.dto.MqDTO;
import com.github.rabbitmq.service.dto.MqQueryCriteria;
import com.github.rabbitmq.service.mapper.MqMapper;
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

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author oldhand
* @date 2019-12-25
*/
@Service
@CacheConfig(cacheNames = "mq")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MqServiceImpl implements MqService {

    private final MqRepository mqRepository;

    private final MqMapper mqMapper;

    public MqServiceImpl(MqRepository mqRepository, MqMapper mqMapper) {
        this.mqRepository = mqRepository;
        this.mqMapper = mqMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(MqQueryCriteria criteria, Pageable pageable){
        Page<Mq> page = mqRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(mqMapper::toDto));
    }

    @Override
    @Cacheable
    public List<MqDTO> queryAll(MqQueryCriteria criteria){
        return mqMapper.toDto(mqRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public MqDTO findById(Long id) {
        Mq mq = mqRepository.findById(id).orElseGet(Mq::new);
        ValidationUtil.isNull(mq.getId(),"Mq","id",id);
        return mqMapper.toDto(mq);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public MqDTO create(MqMessage mqMessage) {
        Mq resources = new Mq();
        resources.setUniquekey(mqMessage.getUniquekey());
        resources.setName(mqMessage.getName());
        resources.setMessage(mqMessage.getMessage());
        resources.setIslock(mqMessage.getIslock());
        resources.setIsasync(mqMessage.getIsasync());
        resources.setAck(0);
        resources.setResult("");
        if (mqMessage.getUniquekey().isEmpty()) {
            resources.setUniquekey(MD5Util.get(IdUtil.simpleUUID()));
        }
        else {
            resources.setUniquekey(mqMessage.getUniquekey());
        }
        if(mqRepository.findByUniquekey(resources.getUniquekey()) != null){
            throw new EntityExistException(Mq.class,"uniquekey",resources.getUniquekey());
        }
        return mqMapper.toDto(mqRepository.save(resources));
    }



    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void updateAck(Long id,String ack,String result) throws Exception {
        Mq mq = mqRepository.findById(id).orElseGet(Mq::new);
        ValidationUtil.isNull( mq.getId(),"Mq","id",id);
        mq.setAcktime(new Timestamp(System.currentTimeMillis()));
        mq.setAck(Integer.parseInt(ack));
        mq.setResult(result);
        mqRepository.save(mq);
    }



    @Override
    public void download(List<MqDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MqDTO mq : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("创建日期", mq.getPublished());
            map.put("队列类型", mq.getName());
            map.put("消息体", mq.getMessage());
            map.put("响应代码", mq.getAck());
            map.put("响应时间", mq.getAcktime());
            map.put("唯一键", mq.getUniquekey());
            map.put("是否锁定", mq.getIslock());
            map.put("是否同步", mq.getIsasync());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}