package com.github.rabbitmq.service;

import com.github.rabbitmq.domain.Mq;
import com.github.rabbitmq.domain.MqMessage;
import com.github.rabbitmq.service.dto.MqDTO;
import com.github.rabbitmq.service.dto.MqQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2019-12-25
*/
public interface MqService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(MqQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<MqDTO>
    */
    List<MqDTO> queryAll(MqQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return MqDTO
     */
    MqDTO findById(Long id);

    MqDTO create(MqMessage resources);


    void download(List<MqDTO> all, HttpServletResponse response) throws IOException;

    void updateAck(Long id,String ack,String result) throws Exception;
}