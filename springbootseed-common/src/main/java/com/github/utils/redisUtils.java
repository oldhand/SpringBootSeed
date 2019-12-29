package com.github.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类,
 */
@Component
public class redisUtils {

    @Getter
    public static Long expiration;

    @Value("${loginCode.expiration}")
    public void setExpiration(Long value) {
        expiration = value;
    }

    private static RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        redisUtils.redisTemplate = redisTemplate;
    }
    /**
     * 获取验证码
     */
    public static String get(String key) {
        try {
            return Objects.requireNonNull(redisTemplate.opsForValue().get(key)).toString();
        }catch (Exception e){
            return "";
        }
    }
    /**
     * 保存验证码
     */
    public static void set(String key, Object val) {
        redisTemplate.opsForValue().set(key,val);
        redisTemplate.expire(key,expiration, TimeUnit.MINUTES);
    }

//    public List<RedisVo> findByKey(String key) {
//        List<RedisVo> redisVos = new ArrayList<>();
//        if(!"*".equals(key)){
//            key = "*" + key + "*";
//        }
//        Set<String> keys = redisTemplate.keys(key);
//        for (String s : keys) {
//            RedisVo redisVo = new RedisVo(s, Objects.requireNonNull(redisTemplate.opsForValue().get(s)).toString());
//            redisVos.add(redisVo);
//        }
//        return redisVos;
//    }
    /**
     * 保存指定的缓存
     */
    public static void delete(String key) {
        redisTemplate.delete(key);
    }

}