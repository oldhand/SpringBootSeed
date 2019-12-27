package com.github.rabbitmq.rest;

import com.github.aop.log.Log;
import com.github.rabbitmq.producer.Sender;
import com.github.rabbitmq.domain.MqMessage;
import com.github.rabbitmq.service.MqService;
import com.github.rabbitmq.service.dto.MqDTO;
import com.github.rabbitmq.service.dto.MqQueryCriteria;
import com.github.utils.TimeUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2019-12-25
*/
@Api(tags = "工具：消息队列管理 (RabbitMQ)")
@RestController
@RequestMapping("/api/mq")
public class MqController {

    private final MqService mqService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Sender sender;

    public MqController(MqService mqService) {
        this.mqService = mqService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, MqQueryCriteria criteria) throws IOException {
        mqService.download(mqService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询队列")
    @ApiOperation("查询队列")
        public ResponseEntity getMqs(MqQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(mqService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增队列")
    @ApiOperation("新增队列")
        public ResponseEntity create(@Validated @RequestBody MqMessage resources){
        MqDTO mq = mqService.create(resources);
        Object result = sender.sendMessage(mq);
        if (result != null) {
            Map<String, String> res = (Map<String, String>)result;
            mq.setAck(Integer.parseInt(res.get("ack")));
            mq.setResult(res.get("result"));
            mq.setAcktime(new Timestamp(System.currentTimeMillis()));
        }
        return new ResponseEntity<>(mq,HttpStatus.CREATED);
    }


}