package ${package}.rest;

import com.github.aop.log.Log;
import ${package}.domain.${className};
import ${package}.service.${className}Service;
import ${package}.service.dto.${className}QueryCriteria;
import com.github.exception.BadRequestException;
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
* @author ${author}
* @date ${date}
*/
@Api(tags = "实例：${className}管理")
@RestController
@RequestMapping("/api/${changeClassName?lower_case}")
public class ${className}Controller {

    private final ${className}Service ${changeClassName}Service;


    public ${className}Controller(${className}Service ${changeClassName}Service) {
        this.${changeClassName}Service = ${changeClassName}Service;
    }
	
    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateTimeUtils.timestampRequestDataBinder(binder);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, ${className}QueryCriteria criteria) throws IOException {
        ${changeClassName}Service.download(${changeClassName}Service.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询${className}")
    @ApiOperation("查询${className}")
        public ResponseEntity get${className}s(${className}QueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(${changeClassName}Service.queryAll(criteria,pageable),HttpStatus.OK);
    }
	
    @GetMapping(value = "/load/{${pkChangeColName}}")
    @Log("装载${className}")
    @ApiOperation("装载${className}")
        public ResponseEntity load(@PathVariable ${pkColumnType} ${pkChangeColName}){
        return new ResponseEntity(${changeClassName}Service.findById(${pkChangeColName}),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增${className}")
    @ApiOperation("新增${className}")
        public ResponseEntity create(@Validated @RequestBody ${className} resources, HttpServletRequest request){
	    if (!AuthorizationUtils.isLogin(request)) {
	        throw new BadRequestException("请先进行登录操作");
	    }
	 	String profileid = AuthorizationUtils.getProfileid(request);
	 	resources.setAuthor(profileid);
	 	resources.setId(ContentUtils.makeContentId("${tableName}"));
        return new ResponseEntity<>(${changeClassName}Service.create(resources),HttpStatus.CREATED);
    }

    @PutMapping(value = "/{${pkChangeColName}}")
    @Log("修改${className}")
    @ApiOperation("修改${className}")
        public ResponseEntity update(@PathVariable ${pkColumnType} ${pkChangeColName},@Validated @RequestBody ${className} resources){
        return new ResponseEntity(${changeClassName}Service.update(${pkChangeColName},resources),HttpStatus.OK);
    }
	
    @DeleteMapping(value = "/{${pkChangeColName}}")
    @Log("逻辑删除${className}")
    @ApiOperation("逻辑删除${className}")
        public ResponseEntity delete(@PathVariable ${pkColumnType} ${pkChangeColName}){
        ${changeClassName}Service.makedelete(${pkChangeColName});
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{${pkChangeColName}}")
    @Log("物理删除${className}")
    @ApiOperation("物理删除${className}")
        public ResponseEntity fulldelete(@PathVariable ${pkColumnType} ${pkChangeColName}){
        ${changeClassName}Service.delete(${pkChangeColName});
        return new ResponseEntity("ok",HttpStatus.OK);
    }
}