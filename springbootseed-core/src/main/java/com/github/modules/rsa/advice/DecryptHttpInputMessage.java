package com.github.modules.rsa.advice;

import com.github.modules.config.GlobalConfig;
import com.github.modules.security.service.OnlineUserService;
import com.github.modules.utils.DESedeUtil;
import com.github.modules.utils.MD5Util;
import com.github.modules.utils.RSAUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Author:Bobby
 * DateTime:2019/4/9
 **/
public class DecryptHttpInputMessage implements HttpInputMessage {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private HttpHeaders headers;
    private InputStream body;


    public DecryptHttpInputMessage(HttpInputMessage inputMessage, OnlineUserService onlineUserService,String publicKey, String charset, boolean showLog) throws Exception {

        if (StringUtils.isEmpty(publicKey)) {
            throw new IllegalArgumentException("publicKey is null");
        }

        this.headers = inputMessage.getHeaders();
        String content = new BufferedReader(new InputStreamReader(inputMessage.getBody())).lines().collect(Collectors.joining(System.lineSeparator()));
        String decryptBody;
        if (content.startsWith("{")) {
            if (GlobalConfig.isDev()) {
                decryptBody = content;
            }
            else {
                log.info("Unencrypted without decryption:{}", content);
                throw new IllegalArgumentException("Unencrypted without decryption");
            }
        } else {
            final String accesstoken = getHeader("accesstoken");
            if (accesstoken.equals("")) {
                decryptBody = DESedeUtil.decrypt(content, MD5Util.get(RSAUtil.loadKey(publicKey)));
                if(showLog) {
                    log.info("Encrypted data received：{}", content);
                    log.info("After decryption：{}", decryptBody);
                }
            }
            else {
                decryptBody = DESedeUtil.decrypt(content, MD5Util.get(onlineUserService.getPublickey(accesstoken)));
                if(showLog) {
                    log.info("Encrypted data received：{}", content);
                    log.info("After decryption：{}", decryptBody);
                }
            }
        }
        this.body = new ByteArrayInputStream(decryptBody.getBytes());
    }

    @Override
    public InputStream getBody(){
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }

    public String getHeader(String key) {
        if (this.headers.containsKey(key)) {
            return this.headers.get(key).get(0);
        }
        return "";
    }
}
