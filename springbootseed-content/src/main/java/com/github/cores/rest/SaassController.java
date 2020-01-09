package com.github.cores.rest;

import com.github.aop.log.Log;
import com.github.cores.domain.Saass;
import com.github.cores.service.SaassService;
import com.github.cores.service.dto.SaassQueryCriteria;
import com.github.exception.BadRequestException;
import com.github.service.ContentIdsService;
import com.github.utils.AuthorizationUtils;
import com.github.utils.DateTimeUtils;
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
* @date 2020-01-03
*/
@Api(tags = "后台：Saas用户管理")
@RestController
@RequestMapping("/api/saass")
public class SaassController {

    private final SaassService SaassService;
	
	private final ContentIdsService ContentIdsService;

    public SaassController(SaassService SaassService,ContentIdsService ContentIdsService) {
        this.SaassService = SaassService;
		this.ContentIdsService = ContentIdsService;
    }
	
    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateTimeUtils.timestampRequestDataBinder(binder);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, SaassQueryCriteria criteria) throws IOException {
        SaassService.download(SaassService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询Saas用户")
    @ApiOperation("查询Saas用户")
        public ResponseEntity getSaasss(SaassQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(SaassService.queryAll(criteria,pageable),HttpStatus.OK);
    }
	
    @GetMapping(value = "/load/{id}")
    @Log("装载Saas用户")
    @ApiOperation("装载Saas用户")
        public ResponseEntity load(@PathVariable Long id){
        return new ResponseEntity(SaassService.findById(id),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Saas用户")
    @ApiOperation("新增Saas用户")
        public ResponseEntity create(@Validated @RequestBody Saass resources, HttpServletRequest request){
	    if (!AuthorizationUtils.isLogin(request)) {
	        throw new BadRequestException("请先进行登录操作");
	    }
	 	String profileid = AuthorizationUtils.getProfileid(request);
	 	resources.setAuthor(profileid);
	    long ContentID = ContentIdsService.create("base_saass");
	 	resources.setId(ContentID);
        return new ResponseEntity<>(SaassService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @Log("修改Saas用户")
    @ApiOperation("修改Saas用户")
        public ResponseEntity update(@PathVariable Long id,@Validated @RequestBody Saass resources){
        return new ResponseEntity(SaassService.update(id,resources),HttpStatus.OK);
    }
	
    @DeleteMapping(value = "/{id}")
    @Log("逻辑删除Saas用户")
    @ApiOperation("逻辑删除Saas用户")
        public ResponseEntity delete(@PathVariable Long id){
        SaassService.makedelete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Log("物理删除Saas用户")
    @ApiOperation("物理删除Saas用户")
        public ResponseEntity fulldelete(@PathVariable Long id){
        SaassService.delete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }
}