package com.github.cores.rest;

import com.github.utils.ContentUtils;
import com.github.utils.MqUtils;
import com.github.aop.log.Log;
import com.github.cores.domain.Modentitynos;
import com.github.cores.service.ModentitynosService;
import com.github.cores.service.dto.ModentitynosDTO;
import com.github.cores.service.dto.ModentitynosQueryCriteria;
import com.github.exception.BadRequestException;
import com.github.rabbitmq.producer.Sender;
import com.github.rabbitmq.service.MqService;
import com.github.rabbitmq.service.dto.MqDTO;
import com.github.service.ContentIdsService;
import com.github.utils.AuthorizationUtils;
import com.github.utils.DateTimeUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
* @author oldhand
* @date 2020-01-15
*/
@Api(tags = "实例：编号管理")
@RestController
@RequestMapping("/api/modentitynos")
public class ModentitynosController {

    private final ModentitynosService ModentitynosService;
	
	private final ContentIdsService ContentIdsService;

    public ModentitynosController(ModentitynosService ModentitynosService,ContentIdsService ContentIdsService) {
        this.ModentitynosService = ModentitynosService;
		this.ContentIdsService = ContentIdsService;
    }
	
    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateTimeUtils.timestampRequestDataBinder(binder);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, ModentitynosQueryCriteria criteria) throws IOException {
        ModentitynosService.download(ModentitynosService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询编号")
    @ApiOperation("查询编号")
        public ResponseEntity getModentitynoss(ModentitynosQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(ModentitynosService.queryAll(criteria,pageable),HttpStatus.OK);
    }
	
    @GetMapping(value = "/load/{id}")
    @Log("装载编号")
    @ApiOperation("装载编号")
        public ResponseEntity load(@PathVariable Long id){
        return new ResponseEntity(ModentitynosService.findById(id),HttpStatus.OK);
    }

    @PostMapping(value = "/make/{tabid}")
    @Log("生成编号")
    @ApiOperation("生成编号")
    public ResponseEntity make(@PathVariable Integer tabid, HttpServletRequest request){
        try {
            Map<String,Object> map = new HashMap<>();
            map.put("tabid",tabid);
            map.put("no",MqUtils.makeModEntityNo(tabid));
            return new ResponseEntity(map,HttpStatus.OK);
        }catch(Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PostMapping
    @Log("新增编号")
    @ApiOperation("新增编号")
        public ResponseEntity create(@Validated @RequestBody Modentitynos resources, HttpServletRequest request){
	    if (!AuthorizationUtils.isLogin(request)) {
	        throw new BadRequestException("请先进行登录操作");
	    }
	 	String profileid = AuthorizationUtils.getProfileid(request);
	 	resources.setAuthor(profileid);
	 	resources.setId(ContentUtils.makeContentId("base_modentitynos"));
        return new ResponseEntity<>(ModentitynosService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @Log("修改编号")
    @ApiOperation("修改编号")
        public ResponseEntity update(@PathVariable Long id,@Validated @RequestBody Modentitynos resources){
        return new ResponseEntity(ModentitynosService.update(id,resources),HttpStatus.OK);
    }
	
    @DeleteMapping(value = "/{id}")
    @Log("逻辑删除编号")
    @ApiOperation("逻辑删除编号")
        public ResponseEntity delete(@PathVariable Long id){
        ModentitynosService.makedelete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Log("物理删除编号")
    @ApiOperation("物理删除编号")
        public ResponseEntity fulldelete(@PathVariable Long id){
        ModentitynosService.delete(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }
}