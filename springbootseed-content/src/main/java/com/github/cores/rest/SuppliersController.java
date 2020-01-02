package com.github.cores.rest;

import com.github.aop.log.Log;
import com.github.cores.domain.Suppliers;
import com.github.cores.service.SuppliersService;
import com.github.cores.service.dto.SuppliersQueryCriteria;
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
* @date 2020-01-02
*/
@Api(tags = "后台：SaaS用户管理")
@RestController
@RequestMapping("/api/suppliers")
public class SuppliersController {

    private final SuppliersService SuppliersService;
	
	private final ContentIdsService ContentIdsService;

    public SuppliersController(SuppliersService SuppliersService,ContentIdsService ContentIdsService) {
        this.SuppliersService = SuppliersService;
		this.ContentIdsService = ContentIdsService;
    }
	
    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateTimeUtils.timestampRequestDataBinder(binder);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, SuppliersQueryCriteria criteria) throws IOException {
        SuppliersService.download(SuppliersService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询SaaS用户")
    @ApiOperation("查询SaaS用户")
        public ResponseEntity getSupplierss(SuppliersQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(SuppliersService.queryAll(criteria,pageable),HttpStatus.OK);
    }
	
    @GetMapping(value = "/load/{id}")
    @Log("装载SaaS用户")
    @ApiOperation("装载SaaS用户")
        public ResponseEntity load(@PathVariable Long id){
        return new ResponseEntity(SuppliersService.findById(id),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增SaaS用户")
    @ApiOperation("新增SaaS用户")
        public ResponseEntity create(@Validated @RequestBody Suppliers resources, HttpServletRequest request){
	    if (!AuthorizationUtils.isLogin(request)) {
	        throw new BadRequestException("请先进行登录操作");
	    }
	 	String profileid = AuthorizationUtils.getProfileid(request);
	 	resources.setAuthor(profileid);
	    long ContentID = ContentIdsService.create("base_suppliers");
	 	resources.setId(ContentID);
        return new ResponseEntity<>(SuppliersService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @Log("修改SaaS用户")
    @ApiOperation("修改SaaS用户")
        public ResponseEntity update(@PathVariable Long id,@Validated @RequestBody Suppliers resources){
        return new ResponseEntity(SuppliersService.update(id,resources),HttpStatus.OK);
    }
	
    @DeleteMapping(value = "/{id}")
    @Log("逻辑删除SaaS用户")
    @ApiOperation("逻辑删除SaaS用户")
        public ResponseEntity delete(@PathVariable Long id){
        SuppliersService.makedelete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Log("物理删除SaaS用户")
    @ApiOperation("物理删除SaaS用户")
        public ResponseEntity fulldelete(@PathVariable Long id){
        SuppliersService.delete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }
}