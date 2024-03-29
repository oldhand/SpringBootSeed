package com.github.cores.rest;

import com.github.utils.ContentUtils;
import com.github.aop.log.Log;
import com.github.cores.domain.Tabs;
import com.github.cores.service.TabsService;
import com.github.cores.service.dto.TabsQueryCriteria;
import com.github.domain.Authorization;
import com.github.exception.BadRequestException;
import com.github.service.ContentIdsService;
import com.github.utils.AuthorizationUtils;
import com.github.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
* @author oldhand
* @date 2020-01-14
*/
@Api(tags = "后台：模块管理")
@RestController
@RequestMapping("/api/tabs")
public class TabsController {

    private final TabsService TabsService;

    public TabsController(TabsService TabsService) {
        this.TabsService = TabsService;
    }
	
    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateTimeUtils.timestampRequestDataBinder(binder);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, TabsQueryCriteria criteria) throws IOException {
        TabsService.download(TabsService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询模块")
    @ApiOperation("查询模块")
        public ResponseEntity getTabss(TabsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(TabsService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value = "/buildmemus")
    @Log("获取菜单")
    @ApiOperation("获取菜单")
    public ResponseEntity buildMemus(HttpServletRequest request){
        if (!AuthorizationUtils.isLogin(request)) {
            throw new BadRequestException("请先进行登录操作");
        }
        Authorization authorization = AuthorizationUtils.get(request);
        String profileid = authorization.getProfileid();
        long saasid = authorization.getSaasid();
        if (StringUtils.isEmpty(profileid)) {
            throw new BadRequestException("请先进行登录操作");
        }
        if (saasid == 0) {
            throw new BadRequestException("当前登录用户没有云服务数据");
        }
        List<Object> info = TabsService.buildMemus(saasid,profileid);
        return new ResponseEntity(info,HttpStatus.OK);
    }
	
    @GetMapping(value = "/load/{id}")
    @Log("装载模块")
    @ApiOperation("装载模块")
        public ResponseEntity load(@PathVariable Long id){
        return new ResponseEntity(TabsService.findById(id),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增模块")
    @ApiOperation("新增模块")
        public ResponseEntity create(@Validated @RequestBody Tabs resources, HttpServletRequest request){
	    if (!AuthorizationUtils.isLogin(request)) {
	        throw new BadRequestException("请先进行登录操作");
	    }
	 	String profileid = AuthorizationUtils.getProfileid(request);
	 	resources.setAuthor(profileid);
	 	resources.setId(ContentUtils.makeContentId("base_tabs"));
        return new ResponseEntity<>(TabsService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @Log("修改模块")
    @ApiOperation("修改模块")
        public ResponseEntity update(@PathVariable Long id,@Validated @RequestBody Tabs resources){
        return new ResponseEntity(TabsService.update(id,resources),HttpStatus.OK);
    }
	
    @DeleteMapping(value = "/{id}")
    @Log("逻辑删除Tabs")
    @ApiOperation("逻辑删除模块")
        public ResponseEntity delete(@PathVariable Long id){
        TabsService.makedelete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Log("物理删除模块")
    @ApiOperation("物理删除模块")
        public ResponseEntity fulldelete(@PathVariable Long id){
        TabsService.delete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }
}