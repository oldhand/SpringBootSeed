package com.github.localstorage.rest;

import com.github.aop.log.Log;
import com.github.localstorage.domain.LocalStorage;
import com.github.localstorage.service.LocalStorageService;
import com.github.localstorage.service.dto.LocalStorageQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @author oldhand
* @date 2019-12-16
*/
@Api(tags = "工具：本地存储管理")
@RestController
@RequestMapping("/api/localStorage")
public class LocalStorageController {

    private final LocalStorageService localStorageService;

    public LocalStorageController(LocalStorageService localStorageService) {
        this.localStorageService = localStorageService;
    }

    @Log("查询文件")
    @ApiOperation("查询文件")
    @GetMapping
        public ResponseEntity getLocalStorages(LocalStorageQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(localStorageService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, LocalStorageQueryCriteria criteria) throws IOException {
        localStorageService.download(localStorageService.queryAll(criteria), response);
    }

    @Log("上传文件")
    @ApiOperation("上传文件")
    @PostMapping
        public ResponseEntity create(@RequestParam String name, @RequestParam("file") MultipartFile file){
        return new ResponseEntity<>(localStorageService.create(name, file),HttpStatus.CREATED);
    }

    @Log("修改文件")
    @ApiOperation("修改文件")
    @PutMapping
        public ResponseEntity update(@Validated @RequestBody LocalStorage resources){
        localStorageService.update(resources);
        return new ResponseEntity("ok", HttpStatus.NO_CONTENT);
    }

    @Log("删除文件")
    @ApiOperation("删除文件")
    @DeleteMapping(value = "/{id}")
        public ResponseEntity delete(@PathVariable Long id){
        localStorageService.delete(id);
        return new ResponseEntity("ok", HttpStatus.OK);
    }

    @Log("多选删除")
    @DeleteMapping
    @ApiOperation("多选删除")
    public ResponseEntity deleteAll(@RequestBody Long[] ids) {
        localStorageService.deleteAll(ids);
        return new ResponseEntity("ok", HttpStatus.OK);
    }
}