package com.github.rabbitmq.rest;

import com.github.aop.log.Log;
import com.github.rabbitmq.core.Sender;
import com.github.rabbitmq.domain.Mq;
import com.github.rabbitmq.domain.MqMessage;
import com.github.rabbitmq.service.MqService;
import com.github.rabbitmq.service.dto.MqDTO;
import com.github.rabbitmq.service.dto.MqQueryCriteria;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.text.MessageFormat;
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
    Sender sender;

//    @Autowired
//    private MqSender mqSender;

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
//        rabbitTemplate.convertAndSend(RabbitConstants.QUEUE,"1message from web");
//        rabbitTemplate.convertAndSend("exchange","topic.messages","2message from web for exchage");
//        rabbitTemplate.convertAndSend(RabbitConstants.EXCHANGE,RabbitConstants.ROUTINGKEY,"3message from web for fanoutExchange");
//
//        //主要是下面这个
//        mqSender.send("message from web for fanoutExchange1234234");

//        sender.sendMessage(new String[]{"mq测试"});
//        for (int i = 0; i < 10; i++) {
//            sender.sendMessage(MessageFormat.format("mq测试{0}",i));
//        }
        MqDTO mq = mqService.create(resources);
        System.out.println("-----------mq-----"+mq.toString()+"---------------");
        return new ResponseEntity<>(mq,HttpStatus.CREATED);
    }

    @PostMapping(value = "/add")
    @Log("add")
    @ApiOperation("add")
    public ResponseEntity add(@Validated @RequestBody Mq resources){

        return new ResponseEntity<>("",HttpStatus.CREATED);
    }


}