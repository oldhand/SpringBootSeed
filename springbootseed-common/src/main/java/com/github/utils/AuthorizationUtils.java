package com.github.utils;

import com.github.domain.Authorization;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 认证相关
 * @author oldhand
 * @date 2019-12-16
 *
*/

@Component
public class AuthorizationUtils {

    @Getter
    private static String tokenHeader;

    @Getter
    private static Long expiration;

    @Getter
    private static String onlineKey;

    @Getter
    private static RedisTemplate redisTemplate;

    @Value("${jwt.expiration}")
    public void setExpiration(Long value) {
        expiration = value;
    }

    @Value("${jwt.online}")
    public void setOnlineKey(String value) {
        onlineKey = value;
    }

    @Value("${jwt.header}")
    public void setTokenHeader(String value) {
        tokenHeader = value;
    }


    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        AuthorizationUtils.redisTemplate = redisTemplate;
    }

    public static boolean isLogin(HttpServletRequest request) {
        if (getProfileid(request) != "") return true;
        return false;
    }
    public static String getProfileid(HttpServletRequest request) {
        try {
            String token =  getAccessToken(request);
            if (!token.isEmpty()) {
                String key = onlineKey + token;
                Authorization authorization = (Authorization) redisTemplate.opsForValue().get(key);
                if (authorization != null) {
                    return authorization.getProfileid();
                }
            }
        }
        catch(Exception e) {
        }
        return "";
    }

    public static boolean setProfileid(HttpServletRequest request, String profileid) {
        try {
            String token =  getAccessToken(request);
            if (!token.isEmpty()) {
                String key = onlineKey + token;
                Authorization authorization = (Authorization) redisTemplate.opsForValue().get(key);
                if (authorization != null) {
                    authorization.setProfileid(profileid);
                    redisTemplate.opsForValue().set(key, authorization);
                    redisTemplate.expire(key,expiration, TimeUnit.MILLISECONDS);
                    return true;
                }
            }
        }
        catch(Exception e) {

        }
        return false;
    }

    public static String getAccessToken(HttpServletRequest request){
        final String requestHeader = request.getHeader(tokenHeader);
        if (requestHeader != null) {
            return requestHeader;
        }
        return "";
    }

}
