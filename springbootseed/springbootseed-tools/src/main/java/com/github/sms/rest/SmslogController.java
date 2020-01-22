package com.github.sms.rest;

import cn.hutool.core.util.IdUtil;
import com.github.aop.log.Log;
import com.github.domain.Authorization;
import com.github.exception.BadRequestException;
import com.github.sms.domain.SearchSms;
import com.github.sms.domain.SmsVerifyCode;
import com.github.sms.domain.Smslog;
import com.github.sms.service.SmslogService;
import com.github.sms.service.dto.SmslogDTO;
import com.github.sms.service.dto.SmslogQueryCriteria;
import com.github.sms.utils.SmsUtil;
import com.github.utils.AuthorizationUtils;
import com.github.utils.DateTimeUtils;
import com.github.utils.redisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
* @author oldhand
* @date 2020-01-21
*/
@Api(tags = "工具：短信验证码管理")
@RestController
@RequestMapping("/api/smslog")
public class SmslogController {

    private final SmslogService smslogService;

    @Value("${jwt.codeKey}")
    private String codeKey;


    public SmslogController(SmslogService smslogService) {
        this.smslogService = smslogService;
    }
	
    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateTimeUtils.timestampRequestDataBinder(binder);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, SmslogQueryCriteria criteria) throws IOException {
        smslogService.download(smslogService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询短信验证码")
    @ApiOperation("查询短信验证码")
        public ResponseEntity getSmslogs(SmslogQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(smslogService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping(value = "/send")
    @Log("发送短信验证码")
    @ApiOperation("发送短信验证码")
        public ResponseEntity create(@Validated @RequestBody Smslog resources, HttpServletRequest request){
	    if (AuthorizationUtils.isLogin(request)) {
            Authorization authorization = AuthorizationUtils.get(request);
            resources.setProfileid(authorization.getProfileid());
            resources.setSaasid(authorization.getSaasid());
	    }
	    else {
            resources.setProfileid("anonymous");
            resources.setSaasid(0L);
        }
        if (resources.getMobile().isEmpty()) {
            throw new BadRequestException("手机号码不能为空");
        }
        if (resources.getRegioncode().isEmpty()) {
            throw new BadRequestException("手机国际区号不能为空");
        }
        if (resources.getTemplate().isEmpty()) {
            throw new BadRequestException("模板不能为空");
        }

        Map<String,Object> info = smslogService.search(resources.getMobile(),resources.getRegioncode());
        long remain = Long.parseLong(info.get("remain").toString());

        if (remain > 0) {
            Timestamp currenttime = new Timestamp(System.currentTimeMillis());
            long count = (currenttime.getTime() - remain) / 1000;
            if (count <= 120) {
                throw new BadRequestException("剩余" + (120 - count) + "秒");
            }
        }

        String uuid = codeKey + "::" + IdUtil.simpleUUID();
        String verifycode = SmsUtil.make();
        resources.setUuid(uuid);
        resources.setResult("");
        resources.setStatus(0);
        resources.setVerifycode(verifycode);
        SmslogDTO smslogdto = smslogService.create(resources);
        try {
            String result = SmsUtil.sendSms(resources.getMobile(),resources.getRegioncode(),verifycode);
            redisUtils.set(uuid,verifycode);
            smslogService.update(smslogdto.getId(),1,result);
            smslogdto.setStatus(1);
            smslogdto.setResult(result);
            smslogdto.setVerifycode("******");
            return new ResponseEntity<>(smslogdto,HttpStatus.CREATED);
        }
        catch(Exception e) {
            smslogService.update(smslogdto.getId(),2,e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    @Log("校验短信验证码")
    @ApiOperation("校验短信验证码")
    @PostMapping(value = "/verify")
    public ResponseEntity verifyCode(@Validated @RequestBody SmsVerifyCode code){
        if (code.getUuid().isEmpty()) {
            throw new BadRequestException("UUID不能为空");
        }
        if (code.getVerifycode().isEmpty()) {
            throw new BadRequestException("短信验证码不能为空");
        }
        // 查询验证码
        String verifycode = redisUtils.get(code.getUuid());
        if (StringUtils.isBlank(verifycode)) {
            throw new BadRequestException("短信验证码已过期");
        }
        if (StringUtils.isBlank(code.getVerifycode()) || !code.getVerifycode().equalsIgnoreCase(verifycode)) {
            throw new BadRequestException("短信验证码错误");
        }
        redisUtils.delete(code.getUuid());
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @PostMapping(value = "/search")
    @Log("查询剩余时间")
    @ApiOperation("查询剩余时间")
    public ResponseEntity search(@Validated @RequestBody SearchSms resources, HttpServletRequest request){
        if (resources.getMobile().isEmpty()) {
            throw new BadRequestException("手机号码不能为空");
        }
        if (resources.getRegioncode().isEmpty()) {
            throw new BadRequestException("手机国际区号不能为空");
        }
        Map<String,Object> info = smslogService.search(resources.getMobile(),resources.getRegioncode());
        Map<String,Object> result = new HashMap<>();
        long remain = Long.parseLong(info.get("remain").toString());
        String uuid = info.get("uuid").toString();
        result.put("mobile",resources.getMobile());
        result.put("regioncode",resources.getRegioncode());
        if (remain == 0) {
            result.put("remain",0);
            result.put("uuid","");
        }
        else {
            Timestamp currenttime = new Timestamp(System.currentTimeMillis());
            long count = (currenttime.getTime() - remain) / 1000;
            if (count >= 120) {
                result.put("remain",0);
                result.put("uuid","");
            }
            else {
                result.put("remain",120 - count);
                result.put("uuid",uuid);
            }
        }
        return new ResponseEntity(result,HttpStatus.OK);
    }
}