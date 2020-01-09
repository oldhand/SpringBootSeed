package com.github.cores.rest;

import com.github.aop.log.Log;
import com.github.cores.domain.Picklists;
import com.github.cores.service.PicklistsService;
import com.github.cores.service.dto.PicklistsQueryCriteria;
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
@Api(tags = "后台：字典管理")
@RestController
@RequestMapping("/api/picklists")
public class PicklistsController {

    private final PicklistsService PicklistsService;
	
	private final ContentIdsService ContentIdsService;

    public PicklistsController(PicklistsService PicklistsService,ContentIdsService ContentIdsService) {
        this.PicklistsService = PicklistsService;
		this.ContentIdsService = ContentIdsService;
    }
	
    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateTimeUtils.timestampRequestDataBinder(binder);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, PicklistsQueryCriteria criteria) throws IOException {
        PicklistsService.download(PicklistsService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询字典")
    @ApiOperation("查询字典")
        public ResponseEntity getPicklistss(PicklistsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(PicklistsService.queryAll(criteria,pageable),HttpStatus.OK);
    }
	
    @GetMapping(value = "/load/{id}")
    @Log("装载字典")
    @ApiOperation("装载字典")
        public ResponseEntity load(@PathVariable Long id){
        return new ResponseEntity(PicklistsService.findById(id),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增字典")
    @ApiOperation("新增字典")
        public ResponseEntity create(@Validated @RequestBody Picklists resources, HttpServletRequest request){
	    if (!AuthorizationUtils.isLogin(request)) {
	        throw new BadRequestException("请先进行登录操作");
	    }
	 	String profileid = AuthorizationUtils.getProfileid(request);
	 	resources.setAuthor(profileid);
	    long ContentID = ContentIdsService.create("base_picklists");
	 	resources.setId(ContentID);
        return new ResponseEntity<>(PicklistsService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @Log("修改字典")
    @ApiOperation("修改字典")
        public ResponseEntity update(@PathVariable Long id,@Validated @RequestBody Picklists resources){
        return new ResponseEntity(PicklistsService.update(id,resources),HttpStatus.OK);
    }
	
    @DeleteMapping(value = "/{id}")
    @Log("逻辑删除字典")
    @ApiOperation("逻辑删除字典")
        public ResponseEntity delete(@PathVariable Long id){
        PicklistsService.makedelete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Log("物理删除字典")
    @ApiOperation("物理删除字典")
        public ResponseEntity fulldelete(@PathVariable Long id){
        PicklistsService.delete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }
}