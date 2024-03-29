package com.github.cores.rest;

import com.github.utils.ContentUtils;
import com.github.aop.log.Log;
import com.github.cores.domain.Users;
import com.github.cores.service.UsersService;
import com.github.cores.service.dto.UsersQueryCriteria;
import com.github.exception.BadRequestException;
import com.github.utils.AuthorizationUtils;
import com.github.utils.DateTimeUtils;
import com.github.utils.MqUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@Api(tags = "后台：用户管理")
@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService UsersService;

    public UsersController(UsersService UsersService) {
        this.UsersService = UsersService;
    }
	
    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateTimeUtils.timestampRequestDataBinder(binder);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, UsersQueryCriteria criteria) throws IOException {
        UsersService.download(UsersService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询用户")
    @ApiOperation("查询用户")
        public ResponseEntity getUserss(UsersQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(UsersService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value = "/load/{id}")
    @Log("装载用户")
    @ApiOperation("装载用户")
        public ResponseEntity load(@PathVariable Long id){
        return new ResponseEntity(UsersService.findById(id),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增用户")
    @ApiOperation("新增用户")
        public ResponseEntity create(@Validated @RequestBody Users resources, HttpServletRequest request){
	    if (!AuthorizationUtils.isLogin(request)) {
	        throw new BadRequestException("请先进行登录操作");
	    }
        String modentityno;
	    try {
	        modentityno = MqUtils.makeModEntityNo("Users");
        }
        catch(Exception e) {
            throw new BadRequestException(e.getMessage());
        }
	 	String profileid = AuthorizationUtils.getProfileid(request);
	 	resources.setAuthor(profileid);
	 	resources.setId(ContentUtils.makeContentId("base_users"));
        resources.setUsersNo(modentityno);
        return new ResponseEntity<>(UsersService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @Log("修改用户")
    @ApiOperation("修改用户")
        public ResponseEntity update(@PathVariable Long id,@Validated @RequestBody Users resources){
        return new ResponseEntity(UsersService.update(id,resources),HttpStatus.OK);
    }
	
    @DeleteMapping(value = "/{id}")
    @Log("逻辑删除用户")
    @ApiOperation("逻辑删除用户")
        public ResponseEntity delete(@PathVariable Long id){
        UsersService.makedelete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Log("物理删除用户")
    @ApiOperation("物理删除用户")
        public ResponseEntity fulldelete(@PathVariable Long id){
        UsersService.delete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }
}