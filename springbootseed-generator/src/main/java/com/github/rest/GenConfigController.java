package com.github.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.github.domain.GenConfig;
import com.github.service.GenConfigService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@RestController
@RequestMapping("/api/genConfig")
@Api(tags = "系统：代码生成器配置管理")
public class GenConfigController {

    private final GenConfigService genConfigService;

    public GenConfigController(GenConfigService genConfigService) {
        this.genConfigService = genConfigService;
    }

    @ApiOperation("查询")
    @GetMapping
    public ResponseEntity get(){
        return new ResponseEntity<>(genConfigService.find(), HttpStatus.OK);
    }

    @ApiOperation("修改")
    @PutMapping
    public ResponseEntity emailConfig(@Validated @RequestBody GenConfig genConfig){
        return new ResponseEntity<>(genConfigService.update(genConfig),HttpStatus.OK);
    }
}
