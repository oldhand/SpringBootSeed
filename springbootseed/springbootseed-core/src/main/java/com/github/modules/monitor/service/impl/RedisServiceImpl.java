package com.github.modules.monitor.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.github.modules.monitor.domain.vo.RedisVo;
import com.github.modules.monitor.service.RedisService;
import com.github.utils.FileUtil;
import com.github.utils.PageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Service
@SuppressWarnings({"unchecked","all"})
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate redisTemplate;

    @Value("${loginCode.expiration}")
    private Long expiration;

    @Value("${jwt.online}")
    private String onlineKey;

    @Value("${jwt.codeKey}")
    private String codeKey;

    public RedisServiceImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Page<RedisVo> findByKey(String key, Pageable pageable){
        List<RedisVo> redisVos = findByKey(key);
        return new PageImpl<RedisVo>(
                PageUtil.toPage(pageable.getPageNumber(),pageable.getPageSize(),redisVos),
                pageable,
                redisVos.size());
    }

    @Override
    public List<RedisVo> findByKey(String key) {
        List<RedisVo> redisVos = new ArrayList<>();
        if(!"*".equals(key)){
            key = "*" + key + "*";
        }
        Set<String> keys = redisTemplate.keys(key);
        for (String s : keys) {
            RedisVo redisVo = new RedisVo(s, Objects.requireNonNull(redisTemplate.opsForValue().get(s)).toString());
            redisVos.add(redisVo);
        }
        return redisVos;
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void deleteTag(String keys) {
        if (keys.isEmpty()) return;
        keys = keys.replace(",",";");
        String[] tags = keys.split(";");
        for(String tag : tags){
            Set<String> rediskeys = redisTemplate.keys(tag + "::*");
            if (rediskeys.size() > 0) {
                redisTemplate.delete(rediskeys.stream().collect(Collectors.toList()));
            }
        }
    }

    @Override
    public void deleteAll() {
        Set<String> keys = redisTemplate.keys(  "*");
        redisTemplate.delete(keys.stream().filter(s -> !s.contains(onlineKey)).filter(s -> !s.contains(codeKey)).collect(Collectors.toList()));
    }

    @Override
    public String getCodeVal(String key) {
        try {
            return Objects.requireNonNull(redisTemplate.opsForValue().get(key)).toString();
        }catch (Exception e){
            return "";
        }
    }

    @Override
    public void saveCode(String key, Object val) {
        redisTemplate.opsForValue().set(key,val);
        redisTemplate.expire(key,expiration, TimeUnit.MINUTES);
    }

    @Override
    public void download(List<RedisVo> redisVos, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RedisVo redisVo : redisVos) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("key", redisVo.getKey());
            map.put("value", redisVo.getValue());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
