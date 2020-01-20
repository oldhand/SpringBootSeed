package com.github.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * 密码相关
 * @author oldhand
 * @date 2019-12-16
 *
 * 一、需求：请输入6位以上密码,数字,字母,字符至少包含两种,不能包含中文和空格
 *
*/

@Component
public class PasswordUtils {

    @Getter
    private static String password;

    @Value("${springbootseed.password}")
    public void setPassword(String value) {
        password = value;
    }

    private static final String pw_pattern = "^(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^[^\\s\\u4e00-\\u9fa5]{6,20}$";


    /**
     * 密码加密
     */
    public static String encryptPassword(String str){
        String secret = DigestUtils.md5DigestAsHex(str.getBytes()) + password;
        return DigestUtils.md5DigestAsHex(secret.getBytes());
    }

    public static void match(String str) throws Exception {
        if (str.isEmpty())  throw new Exception("密码不能为空");
        if (str.length() < 6)  throw new Exception("密码长度不能小于6位");
        if (str.length() > 20)  throw new Exception("密码长度不能大于20位");
        if (Pattern.compile("[\u4e00-\u9fa5]").matcher(str).find()) throw new Exception("密码不能含有中文");
        if (!str.matches(pw_pattern))  throw new Exception("密码不符合规则");
    }
}
