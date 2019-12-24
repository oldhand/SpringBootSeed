package com.github.samples.rest;

import com.github.aop.log.Log;
import com.github.samples.domain.User;
import com.github.samples.service.UserService;
import com.github.samples.service.dto.UserQueryCriteria;
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
* @date 2019-12-23
*/
@Api(tags = "实例：User管理")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, UserQueryCriteria criteria) throws IOException {
        userService.download(userService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询User")
    @ApiOperation("查询User")
        public ResponseEntity getUsers(UserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(userService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增User")
    @ApiOperation("新增User")
        public ResponseEntity create(@Validated @RequestBody User resources){
        return new ResponseEntity<>(userService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改User")
    @ApiOperation("修改User")
        public ResponseEntity update(@Validated @RequestBody User resources){
        userService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除User")
    @ApiOperation("删除User")
        public ResponseEntity delete(@PathVariable Long id){
        userService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}