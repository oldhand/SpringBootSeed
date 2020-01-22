package com.github.modules.tokens.rest;

import cn.hutool.core.util.IdUtil;
import com.github.aop.log.Log;
import com.github.domain.Authorization;
import com.github.exception.BadRequestException;
import com.github.modules.tokens.domain.Token;
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
@Api(tags = "工具：令牌管理")
@RestController
@RequestMapping("/api/token")
public class TokenController {

    @Value("${jwt.codeKey}")
    private String codeKey;

    public TokenController() {}

    @GetMapping(value = "/{uuid}")
    @Log("查询令牌")
    @ApiOperation("查询令牌")
        public ResponseEntity getToken(@PathVariable String uuid){
        // 查询令牌
        String key = codeKey + "::" + uuid;
        String token = redisUtils.get(key);
        if (StringUtils.isBlank(token)) {
            throw new BadRequestException("令牌已过期");
        }
        redisUtils.delete(key);
        Map<String,Object> result = new HashMap<>();
        result.put("token",token);
        return new ResponseEntity(result,HttpStatus.OK);
    }


    @PostMapping
    @Log("生成令牌")
    @ApiOperation("生成令牌")
        public ResponseEntity createToken(@Validated @RequestBody Token resources){
        if (resources.getToken().isEmpty()) {
            throw new BadRequestException("token不能为空");
        }
        if (resources.getExpire() == 0) {
            throw new BadRequestException("必须设置过期时间");
        }
        String uuid = IdUtil.simpleUUID();
        String key = codeKey + "::" + uuid;
        int expire =  resources.getExpire();
        redisUtils.set(key,resources.getToken(),expire);
        Map<String,Object> result = new HashMap<>();
        result.put("uuid",uuid);
        return new ResponseEntity(result,HttpStatus.OK);
    }


}