package com.github.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 测试用
 * @author oldhand
 * @date 2019-12-16
*/
@Slf4j
@Component
public class ConsumerTask {

    public String test(String msg){
        log.info("执行队列! 参数为： {}", msg);
        return "执行队列成功";
    }
}
