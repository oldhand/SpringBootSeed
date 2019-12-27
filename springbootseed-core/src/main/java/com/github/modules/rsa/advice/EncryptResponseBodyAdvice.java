package com.github.modules.rsa.advice;

import com.github.modules.config.GlobalConfig;
import com.github.modules.monitor.service.RedisService;
import com.github.modules.rsa.config.SecretKeyConfig;
import com.github.modules.security.service.OnlineUserService;
import com.github.modules.utils.DESedeUtil;
import com.github.utils.MD5Util;
import com.github.modules.utils.RSAUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Author: oldhand
 * DateTime:2019/4/9
 **/
@ControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private boolean encrypt;

    private final OnlineUserService onlineUserService;

    @Autowired
    private SecretKeyConfig secretKeyConfig;

    private static ThreadLocal<Boolean> encryptLocal = new ThreadLocal<>();

    public EncryptResponseBodyAdvice(OnlineUserService onlineUserService) {
        this.onlineUserService = onlineUserService;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        encrypt = false;
        if (returnType.getMethod().isAnnotationPresent(ApiOperation.class) && secretKeyConfig.isOpen()) {
            encrypt = true;
        }
        return encrypt;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // EncryptResponseBodyAdvice.setEncryptStatus(false);
        // Dynamic Settings Not Encrypted
        Boolean status = encryptLocal.get();
        if (null != status && !status) {
            encryptLocal.remove();
            return body;
        }
        if (encrypt) {
            try {
                final String token = getHeader(request,"token");
                if (GlobalConfig.isDev() && token.equals("anonymous")) {
                    return body;
                }
                final String accesstoken = getHeader(request,"accesstoken");

                String publicKey;
                if (accesstoken != null && !accesstoken.isEmpty()) {
                    publicKey = onlineUserService.getPublickey(accesstoken);
                }
                else {
                    publicKey = RSAUtil.loadKey(secretKeyConfig.getPublicKey());
                }

                String content = JSON.toJSONString(body);

                if (!StringUtils.hasText(publicKey)) {
                    throw new NullPointerException("Please configure rsa-encrypt.publicKey parameter!");
                }
                String result = DESedeUtil.encrypt(content, MD5Util.get(publicKey));
                if(secretKeyConfig.isShowLog()) {
                    log.info("Encrypted content: {}", content);
                    log.info("After encryption：{}", result);
                }

                return result;
            } catch (Exception e) {
                log.error("Encrypted data exception", e);
            }
        }
        return body;
    }
    public String getHeader(ServerHttpRequest request,String key) {
        if (request.getHeaders().containsKey(key)) {
            if (request.getHeaders().get(key).size() > 0) {
                return request.getHeaders().get(key).get(0);
            }
        }
       return "";
    }
}
