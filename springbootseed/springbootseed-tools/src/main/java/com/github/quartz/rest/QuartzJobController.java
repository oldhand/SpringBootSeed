package com.github.quartz.rest;

import com.github.quartz.domain.QuartzJob;
import com.github.quartz.service.QuartzJobService;
import com.github.quartz.service.dto.JobQueryCriteria;
import com.github.utils.DateTimeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.github.aop.log.Log;
import com.github.exception.BadRequestException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Slf4j
@RestController
@Api(tags = "工具：定时任务管理")
@RequestMapping("/api/jobs")
public class QuartzJobController {

    private static final String ENTITY_NAME = "quartzJob";

    private final QuartzJobService quartzJobService;

    public QuartzJobController(QuartzJobService quartzJobService) {
        this.quartzJobService = quartzJobService;
    }

    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateTimeUtils.timestampRequestDataBinder(binder);
    }

    @Log("查询定时任务")
    @ApiOperation("查询定时任务")
    @GetMapping
        public ResponseEntity getJobs(JobQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(quartzJobService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @Log("导出任务数据")
    @ApiOperation("导出任务数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, JobQueryCriteria criteria) throws IOException {
        quartzJobService.download(quartzJobService.queryAll(criteria), response);
    }

    @Log("导出日志数据")
    @ApiOperation("导出日志数据")
    @GetMapping(value = "/download/log")
        public void downloadLog(HttpServletResponse response, JobQueryCriteria criteria) throws IOException {
        quartzJobService.downloadLog(quartzJobService.queryAllLog(criteria), response);
    }

    @Log("查询任务执行日志")
    @ApiOperation("查询任务执行日志")
    @GetMapping(value = "/logs")
        public ResponseEntity getJobLogs(JobQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(quartzJobService.queryAllLog(criteria,pageable), HttpStatus.OK);
    }

    @Log("新增定时任务")
    @ApiOperation("新增定时任务")
    @PostMapping
        public ResponseEntity create(@Validated @RequestBody QuartzJob resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        return new ResponseEntity<>(quartzJobService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改定时任务")
    @ApiOperation("修改定时任务")
    @PutMapping
        public ResponseEntity update(@Validated(QuartzJob.Update.class) @RequestBody QuartzJob resources){
        quartzJobService.update(resources);
        return new ResponseEntity("ok",HttpStatus.NO_CONTENT);
    }

    @Log("更改定时任务状态")
    @ApiOperation("更改定时任务状态")
    @PutMapping(value = "/{id}")
        public ResponseEntity updateIsPause(@PathVariable Long id){
        quartzJobService.updateIsPause(quartzJobService.findById(id));
        return new ResponseEntity("ok",HttpStatus.NO_CONTENT);
    }

    @Log("执行定时任务")
    @ApiOperation("执行定时任务")
    @PutMapping(value = "/exec/{id}")
        public ResponseEntity execution(@PathVariable Long id){
        quartzJobService.execution(quartzJobService.findById(id));
        return new ResponseEntity("ok",HttpStatus.NO_CONTENT);
    }

    @Log("删除定时任务")
    @ApiOperation("删除定时任务")
    @DeleteMapping(value = "/{id}")
        public ResponseEntity delete(@PathVariable Long id){
        quartzJobService.delete(quartzJobService.findById(id));
        return new ResponseEntity("ok",HttpStatus.OK);
    }
}
