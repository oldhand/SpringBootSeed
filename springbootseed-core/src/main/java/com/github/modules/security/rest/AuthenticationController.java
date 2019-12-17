package com.github.modules.security.rest;

import cn.hutool.core.util.IdUtil;
import com.wf.captcha.ArithmeticCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.github.annotation.AnonymousAccess;
import com.github.aop.log.Log;
import com.github.exception.BadRequestException;
import com.github.modules.monitor.service.RedisService;
import com.github.modules.security.security.AuthInfo;
import com.github.modules.security.security.ImgResult;
import com.github.modules.security.security.AuthApplication;
import com.github.modules.security.security.JwtAuthentication;
import com.github.modules.security.service.OnlineUserService;
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

    private final OnlineUserService onlineUserService;

    public AuthenticationController(JwtTokenUtil jwtTokenUtil, RedisService redisService, @Qualifier("jwtApplicationDetailsService") UserDetailsService userDetailsService, OnlineUserService onlineUserService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisService = redisService;
        this.userDetailsService = userDetailsService;
        this.onlineUserService = onlineUserService;
    }

    @Log("Token认证")
    @ApiOperation("Token认证")
    @AnonymousAccess
    @PostMapping(value = "/login")
    public ResponseEntity login(@Validated @RequestBody AuthApplication authApplication, HttpServletRequest request){
        if (authApplication.getTimestamp() == 0) {
            throw new BadRequestException("时间戳不能为空");
        }
        final JwtAuthentication jwtAuthentication = (JwtAuthentication) userDetailsService.loadUserByUsername(authApplication.getAppid());

        if(!jwtAuthentication.getPassword().equals(authApplication.getSecret())){
            throw new AccountExpiredException("密钥错误");
        }

        if(!jwtAuthentication.isEnabled()){
            throw new AccountExpiredException("账号已停用，请联系管理员");
        }
        // 生成令牌
        final String token = jwtTokenUtil.generateToken(jwtAuthentication);
        // 保存在线信息
        onlineUserService.save(jwtAuthentication, token, request);
        // 返回 token
        return ResponseEntity.ok(new AuthInfo(token));
    }


    @ApiOperation("获取验证码")
    @GetMapping(value = "/code")
    public ImgResult getCode(){
        // 算术类型 https://gitee.com/whvse/EasyCaptcha
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        // 几位数运算，默认是两位
        captcha.setLen(2);
        // 获取运算的结果：5
        String result = captcha.text();
        String uuid = codeKey + IdUtil.simpleUUID();
        redisService.saveCode(uuid,result);
        return new ImgResult(captcha.toBase64(),uuid);
    }

    @ApiOperation("注销")
    @DeleteMapping(value = "/logout")
    public ResponseEntity logout(HttpServletRequest request){
        onlineUserService.logout(jwtTokenUtil.getToken(request));
        return new ResponseEntity(HttpStatus.OK);
    }
}
