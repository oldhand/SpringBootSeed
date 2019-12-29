package com.github.modules.security.service;

import com.github.modules.security.security.JwtAuthentication;
import com.github.domain.Authorization;
import com.github.utils.EncryptUtils;
import com.github.utils.FileUtil;
import com.github.utils.PageUtil;
import com.github.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author oldhand
 * @Date 2019-12-16
 */
@Service
@SuppressWarnings({"unchecked","all"})
public class AuthorizationService {

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.online}")
    private String onlineKey;

    private final RedisTemplate redisTemplate;

    public AuthorizationService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(JwtAuthentication jwtAuthentication, String token, String publickey, String privatekey, HttpServletRequest request){
        String ip = StringUtils.getIp(request);
        String browser = StringUtils.getBrowser(request);
        String address = StringUtils.getCityInfo(ip);
        Authorization authorization = null;
        try {
            authorization = new Authorization("",jwtAuthentication.getUsername(), browser , ip, address, EncryptUtils.desEncrypt(token), new Date(),publickey,privatekey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        redisTemplate.opsForValue().set(onlineKey + token, authorization);
        redisTemplate.expire(onlineKey + token,expiration, TimeUnit.MILLISECONDS);
    }

    public Page<Authorization> getAll(String filter, Pageable pageable){
        List<Authorization> authorizations = getAll(filter);
        return new PageImpl<Authorization>(
                PageUtil.toPage(pageable.getPageNumber(),pageable.getPageSize(), authorizations),
                pageable,
                authorizations.size());
    }

    public List<Authorization> getAll(String filter){
        List<String> keys = new ArrayList<>(redisTemplate.keys(onlineKey + "*"));
        Collections.reverse(keys);
        List<Authorization> authorizations = new ArrayList<>();
        for (String key : keys) {
            Authorization authorization = (Authorization) redisTemplate.opsForValue().get(key);
            if(StringUtils.isNotBlank(filter)){
                if(authorization.toString().contains(filter)){
                    authorizations.add(authorization);
                }
            } else {
                authorizations.add(authorization);
            }
        }
        Collections.sort(authorizations, (o1, o2) -> {
            return o2.getLoginTime().compareTo(o1.getLoginTime());
        });
        return authorizations;
    }
    public Authorization get(String val){
        try {
            String key = onlineKey + val;
            Authorization authorization = (Authorization) redisTemplate.opsForValue().get(key);
            return authorization;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getPrivateKey(String val){
        Authorization authorization = this.get(val);
        if (authorization != null) {
            return authorization.getPrivatekey();
        }
        return "";
    }
    public String getPublickey(String val){
        Authorization authorization = this.get(val);
        if (authorization != null) {
            return authorization.getPublickey();
        }
        return "";
    }

    public void kickOut(String val) throws Exception {
        String key = onlineKey + EncryptUtils.desDecrypt(val);
        redisTemplate.delete(key);
    }

    public void logout(String token) {
        String key = onlineKey + token;
        redisTemplate.delete(key);
    }

    public void download(List<Authorization> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Authorization user : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户ID", user.getAppid());
            map.put("应用ID", user.getAppid());
            map.put("登录IP", user.getIp());
            map.put("登录地点", user.getAddress());
            map.put("浏览器", user.getBrowser());
            map.put("登录日期", user.getLoginTime());
            map.put("公钥", user.getPublickey());
            map.put("私钥", user.getPrivatekey());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
