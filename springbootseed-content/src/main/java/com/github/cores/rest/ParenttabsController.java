package com.github.cores.rest;

import com.github.aop.log.Log;
import com.github.cores.domain.Parenttabs;
import com.github.cores.service.ParenttabsService;
import com.github.cores.service.dto.ParenttabsQueryCriteria;
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
* @date 2020-01-14
*/
@Api(tags = "后台：父模块管理")
@RestController
@RequestMapping("/api/parenttabs")
public class ParenttabsController {

    private final ParenttabsService ParenttabsService;
	
	private final ContentIdsService ContentIdsService;

    public ParenttabsController(ParenttabsService ParenttabsService,ContentIdsService ContentIdsService) {
        this.ParenttabsService = ParenttabsService;
		this.ContentIdsService = ContentIdsService;
    }
	
    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateTimeUtils.timestampRequestDataBinder(binder);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, ParenttabsQueryCriteria criteria) throws IOException {
        ParenttabsService.download(ParenttabsService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询父模块")
    @ApiOperation("查询父模块")
        public ResponseEntity getParenttabss(ParenttabsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(ParenttabsService.queryAll(criteria,pageable),HttpStatus.OK);
    }
	
    @GetMapping(value = "/load/{id}")
    @Log("装载父模块")
    @ApiOperation("装载父模块")
        public ResponseEntity load(@PathVariable Long id){
        return new ResponseEntity(ParenttabsService.findById(id),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增父模块")
    @ApiOperation("新增父模块")
        public ResponseEntity create(@Validated @RequestBody Parenttabs resources, HttpServletRequest request){
	    if (!AuthorizationUtils.isLogin(request)) {
	        throw new BadRequestException("请先进行登录操作");
	    }
	 	String profileid = AuthorizationUtils.getProfileid(request);
	 	resources.setAuthor(profileid);
	    long ContentID = ContentIdsService.create("base_parenttabs");
	 	resources.setId(ContentID);
        return new ResponseEntity<>(ParenttabsService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @Log("修改父模块")
    @ApiOperation("修改父模块")
        public ResponseEntity update(@PathVariable Long id,@Validated @RequestBody Parenttabs resources){
        return new ResponseEntity(ParenttabsService.update(id,resources),HttpStatus.OK);
    }
	
    @DeleteMapping(value = "/{id}")
    @Log("逻辑删除父模块")
    @ApiOperation("逻辑删除父模块")
        public ResponseEntity delete(@PathVariable Long id){
        ParenttabsService.makedelete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Log("物理删除父模块")
    @ApiOperation("物理删除父模块")
        public ResponseEntity fulldelete(@PathVariable Long id){
        ParenttabsService.delete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }
}