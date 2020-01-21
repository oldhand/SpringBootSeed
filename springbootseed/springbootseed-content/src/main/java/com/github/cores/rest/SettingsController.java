package com.github.cores.rest;

import com.github.aop.log.Log;
import com.github.cores.domain.Settings;
import com.github.cores.service.SettingsService;
import com.github.cores.service.dto.SettingsDTO;
import com.github.cores.service.dto.SettingsQueryCriteria;
import com.github.exception.BadRequestException;
import com.github.service.ContentIdsService;
import com.github.utils.AuthorizationUtils;
import com.github.utils.DateTimeUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
* @author oldhand
* @date 2020-01-15
*/
@Api(tags = "后台：系统设置管理")
@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private final SettingsService SettingsService;
	
	private final ContentIdsService ContentIdsService;

    public SettingsController(SettingsService SettingsService,ContentIdsService ContentIdsService) {
        this.SettingsService = SettingsService;
		this.ContentIdsService = ContentIdsService;
    }
	
    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateTimeUtils.timestampRequestDataBinder(binder);
    }

    @GetMapping
    @Log("查询系统设置")
    @ApiOperation("查询系统设置")
        public ResponseEntity getSettingss(){
        List<SettingsDTO> settings = SettingsService.query();
        if (settings.size() == 0) {
            throw new BadRequestException("没有找到系统设置数据");
        }
        return new ResponseEntity(settings.get(0),HttpStatus.OK);
    }
	


    @PutMapping
    @Log("修改系统设置")
    @ApiOperation("修改系统设置")
        public ResponseEntity update(@Validated @RequestBody Settings resources){
        return new ResponseEntity(SettingsService.update(resources),HttpStatus.OK);
    }
}