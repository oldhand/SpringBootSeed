package com.github.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 消息队列响应事件
 * @author oldhand
 * @date 2019-12-16
*/
@Slf4j
@Component
public class MqConsumerTask {

    public String test(String msg){
        log.info("执行队列! 参数为： {}", msg);
        return "执行队列成功";
    }
}
