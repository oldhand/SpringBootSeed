package com.github.cores.rest;

import com.github.aop.log.Log;
import com.github.cores.domain.Roles;
import com.github.cores.service.RolesService;
import com.github.cores.service.dto.RolesQueryCriteria;
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
@Api(tags = "后台：角色管理")
@RestController
@RequestMapping("/api/roles")
public class RolesController {

    private final RolesService RolesService;
	
	private final ContentIdsService ContentIdsService;

    public RolesController(RolesService RolesService,ContentIdsService ContentIdsService) {
        this.RolesService = RolesService;
		this.ContentIdsService = ContentIdsService;
    }
	
    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateTimeUtils.timestampRequestDataBinder(binder);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, RolesQueryCriteria criteria) throws IOException {
        RolesService.download(RolesService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询角色")
    @ApiOperation("查询角色")
        public ResponseEntity getRoless(RolesQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(RolesService.queryAll(criteria,pageable),HttpStatus.OK);
    }
	
    @GetMapping(value = "/load/{id}")
    @Log("装载角色")
    @ApiOperation("装载角色")
        public ResponseEntity load(@PathVariable Long id){
        return new ResponseEntity(RolesService.findById(id),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增角色")
    @ApiOperation("新增角色")
        public ResponseEntity create(@Validated @RequestBody Roles resources, HttpServletRequest request){
	    if (!AuthorizationUtils.isLogin(request)) {
	        throw new BadRequestException("请先进行登录操作");
	    }
	 	String profileid = AuthorizationUtils.getProfileid(request);
	 	resources.setAuthor(profileid);
	    long ContentID = ContentIdsService.create("base_roles");
	 	resources.setId(ContentID);
        return new ResponseEntity<>(RolesService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @Log("修改角色")
    @ApiOperation("修改角色")
        public ResponseEntity update(@PathVariable Long id,@Validated @RequestBody Roles resources){
        return new ResponseEntity(RolesService.update(id,resources),HttpStatus.OK);
    }
	
    @DeleteMapping(value = "/{id}")
    @Log("逻辑删除角色")
    @ApiOperation("逻辑删除角色")
        public ResponseEntity delete(@PathVariable Long id){
        RolesService.makedelete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Log("物理删除角色")
    @ApiOperation("物理删除角色")
        public ResponseEntity fulldelete(@PathVariable Long id){
        RolesService.delete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }
}