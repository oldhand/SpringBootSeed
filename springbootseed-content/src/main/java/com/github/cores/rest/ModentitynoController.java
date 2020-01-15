package com.github.cores.rest;

import com.github.aop.log.Log;
import com.github.cores.domain.Modentityno;
import com.github.cores.service.ModentitynoService;
import com.github.cores.service.dto.ModentitynoQueryCriteria;
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
* @date 2020-01-15
*/
@Api(tags = "后台：编号管理")
@RestController
@RequestMapping("/api/modentityno")
public class ModentitynoController {

    private final ModentitynoService ModentitynoService;
	
	private final ContentIdsService ContentIdsService;

    public ModentitynoController(ModentitynoService ModentitynoService,ContentIdsService ContentIdsService) {
        this.ModentitynoService = ModentitynoService;
		this.ContentIdsService = ContentIdsService;
    }
	
    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateTimeUtils.timestampRequestDataBinder(binder);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, ModentitynoQueryCriteria criteria) throws IOException {
        ModentitynoService.download(ModentitynoService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询编号")
    @ApiOperation("查询编号")
        public ResponseEntity getModentitynos(ModentitynoQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(ModentitynoService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value = "/make/{tabid}")
    @Log("生成编号")
    @ApiOperation("生成编号")
    public ResponseEntity getModentitynos(@PathVariable long tabid){

        return new ResponseEntity("ok",HttpStatus.OK);
    }
	
    @GetMapping(value = "/load/{id}")
    @Log("装载编号")
    @ApiOperation("装载编号")
        public ResponseEntity load(@PathVariable Long id){
        return new ResponseEntity(ModentitynoService.findById(id),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增编号")
    @ApiOperation("新增编号")
        public ResponseEntity create(@Validated @RequestBody Modentityno resources, HttpServletRequest request){
	    if (!AuthorizationUtils.isLogin(request)) {
	        throw new BadRequestException("请先进行登录操作");
	    }
	 	String profileid = AuthorizationUtils.getProfileid(request);
	 	resources.setAuthor(profileid);
	    long ContentID = ContentIdsService.create("base_modentityno");
	 	resources.setId(ContentID);
        return new ResponseEntity<>(ModentitynoService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @Log("修改编号")
    @ApiOperation("修改编号")
        public ResponseEntity update(@PathVariable Long id,@Validated @RequestBody Modentityno resources){
        return new ResponseEntity(ModentitynoService.update(id,resources),HttpStatus.OK);
    }
	
    @DeleteMapping(value = "/{id}")
    @Log("逻辑删除编号")
    @ApiOperation("逻辑删除编号")
        public ResponseEntity delete(@PathVariable Long id){
        ModentitynoService.makedelete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Log("物理删除编号")
    @ApiOperation("物理删除编号")
        public ResponseEntity fulldelete(@PathVariable Long id){
        ModentitynoService.delete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }
}