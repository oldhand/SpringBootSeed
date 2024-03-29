package com.github.profile.rest;

import cn.hutool.core.util.IdUtil;
import com.github.aop.log.Log;
import com.github.cores.service.UsersService;
import com.github.cores.service.dto.UsersDTO;
import com.github.domain.Authorization;
import com.github.exception.BadRequestException;
import com.github.profile.domain.*;
import com.github.profile.service.ProfileService;
import com.github.profile.service.dto.ProfileDTO;
import com.github.profile.service.dto.ProfileQueryCriteria;
import com.github.utils.*;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2019-12-27
*/
@Api(tags = "数据库中间件：用户管理")
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    private final UsersService usersservice;

    @Value("${jwt.codeKey}")
    private String codeKey;

    public ProfileController(ProfileService profileService,UsersService usersservice) {
        this.profileService = profileService;
        this.usersservice = usersservice;
    }

    @GetMapping(value = "/info")
    @Log("查询当前登录用户信息")
    @ApiOperation("查询当前登录用户信息")
    public ResponseEntity getInfo(HttpServletRequest request){
        if (!AuthorizationUtils.isLogin(request)) {
            throw new BadRequestException("请先进行登录操作");
        }

        Authorization authorization = AuthorizationUtils.get(request);

        String profileid = authorization.getProfileid();

        long saasid = authorization.getSaasid();

        if (StringUtils.isEmpty(profileid)) {
            throw new BadRequestException("请先进行登录操作");
        }
        Map<String, Object> info = profileService.info(saasid,profileid);
        return new ResponseEntity(info,HttpStatus.OK);
    }


    @Log("导出用户数据")
    @ApiOperation("导出用户数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, ProfileQueryCriteria criteria) throws IOException {
        profileService.download(profileService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询用户")
    @ApiOperation("查询用户")
        public ResponseEntity getProfiles(ProfileQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(profileService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    @Log("注册用户")
    @ApiOperation("注册用户")
        public ResponseEntity create(@Validated @RequestBody RegisterProfile resources){
        try {
            PasswordUtils.match(resources.getPassword());
        }catch(Exception e) {
            throw new BadRequestException("密码: " + e.getMessage());
        }
        ProfileDTO profile = profileService.create(resources);
        return new ResponseEntity<>(profile,HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    @Log("登录")
    @ApiOperation("登录")
    public ResponseEntity login(@Validated @RequestBody LoginProfile loginprofile, HttpServletRequest request){
        if (loginprofile.getId().isEmpty()) {
            throw new BadRequestException("用户ID不能为空");
        }
        try {
            PasswordUtils.match(loginprofile.getPassword());
        }catch(Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        String profileid = loginprofile.getId();

        // 查询上一次登录出错时间戳
        String key = "auth_login::" + MD5Util.get(profileid);
        String authlogin = redisUtils.get(key);

        if (!authlogin.isEmpty()) {
            if (loginprofile.getUuid().isEmpty()) {
                throw new BadRequestException("UUID不能为空");
            }
            if (loginprofile.getVerifycode().isEmpty()) {
                throw new BadRequestException("验证码不能为空");
            }
            // 查询验证码
            String verifycode = redisUtils.get(loginprofile.getUuid());
            if (StringUtils.isBlank(verifycode)) {
                throw new BadRequestException("验证码已过期");
            }
            if (StringUtils.isBlank(loginprofile.getVerifycode()) || !loginprofile.getVerifycode().equalsIgnoreCase(verifycode)) {
                throw new BadRequestException("验证码错误");
            }
        }

        final ProfileDTO profile = profileService.findById(profileid);

        if (!profile.getStatus()) {
            throw new AccountExpiredException("用户已经被禁用");
        }

        if(!profile.getPassword().equals(PasswordUtils.encryptPassword(loginprofile.getPassword()))){
            redisUtils.set(key, DateTimeUtils.gettimeStamp());
            throw new AccountExpiredException("密码错误");
        }
        final UsersDTO users = usersservice.findByProfileid(profileid);

        if (users != null) {
            Long saasid = users.getSaasid();
            if (!AuthorizationUtils.set(request,profileid,saasid)) {
                throw new AccountExpiredException("登录失败");
            }
        }
        else {
            if (!AuthorizationUtils.set(request,profileid,0)) {
                throw new AccountExpiredException("登录失败");
            }
        }
        if (!authlogin.isEmpty()) {
            redisUtils.delete(key);
        }
        if (!loginprofile.getUuid().isEmpty()) {
            // 清除验证码
            redisUtils.delete(loginprofile.getUuid());
        }
        return new ResponseEntity(profile,HttpStatus.OK);
    }

    @PostMapping(value = "/logout")
    @Log("注销")
    @ApiOperation("注销")
    public ResponseEntity logout(HttpServletRequest request){
        if (!AuthorizationUtils.set(request,"",0)) {
            throw new AccountExpiredException("注销失败");
        }
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @PutMapping
    @Log("修改用户")
    @ApiOperation("修改用户")
        public ResponseEntity update(@Validated @RequestBody UpdateProfile resources){
        profileService.update(resources);
        ProfileDTO profile = profileService.findByUsername(resources.getUsername());
        return new ResponseEntity(profile,HttpStatus.OK);
    }
    @PutMapping(value = "/modifypassword")
    @Log("修改密码")
    @ApiOperation("修改密码")
    public ResponseEntity modifypassword(@Validated @RequestBody ChangePassword profile){

        if (profile.getProfileid().isEmpty()) {
            throw new BadRequestException("用户ID不能为空");
        }
        try {
            PasswordUtils.match(profile.getPassword());
        }catch(Exception e) {
            throw new BadRequestException("新密码: " + e.getMessage());
        }

        String profileid = profile.getProfileid();

        // 查询上一次登录出错时间戳

        if (profile.getUuid().isEmpty()) {
            throw new BadRequestException("UUID不能为空");
        }
        if (profile.getVerifycode().isEmpty()) {
            throw new BadRequestException("验证码不能为空");
        }
        // 查询验证码
        String verifycode = redisUtils.get(profile.getUuid());
        if (StringUtils.isBlank(verifycode)) {
            throw new BadRequestException("验证码已过期");
        }
        if (StringUtils.isBlank(profile.getVerifycode()) || !profile.getVerifycode().equalsIgnoreCase(verifycode)) {
            throw new BadRequestException("验证码错误");
        }

        final ProfileDTO profiledto = profileService.findById(profileid);

        if (!profiledto.getStatus()) {
            throw new AccountExpiredException("用户已经被禁用");
        }

        profileService.changePassword(profileid,profile.getPassword());
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @PostMapping(value = "/disable/{id}")
    @Log("禁止用户")
    @ApiOperation("禁止用户")
    public ResponseEntity disable(@PathVariable String id){
        profileService.disable(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @PostMapping(value = "/enable/{id}")
    @Log("启用用户")
    @ApiOperation("启用用户")
        public ResponseEntity enable(@PathVariable String id){
        profileService.enable(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @Log("获取验证码")
    @ApiOperation("获取验证码")
    @GetMapping(value = "/verifycode")
    public ImgResult getCode(){
        // 算术类型 https://gitee.com/whvse/EasyCaptcha
        SpecCaptcha captcha = new SpecCaptcha(111, 36, 4);
        // 设置类型，纯数字、纯字母、字母数字混合
        captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        String result = captcha.text();
        String uuid = codeKey + "::" + IdUtil.simpleUUID();
        redisUtils.set(uuid,result);
        return new ImgResult(captcha.toBase64(),uuid);
    }

    @Log("校验验证码")
    @ApiOperation("校验验证码")
    @PostMapping(value = "/verifycode")
    public ResponseEntity verifyCode(@Validated @RequestBody VerifyCode code){
        if (code.getUuid().isEmpty()) {
            throw new BadRequestException("UUID不能为空");
        }
        if (code.getVerifycode().isEmpty()) {
            throw new BadRequestException("验证码不能为空");
        }
        // 查询验证码
        String verifycode = redisUtils.get(code.getUuid());
        if (StringUtils.isBlank(verifycode)) {
            throw new BadRequestException("验证码已过期");
        }
        if (StringUtils.isBlank(code.getVerifycode()) || !code.getVerifycode().equalsIgnoreCase(verifycode)) {
            throw new BadRequestException("验证码错误");
        }
        redisUtils.delete(code.getUuid());
        return new ResponseEntity("ok",HttpStatus.OK);
    }
}