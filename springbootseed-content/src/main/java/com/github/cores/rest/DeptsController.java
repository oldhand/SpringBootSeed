package com.github.cores.rest;

import com.github.utils.ContentUtils;
import com.github.aop.log.Log;
import com.github.cores.domain.Depts;
import com.github.cores.service.DeptsService;
import com.github.cores.service.dto.DeptsQueryCriteria;
import com.github.exception.BadRequestException;
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
@Api(tags = "后台：部门管理")
@RestController
@RequestMapping("/api/depts")
public class DeptsController {

    private final DeptsService DeptsService;

    public DeptsController(DeptsService DeptsService) {
        this.DeptsService = DeptsService;
    }
	
    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateTimeUtils.timestampRequestDataBinder(binder);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, DeptsQueryCriteria criteria) throws IOException {
        DeptsService.download(DeptsService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询部门")
    @ApiOperation("查询部门")
        public ResponseEntity getDeptss(DeptsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(DeptsService.queryAll(criteria,pageable),HttpStatus.OK);
    }
	
    @GetMapping(value = "/load/{id}")
    @Log("装载部门")
    @ApiOperation("装载部门")
        public ResponseEntity load(@PathVariable Long id){
        return new ResponseEntity(DeptsService.findById(id),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增部门")
    @ApiOperation("新增部门")
        public ResponseEntity create(@Validated @RequestBody Depts resources, HttpServletRequest request){
	    if (!AuthorizationUtils.isLogin(request)) {
	        throw new BadRequestException("请先进行登录操作");
	    }
	 	String profileid = AuthorizationUtils.getProfileid(request);
	 	resources.setAuthor(profileid);
	 	resources.setId(ContentUtils.makeContentId("base_depts"));
        return new ResponseEntity<>(DeptsService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @Log("修改部门")
    @ApiOperation("修改部门")
        public ResponseEntity update(@PathVariable Long id,@Validated @RequestBody Depts resources){
        return new ResponseEntity(DeptsService.update(id,resources),HttpStatus.OK);
    }
	
    @DeleteMapping(value = "/{id}")
    @Log("逻辑删除部门")
    @ApiOperation("逻辑删除部门")
        public ResponseEntity delete(@PathVariable Long id){
        DeptsService.makedelete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Log("物理删除部门")
    @ApiOperation("物理删除部门")
        public ResponseEntity fulldelete(@PathVariable Long id){
        DeptsService.delete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }
}