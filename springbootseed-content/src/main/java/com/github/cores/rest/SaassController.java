package com.github.cores.rest;

import com.github.utils.ContentUtils;
import com.github.aop.log.Log;
import com.github.cores.domain.Saass;
import com.github.cores.service.SaassService;
import com.github.cores.service.dto.SaassDTO;
import com.github.cores.service.dto.SaassQueryCriteria;
import com.github.exception.BadRequestException;
import com.github.service.ContentIdsService;
import com.github.utils.AuthorizationUtils;
import com.github.utils.DateTimeUtils;
import com.github.utils.MqUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
* @author oldhand
* @date 2020-01-14
*/
@Api(tags = "后台：云服务管理")
@RestController
@RequestMapping("/api/saass")
public class SaassController {

    private final SaassService SaassService;

    public SaassController(SaassService SaassService) {
        this.SaassService = SaassService;
    }
	
    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateTimeUtils.timestampRequestDataBinder(binder);
    }

    @PostMapping(value = "/initdata/{id}")
    @Log("初始化云服务数据")
    @ApiOperation("初始化云服务数据")
    public ResponseEntity initdata(@PathVariable Long id,String secret,HttpServletRequest request) {
        if (!secret.equals("springbootseed")) {
            throw new BadRequestException("密钥错误");
        }
        if (!AuthorizationUtils.isLogin(request)) {
            throw new BadRequestException("请先进行登录操作");
        }
        String author = AuthorizationUtils.getProfileid(request);
        String entityno;
        try {
            entityno = MqUtils.makeModEntityNo("Users");
        }
        catch(Exception e) {
            throw new BadRequestException("生成用户编号失败");
        }
        SaassService.initdata(author, id, entityno);
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, SaassQueryCriteria criteria) throws IOException {
        SaassService.download(SaassService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询云服务")
    @ApiOperation("查询云服务")
        public ResponseEntity getSaasss(SaassQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(SaassService.queryAll(criteria,pageable),HttpStatus.OK);
    }
	
    @GetMapping(value = "/load/{id}")
    @Log("装载云服务")
    @ApiOperation("装载云服务")
        public ResponseEntity load(@PathVariable Long id){
        return new ResponseEntity(SaassService.findById(id),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增云服务")
    @ApiOperation("新增云服务")
        public ResponseEntity create(@Validated @RequestBody Saass resources, HttpServletRequest request){
	    if (!AuthorizationUtils.isLogin(request)) {
	        throw new BadRequestException("请先进行登录操作");
	    }
	 	String profileid = AuthorizationUtils.getProfileid(request);
	 	resources.setAuthor(profileid);
	 	resources.setId(ContentUtils.makeContentId("base_saass"));
        return new ResponseEntity<>(SaassService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @Log("修改云服务")
    @ApiOperation("修改云服务")
        public ResponseEntity update(@PathVariable Long id,@Validated @RequestBody Saass resources){
        return new ResponseEntity(SaassService.update(id,resources),HttpStatus.OK);
    }
	
    @DeleteMapping(value = "/{id}")
    @Log("逻辑删除云服务")
    @ApiOperation("逻辑删除云服务")
        public ResponseEntity delete(@PathVariable Long id){
        SaassService.makedelete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Log("物理删除云服务")
    @ApiOperation("物理删除云服务")
        public ResponseEntity fulldelete(@PathVariable Long id){
        SaassService.delete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }
}