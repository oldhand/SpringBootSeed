package com.github.modules.security.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.io.Serializable;

/**
 * @author oldhand
 * @date 2019-12-16
 * 返回token
 */
@Getter
@AllArgsConstructor
public class AuthInfo implements Serializable {

    private final String token;

    private final JwtAuthentication user;
}
