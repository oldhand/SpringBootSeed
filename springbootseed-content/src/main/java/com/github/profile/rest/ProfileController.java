package com.github.profile.rest;

import com.github.aop.log.Log;
import com.github.profile.domain.*;
import com.github.profile.service.ProfileService;
import com.github.profile.service.dto.ProfileQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2019-12-27
*/
@Api(tags = "数据库中间件：用户管理")
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Log("导出用户数据")
    @ApiOperation("导出用户数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, ProfileQueryCriteria criteria) throws IOException {
        profileService.download(profileService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询用户")
    @ApiOperation("查询用户")
        public ResponseEntity getProfiles(ProfileQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(profileService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    @Log("注册用户")
    @ApiOperation("注册用户")
        public ResponseEntity create(@Validated @RequestBody RegisterProfile resources){
        //return new ResponseEntity<>(profileService.create(resources),HttpStatus.CREATED);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/login")
    @Log("登录")
    @ApiOperation("登录")
    public ResponseEntity login(@Validated LoginProfile resources){
        //return new ResponseEntity<>(profileService.create(resources),HttpStatus.CREATED);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/logout/{id}")
    @Log("注销")
    @ApiOperation("注销")
    public ResponseEntity logout(@PathVariable String id){
        //return new ResponseEntity<>(profileService.create(resources),HttpStatus.CREATED);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping
    @Log("修改用户")
    @ApiOperation("修改用户")
        public ResponseEntity update(@Validated @RequestBody UpdateProfile resources){
        //profileService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @PutMapping(value = "/password")
    @Log("修改密码")
    @ApiOperation("修改密码")
    public ResponseEntity changePassword(@Validated @RequestBody ChangePassword resources){
        //profileService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("禁止用户")
    @ApiOperation("禁止用户")
        public ResponseEntity delete(@PathVariable String id){
        profileService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}