//package com.github.content.rest;
//
//import com.github.aop.log.Log;
//import com.github.cores.domain.Depts;
//import com.github.cores.service.DeptsService;
//import com.github.cores.service.dto.DeptsQueryCriteria;
//import com.github.exception.BadRequestException;
//import com.github.service.ContentIdsService;
//import com.github.utils.AuthorizationUtils;
//import com.github.utils.DateTimeUtils;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.ServletRequestDataBinder;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Map;
//
///**
//* @author oldhand
//* @date 2020-01-02
//*/
//@Api(tags = "数据库中间件：文档管理")
//@RestController
//@RequestMapping("/api/content")
//public class ContentController {
//
//    private final DeptsService DeptsService;
//
//	private final ContentIdsService ContentIdsService;
//
//    public ContentController(DeptsService DeptsService, ContentIdsService ContentIdsService) {
//        this.DeptsService = DeptsService;
//		this.ContentIdsService = ContentIdsService;
//    }
//
//    @InitBinder
//    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
//        DateTimeUtils.timestampRequestDataBinder(binder);
//    }
//
//    @Log("导出数据")
//    @ApiOperation("导出数据")
//    @GetMapping(value = "/download")
//        public void download(HttpServletResponse response, DeptsQueryCriteria criteria) throws IOException {
//        DeptsService.download(DeptsService.queryAll(criteria), response);
//    }
//
//    @GetMapping
//    @Log("查询")
//    @ApiOperation("查询")
//        public ResponseEntity getDeptss(DeptsQueryCriteria criteria, Pageable pageable){
//        return new ResponseEntity<>(DeptsService.queryAll(criteria,pageable),HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/load/{id}")
//    @Log("装载")
//    @ApiOperation("装载")
//        public ResponseEntity load(@PathVariable Long id){
//        return new ResponseEntity(DeptsService.findById(id),HttpStatus.OK);
//    }
//
//    @PostMapping
//    @Log("新增")
//    @ApiOperation("新增")
//        public ResponseEntity create(@PathVariable String tablename,@Validated @RequestBody Map resources, HttpServletRequest request){
//	    if (!AuthorizationUtils.isLogin(request)) {
//	        throw new BadRequestException("请先进行登录操作");
//	    }
//	 	String profileid = AuthorizationUtils.getProfileid(request);
////	 	resources.setAuthor(profileid);
////	    long ContentID = ContentIdsService.create("base_depts");
////	 	resources.setId(ContentID);
//        //return new ResponseEntity<>(DeptsService.create(resources),HttpStatus.CREATED);
//        return new ResponseEntity("ok",HttpStatus.OK);
//    }
//
//    @PutMapping(value = "/{id}")
//    @Log("修改")
//    @ApiOperation("修改")
//        public ResponseEntity update(@PathVariable Long id,@Validated @RequestBody Depts resources){
//        return new ResponseEntity(DeptsService.update(id,resources),HttpStatus.OK);
//    }
//
//    @DeleteMapping(value = "/{id}")
//    @Log("逻辑删除")
//    @ApiOperation("逻辑删除")
//        public ResponseEntity delete(@PathVariable Long id){
//        DeptsService.makedelete(id);
//        return new ResponseEntity("ok",HttpStatus.OK);
//    }
//
//    @DeleteMapping(value = "/delete/{id}")
//    @Log("物理删除")
//    @ApiOperation("物理删除")
//        public ResponseEntity fulldelete(@PathVariable Long id){
//        DeptsService.delete(id);
//        return new ResponseEntity("ok",HttpStatus.OK);
//    }
//}