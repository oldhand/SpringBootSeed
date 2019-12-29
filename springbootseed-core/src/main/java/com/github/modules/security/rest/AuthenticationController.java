package com.github.modules.security.rest;

import com.github.modules.config.GlobalConfig;
import com.github.modules.security.security.*;
import com.github.utils.MD5Util;
import com.github.modules.utils.RSAUtil;
import com.github.utils.TimeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.github.annotation.AnonymousAccess;
import com.github.aop.log.Log;
import com.github.exception.BadRequestException;
import com.github.modules.monitor.service.RedisService;
import com.github.modules.security.service.AuthorizationService;
import com.github.modules.security.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author oldhand
 * @date 2019-12-16
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Api(tags = "系统：系统授权接口")
public class AuthenticationController {

    @Value("${jwt.codeKey}")
    private String codeKey;

    private final JwtTokenUtil jwtTokenUtil;

    private final RedisService redisService;

    private final UserDetailsService userDetailsService;

    private final AuthorizationService authorizationService;

    public AuthenticationController(JwtTokenUtil jwtTokenUtil, RedisService redisService, @Qualifier("jwtApplicationDetailsService") UserDetailsService userDetailsService, AuthorizationService authorizationService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisService = redisService;
        this.userDetailsService = userDetailsService;
        this.authorizationService = authorizationService;
    }

    @Log("Token认证")
    @ApiOperation("Token认证")
    @AnonymousAccess
    @PostMapping(value = "/credential")
    public ResponseEntity login(@Validated @RequestBody AuthApplication authApplication, HttpServletRequest request){
        if (authApplication.getAppid().isEmpty()) {
            throw new BadRequestException("应用ID不能为空");
        }
        if (authApplication.getSecret().isEmpty()) {
            throw new BadRequestException("密钥不能为空");
        }
        if (authApplication.getTimestamp() == 0) {
            throw new BadRequestException("时间戳不能为空");
        }
        final JwtAuthentication jwtAuthentication = (JwtAuthentication) userDetailsService.loadUserByUsername(authApplication.getAppid());

        if (!GlobalConfig.isDev()) {
            String md5_secret = MD5Util.get(authApplication.getAppid()+authApplication.getTimestamp()+jwtAuthentication.getPassword());

            if(!md5_secret.equals(authApplication.getSecret())){
                throw new AccountExpiredException("密钥错误");
            }
        }

        if(!jwtAuthentication.isEnabled()){
            throw new AccountExpiredException("账号已停用，请联系管理员");
        }
        try {
            // 生成令牌
            final String token = jwtTokenUtil.generateToken(jwtAuthentication);

            Map<String, String> keys = RSAUtil.initKey();

            long timestamp = TimeUtils.gettimeStamp();
            // 保存在线信息
            authorizationService.save(jwtAuthentication, token, keys.get("publickey"), keys.get("privatekey"), request);
            // 返回 token
            return ResponseEntity.ok(new AuthInfo(token,"",keys.get("publickey")));
        }
        catch (Exception e) {
            throw new AccountExpiredException("生成token错误");
        }
    }



    @Log("刷新Token")
    @ApiOperation("刷新Token")
    @PostMapping(value = "/flush")
    public ResponseEntity flush(@Validated @RequestBody AuthApplication authApplication, HttpServletRequest request){
        authorizationService.logout(jwtTokenUtil.getAccessToken(request));
        return new ResponseEntity(HttpStatus.OK);
    }
}
