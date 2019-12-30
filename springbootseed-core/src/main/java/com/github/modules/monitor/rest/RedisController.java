package com.github.modules.monitor.rest;

import com.github.exception.BadRequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.github.aop.log.Log;
import com.github.modules.monitor.domain.vo.RedisVo;
import com.github.modules.monitor.service.RedisService;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@RestController
@RequestMapping("/api/redis")
@Api(tags = "系统：Redis缓存管理")
public class RedisController {

    private final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @Log("查询Redis缓存")
    @GetMapping
    @ApiOperation("查询Redis缓存")
        public ResponseEntity getRedis(String key, Pageable pageable){
        return new ResponseEntity<>(redisService.findByKey(key,pageable), HttpStatus.OK);
    }

    @Log("导出缓存")
    @ApiOperation("导出缓存")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, String key) throws IOException {
        redisService.download(redisService.findByKey(key), response);
    }

    @Log("删除Redis缓存")
    @DeleteMapping(value = "/{id}")
    @ApiImplicitParam(name = "id",value = "缓存id",required = true,dataType = "String",paramType = "path")
    @ApiOperation("删除Redis缓存")
        public ResponseEntity delete(@PathVariable String id){
        if (id.isEmpty()) {
            throw new BadRequestException("缓存ID不能为空");
        }
        redisService.delete(id);
        return new ResponseEntity("ok", HttpStatus.OK);
    }

    @Log("删除Redis缓存标签")
    @DeleteMapping(value = "/tag/{keys}")
    @ApiOperation("删除Redis缓存标签")
    @ApiImplicitParam(name = "keys",value = "缓存标签 (分号或逗号分隔)",required = true,dataType = "String",paramType = "path")
    public ResponseEntity deleteTag(@PathVariable String keys){
        if (keys.isEmpty()) {
            throw new BadRequestException("缓存标签不能为空");
        }
        redisService.deleteTag(keys);
        return new ResponseEntity("ok", HttpStatus.OK);
    }

    @Log("清空Redis缓存")
    @DeleteMapping(value = "/all")
    @ApiOperation("清空Redis缓存")
        public ResponseEntity deleteAll(){
        redisService.deleteAll();
        return new ResponseEntity("ok", HttpStatus.OK);
    }
}
