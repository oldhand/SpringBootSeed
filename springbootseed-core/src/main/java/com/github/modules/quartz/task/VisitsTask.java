package com.github.modules.quartz.task;

import com.github.modules.monitor.service.VisitsService;
import org.springframework.stereotype.Component;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Component
public class VisitsTask {

    private final VisitsService visitsService;

    public VisitsTask(VisitsService visitsService) {
        this.visitsService = visitsService;
    }

    public void run(){
        visitsService.save();
    }
}
