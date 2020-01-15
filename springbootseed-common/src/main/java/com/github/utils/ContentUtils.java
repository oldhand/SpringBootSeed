package com.github.utils;

import com.github.domain.Authorization;
import com.github.service.ContentIdsService;
import com.github.service.YearContentIdsService;
import com.github.service.YearMonthContentIdsService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author oldhand
 * @date 2019-12-16
 *
*/

@Component
public class ContentUtils {

    private static ContentUtils  staticinstance;

    @Autowired
    private ContentIdsService contentidsservice;

    @Autowired
    private YearContentIdsService yearcontentidsservice;

    @Autowired
    private YearMonthContentIdsService yearmonthcontentidsservice;

    // 通过注解实现注入
    @PostConstruct
    public void init() {
        staticinstance = new ContentUtils();
        staticinstance.contentidsservice = contentidsservice;
        staticinstance.yearcontentidsservice = yearcontentidsservice;
        staticinstance.yearmonthcontentidsservice = yearmonthcontentidsservice;
    }

    public static Long makeContentId(String xnType) {
        return staticinstance.contentidsservice.create(xnType);
    }

    public static Long makeCYearontentId(String xnType) {
        return staticinstance.yearcontentidsservice.create(xnType);
    }

    public static Long makeYearMonthContentId(String xnType) {
        return staticinstance.yearmonthcontentidsservice.create(xnType);
    }

}
