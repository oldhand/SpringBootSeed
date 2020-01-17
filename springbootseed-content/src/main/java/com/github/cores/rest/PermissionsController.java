package com.github.cores.rest;

import com.github.utils.ContentUtils;
import com.github.aop.log.Log;
import com.github.cores.domain.Permissions;
import com.github.cores.service.PermissionsService;
import com.github.cores.service.dto.PermissionsQueryCriteria;
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
@Api(tags = "后台：权限管理")
@RestController
@RequestMapping("/api/permissions")
public class PermissionsController {

    private final PermissionsService PermissionsService;

    public PermissionsController(PermissionsService PermissionsService) {
        this.PermissionsService = PermissionsService;
    }
	
    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateTimeUtils.timestampRequestDataBinder(binder);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, PermissionsQueryCriteria criteria) throws IOException {
        PermissionsService.download(PermissionsService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询权限")
    @ApiOperation("查询权限")
        public ResponseEntity getPermissionss(PermissionsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(PermissionsService.queryAll(criteria,pageable),HttpStatus.OK);
    }
	
    @GetMapping(value = "/load/{id}")
    @Log("装载权限")
    @ApiOperation("装载权限")
        public ResponseEntity load(@PathVariable Long id){
        return new ResponseEntity(PermissionsService.findById(id),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增权限")
    @ApiOperation("新增权限")
        public ResponseEntity create(@Validated @RequestBody Permissions resources, HttpServletRequest request){
	    if (!AuthorizationUtils.isLogin(request)) {
	        throw new BadRequestException("请先进行登录操作");
	    }
	 	String profileid = AuthorizationUtils.getProfileid(request);
	 	resources.setAuthor(profileid);
	 	resources.setId(ContentUtils.makeContentId("base_permissions"));
        return new ResponseEntity<>(PermissionsService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @Log("修改权限")
    @ApiOperation("修改权限")
        public ResponseEntity update(@PathVariable Long id,@Validated @RequestBody Permissions resources){
        return new ResponseEntity(PermissionsService.update(id,resources),HttpStatus.OK);
    }
	
    @DeleteMapping(value = "/{id}")
    @Log("逻辑删除权限")
    @ApiOperation("逻辑删除权限")
        public ResponseEntity delete(@PathVariable Long id){
        PermissionsService.makedelete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Log("物理删除权限")
    @ApiOperation("物理删除权限")
        public ResponseEntity fulldelete(@PathVariable Long id){
        PermissionsService.delete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }
}