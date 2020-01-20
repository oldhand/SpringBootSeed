package com.github.modules.rsa.annotation;

import com.github.modules.rsa.advice.EncryptRequestBodyAdvice;
import com.github.modules.rsa.advice.EncryptResponseBodyAdvice;
import com.github.modules.rsa.config.SecretKeyConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Author: oldhand
 * DateTime:2019/4/9 16:44
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({SecretKeyConfig.class,
        EncryptResponseBodyAdvice.class,
        EncryptRequestBodyAdvice.class})
public @interface EnableSecurity{

}
