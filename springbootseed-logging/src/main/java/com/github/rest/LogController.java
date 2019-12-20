package com.github.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.github.aop.log.Log;
import com.github.service.LogService;
import com.github.service.dto.LogQueryCriteria;
import com.github.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@RestController
@RequestMapping("/api/logs")
@Api(tags = "监控：日志管理")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, LogQueryCriteria criteria) throws IOException {
        logService.download(logService.queryAll(criteria), response);
    }

    @Log("日志查询")
    @GetMapping
    @ApiOperation("日志查询")
    public ResponseEntity getLogs(LogQueryCriteria criteria, Pageable pageable){
        criteria.setLogType("INFO");
        return new ResponseEntity<>(logService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @Log("用户日志查询")
    @GetMapping(value = "/user")
    @ApiOperation("用户日志查询")
    public ResponseEntity getUserLogs(LogQueryCriteria criteria, Pageable pageable){
        criteria.setLogType("INFO");
        String username = "anonymous";
        try {
            username = SecurityUtils.getUsername();
        } catch (Exception e) {
        }
        criteria.setBlurry(username);
        return new ResponseEntity<>(logService.queryAllByUser(criteria,pageable), HttpStatus.OK);
    }

    @Log("错误日志查询")
    @GetMapping(value = "/error")
    @ApiOperation("错误日志查询")
    public ResponseEntity getErrorLogs(LogQueryCriteria criteria, Pageable pageable){
        criteria.setLogType("ERROR");
        return new ResponseEntity<>(logService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @Log("日志异常详情查询")
    @GetMapping(value = "/error/{id}")
    @ApiOperation("日志异常详情查询")
    public ResponseEntity getErrorLogs(@PathVariable Long id){
        return new ResponseEntity<>(logService.findByErrDetail(id), HttpStatus.OK);
    }
}
