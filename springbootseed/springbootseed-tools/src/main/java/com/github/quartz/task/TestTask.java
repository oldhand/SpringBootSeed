package com.github.quartz.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 测试用
 * @author oldhand
 * @date 2019-12-16
*/
@Slf4j
@Component
public class TestTask {

    public void run(){
        log.info("执行成功");
    }

    public void run1(String str){
        log.info("执行成功，参数为： {}" + str);
    }
}
