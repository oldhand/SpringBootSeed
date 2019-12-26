package com.github.rest;

import cn.hutool.core.util.PageUtil;
import com.github.domain.GenConfig;
import com.github.aop.log.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.github.domain.vo.ColumnInfo;
import com.github.exception.BadRequestException;
import com.github.service.GenConfigService;
import com.github.service.GeneratorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * @author oldhand
 * @date 2019-12-16
*/

@RestController
@RequestMapping("/api/generator")
@Api(tags = "系统：代码生成管理")
public class GeneratorController {

    private final GeneratorService generatorService;

    private final GenConfigService genConfigService;

    @Value("${generator.enabled}")
    private Boolean generatorEnabled;

    public GeneratorController(GeneratorService generatorService, GenConfigService genConfigService) {
        this.generatorService = generatorService;
        this.genConfigService = genConfigService;
    }

    @Log("查询代码生成配置")
    @ApiOperation("查询代码生成配置")
    @GetMapping(value = "/config")
    public ResponseEntity get(){
        return new ResponseEntity<>(genConfigService.find(), HttpStatus.OK);
    }

    @Log("修改代码生成配置")
    @ApiOperation("修改代码生成配置")
    @PutMapping(value = "/config")
    public ResponseEntity emailConfig(@Validated @RequestBody GenConfig genConfig){
        return new ResponseEntity<>(genConfigService.update(genConfig),HttpStatus.OK);
    }

    @Log("查询数据库元数据")
    @ApiOperation("查询数据库元数据")
    @GetMapping(value = "/tables")
    public ResponseEntity getTables(@RequestParam(defaultValue = "") String name,
                                    @RequestParam(defaultValue = "0")Integer page,
                                    @RequestParam(defaultValue = "10")Integer size){
        int[] startEnd = PageUtil.transToStartEnd(page+1, size);
        return new ResponseEntity<>(generatorService.getTables(name,startEnd), HttpStatus.OK);
    }

    @Log("查询表内元数据")
    @ApiOperation("查询表内元数据")
    @GetMapping(value = "/columns")
    public ResponseEntity getTables(@RequestParam String tableName){
        return new ResponseEntity<>(generatorService.getColumns(tableName), HttpStatus.OK);
    }

    @Log("生成代码(根据元数据)")
    @ApiOperation("生成代码(根据元数据)")
    @PostMapping(value = "/columns")
    public ResponseEntity generatorbycolumns(@RequestBody List<ColumnInfo> columnInfos, @RequestParam String tableName){
        if(!generatorEnabled){
            throw new BadRequestException("此环境不允许生成代码！");
        }
        generatorService.generator(columnInfos,genConfigService.find(),tableName);
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @Log("生成代码")
    @ApiOperation("生成代码")
    @PostMapping
    public ResponseEntity generator(@RequestParam String tableName){
        if(!generatorEnabled){
            throw new BadRequestException("此环境不允许生成代码！");
        }
        Map<String,Object> columns = (Map<String,Object>)generatorService.getColumns(tableName);
        List<ColumnInfo> columnInfos = (List<ColumnInfo>)columns.get("content");
        generatorService.generator(columnInfos,genConfigService.find(),tableName);
        return new ResponseEntity("ok", HttpStatus.OK);
    }


}
