package com.github.modules.quartz.service;

import com.github.modules.quartz.domain.QuartzJob;
import com.github.modules.quartz.domain.QuartzLog;
import com.github.modules.quartz.service.dto.JobQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author oldhand
 * @date 2019-12-16
*/
public interface QuartzJobService {

    Object queryAll(JobQueryCriteria criteria, Pageable pageable);

    List<QuartzJob> queryAll(JobQueryCriteria criteria);

    Object queryAllLog(JobQueryCriteria criteria, Pageable pageable);

    List<QuartzLog> queryAllLog(JobQueryCriteria criteria);

    QuartzJob create(QuartzJob resources);

    void update(QuartzJob resources);

    void delete(QuartzJob quartzJob);

    QuartzJob findById(Long id);

    /**
     * 更改定时任务状态
     * @param quartzJob /
     */
    void updateIsPause(QuartzJob quartzJob);

    /**
     * 立即执行定时任务
     * @param quartzJob /
     */
    void execution(QuartzJob quartzJob);

    void download(List<QuartzJob> queryAll, HttpServletResponse response) throws IOException;

    void downloadLog(List<QuartzLog> queryAllLog, HttpServletResponse response) throws IOException;
}
